package com.maps.sample

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.maps.route.extensions.drawMarker
import com.maps.route.extensions.drawRouteOnMap
import com.maps.route.extensions.getTravelEstimations
import com.maps.route.extensions.moveCameraOnMap

class RouteFragment : Fragment(), OnMapReadyCallback {

    private var googleMap: GoogleMap? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_map_route, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //initialized google maps
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.mapView) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(p0: GoogleMap?) {
        this.googleMap = p0

        val source = LatLng(31.490127, 74.316971) //starting point (LatLng)
        val destination = LatLng(31.474316, 74.316112) // ending point (LatLng)

        //if you only want the Estimates (Distance & Time of arrival)
        getTravelEstimations(
            mapsApiKey = getString(R.string.google_map_api_key),
            source = source,
            destination = destination
        ) { estimates ->
            estimates?.let {
                //Google Estimated time of arrival
                Log.d(TAG, "ETA: ${it.duration?.text}, ${it.duration?.value}")
                //Google suggested path distance
                Log.d(TAG, "Distance: ${it.distance?.text}, ${it.distance?.text}")

            } ?: Log.e(TAG, "Nothing found")
        }


        googleMap?.run {
            //if you want to move the map on specific location
            moveCameraOnMap(latLng = source)

            //if you want to drop a marker of maps, call it
            drawMarker(location = source, context = requireContext(), title = "test marker")

            //if you only want to draw a route on maps
            //Called the drawRouteOnMap extension to draw the polyline/route on google maps
            drawRouteOnMap(
                getString(R.string.google_map_api_key),
                source = source,
                destination = destination,
                context = context!!
            )

            //if you only want to draw a route on maps and also need the ETAs then implement the EstimationsCallBack and pass the ref like this
            //Called the drawRouteOnMap extension to draw the polyline/route on google maps
            drawRouteOnMap(
                getString(R.string.google_map_api_key),
                source = source,
                destination = destination,
                context = context!!,
            ) { estimates ->
                estimates?.let {
                    //Google Estimated time of arrival
                    Log.d(TAG, "ETA: ${it.duration?.text}, ${it.duration?.value}")
                    //Google suggested path distance
                    Log.d(TAG, "Distance: ${it.distance?.text}, ${it.distance?.text}")

                } ?: Log.e(TAG, "Nothing found")
            }
        }
    }

    companion object {
        var TAG = RouteFragment::javaClass.name
    }
}
