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
import com.maps.route.extensions.drawRouteOnMap
import com.maps.route.extensions.moveCameraOnMap
import io.reactivex.rxjava3.disposables.Disposable

class RouteFragment : Fragment(), OnMapReadyCallback {

    private var googleMap: GoogleMap? = null
    private var disposable:Disposable?=null

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

        googleMap?.run {
            moveCameraOnMap(latLng = source)

            //Called the drawRouteOnMap extension to draw the polyline/route on google maps
           disposable =  drawRouteOnMap(
                getString(R.string.google_map_api_key),
                source = source,
                destination = destination,
                context = context!!
            )
        }
    }

    override fun onDestroy() {
        disposable?.dispose()
        super.onDestroy()
    }
}
