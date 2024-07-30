package com.maps.route

import android.content.Context
import androidx.annotation.Keep
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.maps.route.model.Leg
import com.maps.route.model.TravelMode
import com.maps.route.utils.extensions.getColorCompat

/**
 * An interface for drawing routes on a Google Map using the Directions API.
 */
@Keep
interface DrawRouteSDK {

    /**
     * Draws a route on the given Google Map from the source location to the destination location.
     *
     * @param googleMap The [GoogleMap] instance where the route will be drawn.
     * @param travelMode The mode of travel to be used for routing. Defaults to [TravelMode.DRIVING].
     * @param source The starting point [LatLng] of the route.
     * @param destination The ending point [LatLng] of the route.
     * @param context The [Context] used for accessing resources.
     * @param color The color of the route line. Defaults to a color defined in resources (R.color.pathColor).
     * @param showMarkers A flag indicating whether markers should be displayed at the source and destination. Defaults to true.
     * @param boundMarkers A flag indicating whether the map should automatically adjust the camera to include both markers. Defaults to true.
     * @param polygonWidth The width of the route line in pixels. Defaults to 7.
     * @param estimates A lambda function to receive the travel estimates (distance and duration) as a [Leg] object.
     * @param error A lambda function to handle errors or exceptions encountered during the operation.
     */
    fun drawRoute(
        googleMap: GoogleMap,
        travelMode: TravelMode = TravelMode.DRIVING,
        source: LatLng,
        destination: LatLng,
        context: Context,
        color: Int = context.getColorCompat(R.color.pathColor),
        showMarkers: Boolean = true,
        boundMarkers: Boolean = true,
        polygonWidth: Int = 12,
        estimates: (Leg) -> Unit,
        error: (Throwable) -> Unit,
    )

    /**
     * Retrieves travel estimations between the source and destination using the Directions API.
     *
     * @param source The starting point [LatLng] of the route.
     * @param destination The ending point [LatLng] of the route.
     * @param travelMode The mode of travel to be used for routing. Defaults to [TravelMode.DRIVING].
     * @param estimates A lambda function to receive the travel estimates (distance and duration) as a [Leg] object.
     * @param error A lambda function to handle errors or exceptions encountered during the operation.
     */
    fun getTravelEstimations(
        source: LatLng,
        destination: LatLng,
        travelMode: TravelMode = TravelMode.DRIVING,
        estimates: (Leg) -> Unit,
        error: (Throwable) -> Unit,
    )

    /**
     * Remove the paths from maps.
     * @return nothing
     */
    fun removePaths()
}
