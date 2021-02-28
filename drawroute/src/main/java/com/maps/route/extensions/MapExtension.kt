package com.maps.route.extensions

import android.content.Context
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.*
import com.maps.route.R
import com.maps.route.RouteDrawer
import com.maps.route.RouteRest
import com.maps.route.callbacks.EstimationsCallBack
import com.maps.route.model.Legs
import com.maps.route.model.Routes
import com.maps.route.model.TravelMode
import com.maps.route.parser.RouteJsonParser
import com.maps.route.utils.MapUtils
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.annotations.NonNull
import io.reactivex.rxjava3.disposables.Disposable


/**
 * Extension function to draw a marker.
 * @param location latLng where the marker is required
 * @param resDrawable The image/drawable of marker
 * @param title optional title of marker
 * @author Dawar Malik.
 */
fun GoogleMap.drawMarker(
    location: LatLng?,
    context: Context,
    resDrawable: Int = R.drawable.ic_location,
    title: String? = null
) {
    val circleDrawable = ContextCompat.getDrawable(context, resDrawable)
    val markerIcon = MapUtils.getMarkerIconFromDrawable(circleDrawable)
    addMarker(
        MarkerOptions()
            .position(location!!)
            .title(title)
            .icon(markerIcon)
    )
}

/**
 * Extension function to zoom on map.
 * @param zoom zoom level, by default its 15.5f
 * @param animate if animation is required, by default it's true
 * @param latLng latLng where the zoom is required
 * @author Dawar Malik.
 */
fun GoogleMap.moveCameraOnMap(
    zoom: Float = 15.5f,
    animate: Boolean = true,
    latLng: LatLng
) {
    if (animate) {
        animateCamera(
            CameraUpdateFactory.newCameraPosition(
                CameraPosition.Builder().target(LatLng(latLng.latitude, latLng.longitude)).zoom(
                    zoom
                ).build()
            )
        )
    } else {
        moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom))
        animateCamera(
            CameraUpdateFactory.zoomTo(zoom)
        )
    }
}


/**
 * Extension function to draw path/route on map.
 * @param mapsApiKey google maps API from GCP, make sure google directions are enabled
 * @param context Context
 * @param source source point from where the path is required
 * @param destination destination point till where the path is required
 * @param color not required as by default = #6200EE
 * @param markers not required as by default = true
 * @param boundMarkers not required as by default = true
 * @param polygonWidth not required as by default = 13
 * @param travelMode not required as by default = DRIVING, can be DRIVING, WALKING, BICYCLING, TRANSIT
 * @param @Deprecated("Use lambda instead of interface")
 * estimations: ((Legs?) -> Unit)? = null //To get the Estimations(Time of arrival and distance) please call the lambda to get the values
 * @author Dawar Malik.
 */
fun GoogleMap.drawRouteOnMap(
    mapsApiKey: String,
    context: Context,
    source: LatLng,
    destination: LatLng,
    color: Int = context.getColorCompat(R.color.pathColor),
    markers: Boolean = true,
    boundMarkers: Boolean = true,
    polygonWidth: Int = 7,
    travelMode: TravelMode = TravelMode.DRIVING,
    estimationsCallBack: EstimationsCallBack? = null, //@Deprecated("Use lambda instead of interface")
    estimates: ((Legs?) -> Unit)? = null  // To get the Estimations(Time of arrival and distance) please call the lambda to get the values
) {

    // if user need the source and destination markers
    if (markers) {
        context.run {
            drawMarker(location = source, context = this)
            drawMarker(location = destination, context = this)
        }
    }

    //creation of polyline with attributes
    val routeDrawer = RouteDrawer.RouteDrawerBuilder(this)
        .withColor(color)
        .withWidth(polygonWidth)
        .withAlpha(0.6f)
        .withMarkerIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        .build()

    RouteRest().apply {
        getJsonDirections(
            source, destination, //starting and ending point
            travelMode, //Travel mode
            mapsApiKey //google maps API from GCP, make sure google directions are enabled
        )?.observeOn(AndroidSchedulers.mainThread())
            ?.map { s -> RouteJsonParser<Routes>().parse(s, Routes::class.java) }
            ?.subscribe { r ->
                val routes = r.routes

                if (routes.isNullOrEmpty().not()) {
                    routes!![0].legs?.get(0).let {
                        estimationsCallBack?.routeEstimations(it)
                        estimates?.invoke(it)
                    }
                    routeDrawer.drawPath(r)
                    // if user requires to bound the markers with padding
                    if (boundMarkers)
                        boundMarkersOnMap(arrayListOf(source, destination))
                } else {
                    estimationsCallBack?.routeEstimations(null)
                    estimates?.invoke(null)
                }
            }
    }
}


/**
 * Extension function to get ETA(Estimated time of arrival). Just call the method as simple getTravelEstimations(.......)
in your activity and get the ETA, don't forget to implement the Estimations interface in that view.
 * @param mapsApiKey google maps API from GCP, make sure google directions are enabled
 * @param context Context
 * @param source source point from where the path is required
 * @param destination destination point till where the path is required
 * @param travelMode not required as by default = DRIVING, can be DRIVING, WALKING, BICYCLING, TRANSIT
 * @param estimates lambda to get the estimates data from google
 * @author Dawar Malik.
 */
fun getTravelEstimations(
    mapsApiKey: String,
    source: LatLng,
    destination: LatLng,
    travelMode: TravelMode = TravelMode.DRIVING,
    estimationsCallBack: EstimationsCallBack? = null,
    estimates: ((Legs?) -> Unit)? = null  // To get the Estimations(Time of arrival and distance) please call the lambda to get the values
) {
    //API call to get the path points from google
    val routeRest = RouteRest()
    routeRest.getJsonDirections(
        source, destination, //starting and ending point
        travelMode, //Travel mode
        mapsApiKey //google maps API from GCP, make sure google directions are enabled
    )?.observeOn(AndroidSchedulers.mainThread())
        ?.map { s -> RouteJsonParser<Routes>().parse(s, Routes::class.java) }
        ?.subscribe { r ->
            r.routes.apply {
                if (isNullOrEmpty().not()) {
                    this!![0].legs?.get(0).let {
                        estimationsCallBack?.routeEstimations(it)
                        estimates?.invoke(it)
                    }
                } else {
                    estimationsCallBack?.routeEstimations(null)
                    estimates?.invoke(null)
                }
            }
        }
}

/**
 * Extension function to bound all the markers on map.
 * @param padding by default it's 425
 * @param latLng array of all the latlng that are required to bound.
 * @author Dawar Malik.
 */
fun GoogleMap.boundMarkersOnMap(
    latLng: ArrayList<LatLng>,
    padding: Int = 5
) {
    val builder = LatLngBounds.Builder()
    for (marker in latLng) {
        builder.include(marker)
    }
    val bounds = builder.build()
    val cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, padding)
    moveCamera(cameraUpdate)
}
