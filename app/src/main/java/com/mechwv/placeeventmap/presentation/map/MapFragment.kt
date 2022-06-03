package com.mechwv.placeeventmap.presentation.map

import android.Manifest
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar
import com.mechwv.placeeventmap.R
import com.mechwv.placeeventmap.databinding.BottomSheetDialogLayoutBinding
import com.mechwv.placeeventmap.databinding.MapFragmentBinding
import com.mechwv.placeeventmap.domain.model.Place
import com.mechwv.placeeventmap.presentation.dialogs.PlaceCreateDialog
import com.mechwv.placeeventmap.presentation.retrofit.model.geoApi.GeoPlace
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.BoundingBox
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.location.*
import com.yandex.mapkit.map.*
import com.yandex.mapkit.map.Map
import com.yandex.mapkit.mapview.MapView
import com.yandex.mapkit.search.*
import com.yandex.runtime.Error
import com.yandex.runtime.image.AnimatedImageProvider
import com.yandex.runtime.image.ImageProvider
import com.yandex.runtime.network.NetworkError
import com.yandex.runtime.network.RemoteError
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch


@ExperimentalCoroutinesApi
@AndroidEntryPoint
class MapFragment : SuggestSession.SuggestListener, Session.SearchListener, Fragment() {
    private val viewModel: MapViewModel by viewModels()
    private lateinit var binding: MapFragmentBinding
    private var mapView: MapView? = null

    private var locationManager: LocationManager? = null
    private var mapObjects: MapObjectCollection? = null
    private var myLocation: Point? = null

    private lateinit var searchManager: SearchManager
    private lateinit var searchSession: Session
    private var suggestSession: SuggestSession? = null
    private var suggestResultView: ListView? = null
    private var resultAdapter: ArrayAdapter<*>? = null
    private var suggestResult: MutableList<Any> = mutableListOf()
    private lateinit var layout: RelativeLayout
    private lateinit var supportFragmentManager: FragmentManager

    private lateinit var mBottomSheetLayout: LinearLayout
    private lateinit var mBottomSheetLayoutBinding: BottomSheetDialogLayoutBinding
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>

    //Constants
    private val DESIRED_ACCURACY = 0.0
    private val MINIMAL_TIME: Long = 0
    private val MINIMAL_DISTANCE = 50.0
    private val USE_IN_BACKGROUND = false
    val COMFORTABLE_ZOOM_LEVEL = 15//15
    val OK_ZOOM_LEVEL = 10//10

    private val CENTER = Point(55.75, 37.62)
    private val BOX_SIZE = 0.2
    private val BOUNDING_BOX = BoundingBox(
        Point(CENTER.latitude - BOX_SIZE, CENTER.longitude - BOX_SIZE),
        Point(CENTER.latitude + BOX_SIZE, CENTER.longitude + BOX_SIZE)
    )
    private val SEARCH_OPTIONS = SuggestOptions().setSuggestTypes(
        SuggestType.GEO.value or SuggestType.BIZ.value or SuggestType.TRANSIT.value)

    private val RESULT_NUMBER_LIMIT = 5

    private var activityResultLauncher: ActivityResultLauncher<Array<String>> =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()) { result ->
            var allAreGranted = true
            for(b in result.values) {
                allAreGranted = allAreGranted && b
            }
            if(allAreGranted) {

            }
        }

    private val listener = object : InputListener {
        override fun onMapLongTap(p0: Map, p1: Point) {}

        override fun onMapTap(map: Map, point: Point) {
            Log.d("Map touch","You have touched $point")
            createPlacemark(point, null, "animation.png")
            if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            }
            showDialog(point)
        }
    }

    private val myLocationListener = object : LocationListener {
        override fun onLocationUpdated(location: Location) {
            if (myLocation == null) {
                moveCamera(location.position, COMFORTABLE_ZOOM_LEVEL.toFloat())
            }
            myLocation = location.position
            Log.w(
                "MAP123123",
                "my location - " + myLocation!!.latitude
                    .toString() + "," + myLocation!!.longitude
            )
        }

        override fun onLocationStatusUpdated(locationStatus: LocationStatus) {
            if (locationStatus == LocationStatus.NOT_AVAILABLE) {
                Snackbar.make(
                    layout,
                    "location error",
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = MapFragmentBinding.inflate(layoutInflater, container, false)
        supportFragmentManager = childFragmentManager
        init()
        return binding.root
    }
    private fun init() {
        MapKitFactory.initialize(context)
        SearchFactory.initialize(context)

        val appPerms = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION)

        val searchEdit = binding.searchContainer
        val searchText = binding.searchText

        activityResultLauncher.launch(appPerms)
        mapView = binding.mapview
        layout = binding.mapFragment

        mBottomSheetLayoutBinding = binding.bottomSheet
        mBottomSheetLayout = binding.bottomSheet.bottomSheetLayout
        bottomSheetBehavior = BottomSheetBehavior.from(mBottomSheetLayout)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

        suggestResultView = binding.suggestResult
        resultAdapter = ArrayAdapter(
                context!!,
                android.R.layout.simple_list_item_1,
                android.R.id.text1,
                suggestResult
            )
        suggestResultView?.adapter = resultAdapter
        suggestResultView?.setOnItemClickListener { adapterView, view, pos, id ->
            val element = adapterView?.getItemAtPosition(pos)
            searchText.setText(element.toString())
        }

        mapObjects = mapView?.map?.mapObjects
        searchManager = SearchFactory.getInstance().createSearchManager(SearchManagerType.COMBINED)
        suggestSession = searchManager.createSuggestSession()


        searchEdit.setEndIconOnClickListener {
//            Toast.makeText(context, searchEdit.editText.text.toString(), Toast.LENGTH_SHORT).show()
            if (searchEdit.editText?.text.toString() != "") {
                viewModel.getAddressByString(searchEdit.editText?.text.toString())
                    .observe(viewLifecycleOwner) {
                        if (it != GeoPlace()) {
                            Toast.makeText(
                                context,
                                "${it.name} + ${it.lat} + ${it.long}",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                            moveCamera(Point(it.lat, it.long), zoom = COMFORTABLE_ZOOM_LEVEL.toFloat())
                            suggestResultView!!.visibility = View.INVISIBLE
                        } else {
                            Toast.makeText(
                                context,
                                "Sorry, the place is unavailable",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }
                    }
            }
        }

        searchText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(editable: Editable?) {
                requestSuggest(editable.toString())
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        searchText.setOnEditorActionListener { textView, actionId, keyEvent ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                if (searchEdit.editText?.text.toString() != "") {
                    viewModel.getAddressByString(searchEdit.editText.toString())
                        .observe(viewLifecycleOwner) {
                            if (it != GeoPlace()) {
                                Toast.makeText(
                                    context,
                                    "${it.name} + ${it.lat} + ${it.long}",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                                moveCamera(Point(it.lat, it.long), zoom = COMFORTABLE_ZOOM_LEVEL.toFloat())
                                suggestResultView!!.visibility = View.INVISIBLE
                            } else {
                                Toast.makeText(
                                    context,
                                    "Sorry, the place is unavailable",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                            }
                        }
                }
            }
            false
        }

        setMarkers()

        moveCameraToPlace()

        mapView?.map?.addInputListener(listener)
    }

    fun setMarkers() {
        viewModel.DBPlaces.observe(viewLifecycleOwner) {
            it.forEach { p ->
                createPlacemark(Point(p.latitude, p.longitude), p)
//                mapObjects?.addPlacemark(
//                    Point(p.latitude, p.longitude),
//                    ImageProvider.fromResource(context, R.drawable.search_result)
//                )
            }
        }
    }

    fun showDialog(point: Point) {
        val dialog = PlaceCreateDialog()
        dialog.arguments = bundleOf("longitude" to point.longitude, "latitude" to point.latitude)
        dialog.show(childFragmentManager, "tag")
    }

    private fun moveCameraToPlace() {
        val uid = arguments?.get("place_id") as Int
        Log.e("uid","$uid")
        if (uid != -1) {
            viewModel.getPlace(uid).observe(viewLifecycleOwner) {
                Log.e("COORDINATES", "${it.latitude}, ${it.longitude}")
                moveCamera(Point(it.latitude, it.longitude), COMFORTABLE_ZOOM_LEVEL.toFloat())
                createPlacemark(Point(it.latitude, it.longitude), it)
            }
        } else {
            locationManager = MapKitFactory.getInstance().createLocationManager()
            mapView!!.map.move(
                CameraPosition(Point(55.751574, 37.573856), 11.0f, 0.0f, 0.0f),
                Animation(Animation.Type.SMOOTH, 0F),
                null
            )
        }
    }

    private fun moveCamera(point: Point, zoom: Float) {
        mapView!!.map.move(
            CameraPosition(point, zoom, 0.0f, 0.0f),
            Animation(Animation.Type.SMOOTH, 1F),
            null
        )
    }

    private fun createPlacemark(geoPoint: Point, data: Place?, assetName: String = "star.png") {
        lifecycleScope.launch{
            val imageProvider = AnimatedImageProvider.fromAsset(context, assetName)
            val animatedPlacemark =
                mapObjects!!.addPlacemark(geoPoint, imageProvider, IconStyle().setScale(1f))
            animatedPlacemark.userData = data
            animatedPlacemark.addTapListener(mapObjectTapListener)
            animatedPlacemark.useAnimation().play()
        }
        return
    }

    private val mapObjectTapListener =
        MapObjectTapListener { mapObject, point ->
            if (mapObject is PlacemarkMapObject) {
                val userData = mapObject.userData
                if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_HIDDEN) {
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                }
                if (userData is Place) {
                    val place : Place = userData
                    mBottomSheetLayoutBinding.place = place
                    viewModel.getEventName(place).observe(viewLifecycleOwner) {
                        if ((it != "") && (it != null))
                            mBottomSheetLayoutBinding.eventNameText.text = it
                    }
                }
            }
            true
        }

    override fun onStop() {
        super.onStop()
        mapView?.onStop()
        MapKitFactory.getInstance().onStop()
        locationManager?.unsubscribe(myLocationListener)
    }

    override fun onStart() {
        super.onStart()
        mapView?.onStart()
        MapKitFactory.getInstance().onStart()
        subscribeToLocationUpdate()
    }

    private fun subscribeToLocationUpdate() {
        if (locationManager != null && myLocationListener != null) {
            locationManager!!.subscribeForLocationUpdates(
                DESIRED_ACCURACY,
                MINIMAL_TIME,
                MINIMAL_DISTANCE,
                USE_IN_BACKGROUND,
                FilteringMode.OFF,
                myLocationListener
            )
        }
    }

    override fun onSearchResponse(response: Response) {
        val mapObjects = mapView!!.map.mapObjects
        mapObjects.clear()
        Log.e("SEARCH", "STARTED")
        for (searchResult in response.collection.children) {
            val resultLocation = searchResult.obj!!.geometry[0].point
            if (resultLocation != null) {
                Log.e("SEARCH", "${resultLocation.latitude}, ${resultLocation.longitude}")
                mapObjects.addPlacemark(
                    resultLocation,
                    ImageProvider.fromResource(context, R.drawable.search_result)
                )
            }
        }
    }

    override fun onSearchError(error: Error) {
        var errorMessage = getString(R.string.unknown_error_message)
        if (error is RemoteError) {
            errorMessage = getString(R.string.remote_error_message)
        } else if (error is NetworkError) {
            errorMessage = getString(R.string.network_error_message)
        }

        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
    }

    override fun onResponse(suggest: List<SuggestItem>) {
        suggestResult.clear()
        for (i in 0 until Math.min(RESULT_NUMBER_LIMIT, suggest.size)) {
            suggest[i].displayText?.let { suggestResult.add(it) }
        }
        resultAdapter!!.notifyDataSetChanged()
        suggestResultView!!.visibility = View.VISIBLE
    }

    override fun onError(error: Error) {
        var errorMessage = getString(R.string.unknown_error_message)
        if (error is RemoteError) {
            errorMessage = getString(R.string.remote_error_message)
        } else if (error is NetworkError) {
            errorMessage = getString(R.string.network_error_message)
        }
        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
    }

    private fun requestSuggest(query: String) {
        suggestResultView!!.visibility = View.INVISIBLE
        suggestSession!!.suggest(query, BOUNDING_BOX, SEARCH_OPTIONS, this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mapObjects?.clear()
        mapView?.onStop()
        MapKitFactory.getInstance().onStop()
        locationManager?.unsubscribe(myLocationListener)
    }

}