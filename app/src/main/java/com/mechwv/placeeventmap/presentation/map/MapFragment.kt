package com.mechwv.placeeventmap.presentation.map

import android.Manifest
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.mechwv.placeeventmap.databinding.MapFragmentBinding
import com.google.android.material.snackbar.Snackbar
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.location.*
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.InputListener
import com.yandex.mapkit.mapview.MapView
import dagger.hilt.android.AndroidEntryPoint
import com.yandex.mapkit.map.Map

@AndroidEntryPoint
class MapFragment : Fragment() {
    private val viewModel: MapViewModel by viewModels()
    private lateinit var binding: MapFragmentBinding
    private var mapView: MapView? = null

    private var myLocationListener: LocationListener? = null
    private var locationManager: LocationManager? = null
    private var myLocation: Point? = null

    private val DESIRED_ACCURACY = 0.0
    private val MINIMAL_TIME: Long = 0
    private val MINIMAL_DISTANCE = 50.0
    private val USE_IN_BACKGROUND = false
    val COMFORTABLE_ZOOM_LEVEL = 18
    val OK_ZOOM_LEVEL = 10

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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        MapKitFactory.initialize(context)

        val appPerms = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        activityResultLauncher.launch(appPerms)


        binding = MapFragmentBinding.inflate(layoutInflater, container, false)
        mapView = binding.mapview
        val layout = binding.mapFragment

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

        mapView?.map?.addInputListener(viewModel.listener)


        myLocationListener = object : LocationListener {
            override fun onLocationUpdated(location: Location) {
                if (myLocation == null) {
                    moveCamera(location.getPosition(), COMFORTABLE_ZOOM_LEVEL.toFloat())
                }
                myLocation = location.getPosition()
                Log.w(
                    "MAP123123",
                    "my location - " + myLocation!!.getLatitude()
                        .toString() + "," + myLocation!!.getLongitude()
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

        return binding.root
    }

    private fun moveCamera(point: Point, zoom: Float) {
        mapView!!.map.move(
            CameraPosition(point, zoom, 0.0f, 0.0f),
            Animation(Animation.Type.SMOOTH, 1F),
            null
        )
    }



    override fun onStop() {
        super.onStop()
        mapView?.onStop()
        MapKitFactory.getInstance().onStop()
        locationManager?.unsubscribe(myLocationListener!!)
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
                myLocationListener!!
            )
        }
    }


}