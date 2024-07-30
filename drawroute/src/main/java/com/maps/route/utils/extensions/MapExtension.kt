package com.maps.route.utils.extensions

import android.content.Context
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.maps.route.R


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
    val markerIcon = circleDrawable?.getMarkerIconFromDrawable()
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
    zoom: Float = 14.0f,
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
