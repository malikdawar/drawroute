package com.maps.sample

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.maps.route.DrawRouteSDK
import com.maps.route.DrawRouteSDKImpl
import com.maps.route.model.TravelMode
import com.maps.route.utils.extensions.drawMarker
import com.maps.route.utils.extensions.getColorCompat
import com.maps.route.utils.extensions.moveCameraOnMap

class RouteFragment : Fragment(), OnMapReadyCallback {

    private lateinit var googleMap: GoogleMap
    private val drawRouteSDK: DrawRouteSDK =
        DrawRouteSDKImpl("YOUR_API_KEY_HERE")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_map_route, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //  initialized google maps
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.mapView) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(p0: GoogleMap) {
        this.googleMap = p0

        val source = LatLng(35.91927118264419, 14.472031528630778) //starting point (LatLng)
        val destination = LatLng(35.91176380055831, 14.464306767283505) // ending point (LatLng)

        drawRouteSDK.getTravelEstimations(
            source = source,
            destination = destination,
            travelMode = TravelMode.DRIVING,
            estimates = {
                println("$TAG: getTravelEstimations::estimates ${it.duration}")
            },
            error = {
                println("$TAG: getTravelEstimations:: ${it.message}")
            }
        )

        googleMap.run {
            //if you want to move the map on specific location
            moveCameraOnMap(latLng = source)
            //if you want to drop a marker of maps, call it
            drawMarker(location = source, context = requireContext(), title = "test marker")

            //if you only want to draw a route on maps and also need the ETAs then implement the EstimationsCallBack and pass the ref like this
            //Called the drawRouteOnMap extension to draw the polyline/route on google maps
            drawRouteSDK.drawRoute(
                googleMap = googleMap,
                source = source,
                destination = destination,
                context = requireContext(),
                travelMode = TravelMode.DRIVING,
                color = requireContext().getColorCompat(R.color.red),
                estimates = {
                    println("$TAG: drawRoute::estimates ${it.duration}")
                }, error = {
                    println("$TAG: drawRoute::error ${it.message}")
                }
            )
        }
    }

    companion object {
        var TAG = RouteFragment::javaClass.name
    }
}