package com.maps.route.data

import com.google.android.gms.maps.model.LatLng
import com.maps.route.model.DirectionsResponse
import com.maps.route.model.TravelMode
import kotlinx.coroutines.flow.Flow

internal interface RepositoryDrawRoute {

    suspend fun fetchDirections(
        origin: LatLng,
        destination: LatLng,
        mode: TravelMode = TravelMode.DRIVING,
        apiKey: String,
    ): Flow<DataState<DirectionsResponse>>
}