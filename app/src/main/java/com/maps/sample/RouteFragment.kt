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

/**
 * A [Fragment] that displays a Google Map and draws a route between two locations.
 * Implements [OnMapReadyCallback] to handle the Google Map setup.
 *
 * This fragment demonstrates how to:
 * - Initialize Google Maps in a fragment.
 * - Use the [DrawRouteSDK] to fetch and display a route between two points.
 * - Display travel estimations like duration and distance.
 * - Handle map interactions such as adding markers and moving the camera.
 */
class RouteFragment : Fragment(), OnMapReadyCallback {

    // Reference to the GoogleMap object once it is ready
    private lateinit var googleMap: GoogleMap

    // Initialize the DrawRouteSDK with your Google Maps API key
    private val drawRouteSDK: DrawRouteSDK = DrawRouteSDKImpl("YOUR_API_KEY_HERE")

    /**
     * Inflates the layout for this fragment.
     *
     * @param inflater The LayoutInflater object that can be used to inflate any views in the fragment.
     * @param container If non-null, this is the parent view that the fragment's UI should be attached to.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here.
     * @return The View for the fragment's UI, or null.
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_map_route, container, false)
    }

    /**
     * Called immediately after [onCreateView] has returned, and the fragment's view hierarchy is created.
     * Sets up the Google Map by requesting the map fragment asynchronously.
     *
     * @param view The View returned by [onCreateView].
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize the Google Map by finding the SupportMapFragment
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.mapView) as SupportMapFragment
        // Asynchronously request the Google Map to be ready
        mapFragment.getMapAsync(this)
    }

    /**
     * Callback triggered when the Google Map is ready to be used.
     * Sets up the initial camera position, adds markers, and draws routes.
     *
     * @param googleMap The GoogleMap object representing the Google Map.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap

        // Define source and destination locations using LatLng
        val source = LatLng(35.91927118264419, 14.472031528630778) // starting point (LatLng)
        val destination = LatLng(35.91176380055831, 14.464306767283505) // ending point (LatLng)

        // Fetch travel estimations between source and destination using the DrawRouteSDK
        drawRouteSDK.getTravelEstimations(
            source = source,
            destination = destination,
            travelMode = TravelMode.DRIVING,
            estimates = { leg ->
                // Handle the successful retrieval of travel estimations
                println("$TAG: getTravelEstimations::estimates ${leg?.duration}")
            },
            error = { throwable ->
                // Handle any errors that occurred during the estimation retrieval
                println("$TAG: getTravelEstimations::error ${throwable.message}")
            }
        )

        // Configure the Google Map with camera movement, markers, and route drawing
        googleMap.run {
            // Move the camera to the source location
            moveCameraOnMap(latLng = source)

            // Drop a marker at the source location with a title
            drawMarker(location = source, context = requireContext(), title = "test marker")

            // Draw the route on the map and get the estimated time of arrival (ETA)
            drawRouteSDK.drawRoute(
                googleMap = googleMap,
                source = source,
                destination = destination,
                context = requireContext(),
                travelMode = TravelMode.DRIVING,
                color = requireContext().getColorCompat(R.color.red), // Set the color of the route line
                estimates = { leg ->
                    // Handle successful drawing of route and display estimations
                    println("$TAG: drawRoute::estimates ${leg.duration}")
                },
                error = { throwable ->
                    // Handle any errors that occurred during the route drawing
                    println("$TAG: drawRoute::error ${throwable.message}")
                }
            )
        }
    }

    companion object {
        // Tag for logging and debugging purposes
        private val TAG = RouteFragment::class.java.simpleName
    }
}
