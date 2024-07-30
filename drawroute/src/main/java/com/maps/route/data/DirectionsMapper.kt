package com.maps.route.data

import com.google.gson.Gson
import com.maps.route.model.DirectionsResponse
import com.squareup.okhttp.ResponseBody

internal fun ResponseBody.toDirectionsResponse(): DirectionsResponse {
    return Gson().fromJson(toString(), DirectionsResponse::class.java)
}
