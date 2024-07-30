package com.maps.route

import android.content.Context
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.maps.route.data.DataState
import com.maps.route.data.DrawRouteBuilder
import com.maps.route.data.RepositoryDrawRoute
import com.maps.route.data.RepositoryDrawRouteImp
import com.maps.route.model.Leg
import com.maps.route.model.TravelMode
import com.maps.route.utils.extensions.boundMarkersOnMap
import com.maps.route.utils.extensions.drawMarker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

/**
 * This is an internal implementation of the [DrawRouteSDK] interface.
 * It handles drawing routes on a Google Map and fetching travel estimations using coroutines.
 *
 * @property scope The coroutine scope used for executing network requests on the IO dispatcher.
 * @property repositoryDrawRoute The repository responsible for fetching directions data.
 * @property gcpApiKey The Google Cloud Platform API key used for accessing Google Maps services.
 */
class DrawRouteSDKImpl(private var gcpApiKey: String) : DrawRouteSDK {

    // Coroutine scope for managing coroutines
    private val scope = CoroutineScope(Dispatchers.IO)
    // instance of RepositoryDrawRoute, creating manually since didn't use any DI framework
    private val repositoryDrawRoute: RepositoryDrawRoute = RepositoryDrawRouteImp()
    // instance of DrawRouteBuilder, created as nullable since we need it for 2 different methods
    private var drawRouteBuilder: DrawRouteBuilder? = null

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
    override fun drawRoute(
        googleMap: GoogleMap,
        travelMode: TravelMode,
        source: LatLng,
        destination: LatLng,
        context: Context,
        color: Int,
        showMarkers: Boolean,
        boundMarkers: Boolean,
        polygonWidth: Int,
        estimates: (Leg) -> Unit,
        error: (Throwable) -> Unit,
    ) {
        with(googleMap) {
            // If user needs the source and destination markers
            if (showMarkers) {
                drawMarker(location = source, context = context)
                drawMarker(location = destination, context = context)
            }

            // Creation of polyline with attributes
            drawRouteBuilder = DrawRouteBuilder.BuildRoute(this)
                .withColor(color)
                .withWidth(polygonWidth)
                .withAlpha(0.6f)
                .withMarkerIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
                .build()

            // Launch a coroutine to handle the network request and drawing the route
            scope.launch(Dispatchers.Main) { // Ensure UI operations are on the Main dispatcher
                repositoryDrawRoute.fetchDirections(source, destination, travelMode, gcpApiKey)
                    .catch { e ->
                        // Handle the exception and invoke the error lambda
                        error.invoke(e)
                    }
                    .collect { dataState ->
                        when (dataState) {
                            is DataState.Success -> {
                                val routes = dataState.data.routes

                                // Check if routes are available, provide estimates, and draw the path
                                if (routes.isNullOrEmpty().not()) {
                                    routes?.get(0)?.legs?.get(0)?.let {
                                        estimates.invoke(it)
                                    }
                                    drawRouteBuilder?.drawPath(routes!!)
                                    // If user requires to bound the markers with padding
                                    if (boundMarkers)
                                        boundMarkersOnMap(arrayListOf(source, destination))
                                }
                            }
                        }
                    }
            }
        }
    }

    /**
     * Retrieves travel estimations between the source and destination using the Directions API.
     *
     * @param source The starting point [LatLng] of the route.
     * @param destination The ending point [LatLng] of the route.
     * @param travelMode The mode of travel to be used for routing. Defaults to [TravelMode.DRIVING].
     * @param estimates A lambda function to receive the travel estimates (distance and duration) as a [Leg] object.
     * @param error A lambda function to handle errors or exceptions encountered during the operation.
     */
    override fun getTravelEstimations(
        source: LatLng,
        destination: LatLng,
        travelMode: TravelMode,
        estimates: (Leg) -> Unit,
        error: (Throwable) -> Unit,
    ) {
        // Launch a coroutine to handle the network request
        scope.launch(Dispatchers.Main) { // Ensure UI operations are on the Main dispatcher
            repositoryDrawRoute.fetchDirections(source, destination, travelMode, gcpApiKey)
                .catch { e ->
                    // Handle the exception and invoke the error lambda
                    error.invoke(e)
                }
                .collect { dataState ->
                    when (dataState) {
                        is DataState.Success -> {
                            val routes = dataState.data.routes
                            // Check if routes are available and provide estimates
                            if (routes.isNullOrEmpty().not()) {
                                routes?.get(0)?.legs?.get(0)?.let {
                                    estimates.invoke(it)
                                }
                            }
                        }
                    }
                }
        }
    }

    /**
     * Remove the paths from maps.
     * @return nothing
     */
    override fun removePaths() {
        drawRouteBuilder?.removePaths()
    }
}
