package com.maps.sample

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.maps.route.RouteDrawer
import com.maps.route.RouteRest
import com.maps.route.model.Routes
import com.maps.route.model.TravelMode
import com.maps.route.parser.RouteJsonParser
import com.maps.sample.extensions.getColorCompat
import com.maps.sample.utils.MapUtils
import org.koin.java.KoinJavaComponent
import rx.android.schedulers.AndroidSchedulers

class RouteFragment : Fragment(), OnMapReadyCallback {

    // MapUtils helper class from Koin, DI frame work
    private val mapUtils: MapUtils by KoinJavaComponent.inject(
        MapUtils::class.java
    )

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

        googleMap?.run {
            //Called the drawRouteOnMap method to draw the polyline/route on google maps
            drawRouteOnMap(this, "Your_API_Key", source = source, destination = destination)

            mapUtils.let {
                //Dropped the marker on source
                it.drawMarker(
                    this,
                    source,
                    R.drawable.ic_location,
                    null
                )
                //Dropped the marker on destination
                it.drawMarker(
                    this,
                    destination,
                    R.drawable.ic_location,
                    null
                )
            }
        }
    }

    //Method to create the path on maps
    private fun drawRouteOnMap(
        googleMap: GoogleMap,
        mapsApiKey: String,
        source: LatLng,
        destination: LatLng,
        color: Int = context!!.getColorCompat(R.color.colorPrimary)
    ) {
        //creation of polyline with attributes
        val routeDrawer = RouteDrawer.RouteDrawerBuilder(googleMap)
            .withColor(color)
            .withWidth(13)
            .withAlpha(0.6f)
            .withMarkerIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
            .build()

        //API call to get the path points from google
        val routeRest = RouteRest()
        routeRest.getJsonDirections(
            source, destination, //starting and ending point
            TravelMode.DRIVING, //Travel mode
            mapsApiKey //google maps API from GCP, make sure google directions are enabled
        )
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.map { s -> RouteJsonParser<Routes>().parse(s, Routes::class.java) }
            ?.subscribe { r -> routeDrawer.drawPath(r) }
    }
}
