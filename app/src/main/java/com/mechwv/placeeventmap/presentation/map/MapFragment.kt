package com.mechwv.placeeventmap.presentation.map

import android.Manifest
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.mechwv.placeeventmap.R
import com.mechwv.placeeventmap.databinding.MapFragmentBinding
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
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
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MapFragment : Session.SearchListener, Fragment() {
    private val viewModel: MapViewModel by viewModels()
    private lateinit var binding: MapFragmentBinding
    private var mapView: MapView? = null

    private var locationManager: LocationManager? = null
    private var mapObjects: MapObjectCollection? = null
    private var myLocation: Point? = null

    private lateinit var searchManager: SearchManager
    private lateinit var searchSession: Session
    private lateinit var layout: RelativeLayout

    //Constants
    private val DESIRED_ACCURACY = 0.0
    private val MINIMAL_TIME: Long = 0
    private val MINIMAL_DISTANCE = 50.0
    private val USE_IN_BACKGROUND = false
    val COMFORTABLE_ZOOM_LEVEL = 5//15
    val OK_ZOOM_LEVEL = 10//10

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
            createPlacemark(point, "", "")
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
        init()
        return binding.root
    }
    private fun init() {
        MapKitFactory.initialize(context)

        val appPerms = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        activityResultLauncher.launch(appPerms)
        mapView = binding.mapview
        layout = binding.mapFragment

        mapObjects = mapView?.map?.mapObjects
        searchManager = SearchFactory.getInstance().createSearchManager(SearchManagerType.COMBINED)
        val searchEdit = binding.searchEdit


        searchEdit.setOnEditorActionListener { textView, actionId, keyEvent ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                submitQuery(searchEdit.text.toString())
            }
            false
        }

        moveCameraToPlace()

        mapView?.map?.addInputListener(listener)
    }

    private fun moveCameraToPlace() {
        val uid = arguments?.get("place_id") as Int
        Log.e("uid","$uid")
        if (uid != -1) {
            viewModel.getPlace(uid).observe(viewLifecycleOwner) {
                Log.e("COORDINATES", "${it.latitude}, ${it.longtitude}")
                moveCamera(Point(it.latitude, it.longtitude), OK_ZOOM_LEVEL.toFloat())
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

    private fun createPlacemark(geoPoint: Point, name: String, description: String) {
        lifecycleScope.launch{
            val imageProvider = AnimatedImageProvider.fromAsset(context, "sf1.png")
            val animatedPlacemark =
                mapObjects!!.addPlacemark(geoPoint, imageProvider, IconStyle())
            animatedPlacemark.useAnimation().play()
        }
        return
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

    private fun submitQuery(query: String) {
        searchSession = searchManager.submit(
            query,
            VisibleRegionUtils.toPolygon(mapView!!.map.visibleRegion),
            SearchOptions(),
            this
        )
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


}