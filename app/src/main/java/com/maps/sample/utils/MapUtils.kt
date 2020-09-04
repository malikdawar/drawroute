package com.maps.sample.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.*
import com.maps.sample.App

class MapUtils internal constructor(private val context: Context?) {

    /**
     * Helper method to draw a marker
     * @return void
     * @param googleMap Google maps instance
     * @param location latLng where the marker is required
     * @param resDrawable The image/drawable of marker
     * @param title optional title of marker
     * @author Dawar Malik.
     */
    fun drawMarker(
        googleMap: GoogleMap,
        location: LatLng?,
        resDrawable: Int,
        title: String? = null
    ) {
        val circleDrawable = ContextCompat.getDrawable(context!!, resDrawable)
        val markerIcon = getMarkerIconFromDrawable(circleDrawable)
        googleMap.addMarker(
            MarkerOptions()
                .position(location!!)
                .title(title)
                .icon(markerIcon)
        )
    }

    /**
     * Helper method to zoom on map
     * @return void
     * @param googleMap Google maps instance
     * @param zoom zoom level, by default its 15.5f
     * @param animate if animation is required, by default it's true
     * @param latLng latLng where the zoom is required
     * @author Dawar Malik.
     */
    fun moveCamera(
        googleMap: GoogleMap?,
        zoom: Float = 15.5f,
        animate: Boolean = true,
        latLng: LatLng
    ) {
        if (animate) {
            googleMap?.animateCamera(
                CameraUpdateFactory.newCameraPosition(
                    CameraPosition.Builder().target(LatLng(latLng.latitude, latLng.longitude)).zoom(
                        zoom
                    ).build()
                )
            )
        } else {
            googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom))
            googleMap?.animateCamera(
                CameraUpdateFactory.zoomTo(zoom)
            )
        }
    }


    /**
     * Helper method to bound all the markers on map
     * @return void
     * @param googleMap Google maps instance
     * @param padding by default it's 425
     * @param latLng array of all the latlng taht are require to bound
     * @author Dawar Malik.
     */
    fun boundMarkersOnMap(
        googleMap: GoogleMap,
        latLng: ArrayList<LatLng>,
        padding: Int = 425
    ) {
        val builder = LatLngBounds.Builder()
        for (marker in latLng) {
            builder.include(marker)
        }
        val bounds = builder.build()
        val cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, padding)
        googleMap.moveCamera(cameraUpdate)
    }

    /**
     * Helper method to get BitmapDescriptor from drawable
     * @return void
     * @param drawable Drawable resource
     * @author Dawar Malik.
     */
    private fun getMarkerIconFromDrawable(drawable: Drawable?): BitmapDescriptor {
        val canvas = Canvas()
        val bitmap = Bitmap.createBitmap(
            drawable!!.intrinsicWidth,
            drawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        canvas.setBitmap(bitmap)
        drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
        drawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

    companion object {
        var INSTANCE: MapUtils? = null
        fun getInstance(): MapUtils? {
            INSTANCE =
                MapUtils(App.getAppContext())
            return INSTANCE
        }
    }
}
