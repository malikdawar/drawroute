/*

Copyright 2020 Malik Dawar

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

 */
package com.maps.route

import com.google.android.gms.maps.model.LatLng
import com.maps.route.model.TravelMode
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request
import rx.Observable
import rx.functions.Func0
import rx.schedulers.Schedulers
import rx.util.async.Async
import java.io.IOException
import java.util.*

class RouteRest : RouteApi {
    private var client: OkHttpClient = OkHttpClient()

    override fun getJsonDirections(
        start: LatLng,
        end: LatLng,
        mode: TravelMode,
        apiKey: String
    ): Observable<String?>? {
        val resultFunc = Func0 label@{
            try {
                return@label getJSONDirection(start, end, mode, apiKey)
            } catch (e: IOException) {
                e.printStackTrace()
            }
            null
        }
        return Async.start(resultFunc, Schedulers.io())
    }

    @Throws(IOException::class)
    private fun getJSONDirection(
        start: LatLng,
        end: LatLng,
        mode: TravelMode,
        apiKey: String
    ): String {
        val url = ("https://maps.googleapis.com/maps/api/directions/json?"
                + "origin="
                + start.latitude + ","
                + start.longitude
                + "&destination="
                + end.latitude + ","
                + end.longitude
                + "&sensor=false&units=metric&mode="
                + mode.name.toLowerCase(Locale.US)
                + "&key="
                + apiKey)
        val request = Request.Builder()
            .url(url)
            .build()
        val response = client.newCall(request).execute()
        return response.body().string()
    }
}