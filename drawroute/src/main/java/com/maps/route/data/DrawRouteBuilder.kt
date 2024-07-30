/*

Copyright 2024 Malik Dawar

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
package com.maps.route.data

import android.graphics.Color
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions
import com.maps.route.model.Route

internal class DrawRouteBuilder private constructor(builder: BuildRoute) {

    private val alpha: Float
    private val pathWidth: Int
    private val pathColor: Int
    private val bitmapDescriptor: BitmapDescriptor
    private val googleMap: GoogleMap

    companion object {
        private const val DEFAULT_MARKER_ALPHA = 1f
        private const val DEFAULT_PATH_WIDTH = 5
        private const val DEFAULT_PATH_COLOR = Color.RED
    }

    init {
        googleMap = builder.googleMap
        alpha = builder.alpha
        pathWidth = builder.pathWidth
        pathColor = builder.pathColor
        bitmapDescriptor = builder.bitmapDescriptor
    }

    fun drawPath(routes: List<Route>) {
        var polylineOptions: PolylineOptions?

        for (route in routes) {
            for (legs in route.legs!!) {
                polylineOptions = PolylineOptions()
                for (step in legs.steps!!) {
                    polylineOptions.add(
                        LatLng(
                            step?.startLocation?.lat!!,
                            step.startLocation.lng!!
                        )
                    )
                    polylineOptions.width(pathWidth.toFloat())
                    polylineOptions.color(pathColor)
                    googleMap.addPolyline(polylineOptions)
                }
            }
        }
    }

    class BuildRoute(internal val googleMap: GoogleMap) {
        internal var bitmapDescriptor: BitmapDescriptor
        internal var pathWidth: Int
        internal var pathColor: Int
        internal var alpha: Float

        fun withColor(pathColor: Int): BuildRoute {
            this.pathColor = pathColor
            return this
        }

        fun withWidth(pathWidth: Int): BuildRoute {
            this.pathWidth = pathWidth
            return this
        }

        fun withMarkerIcon(bitmapDescriptor: BitmapDescriptor): BuildRoute {
            this.bitmapDescriptor = bitmapDescriptor
            return this
        }

        fun withAlpha(alpha: Float): BuildRoute {
            this.alpha = alpha
            return this
        }

        fun build(): DrawRouteBuilder {
            return DrawRouteBuilder(this)
        }

        init {
            pathWidth = DEFAULT_PATH_WIDTH
            pathColor = DEFAULT_PATH_COLOR
            alpha = DEFAULT_MARKER_ALPHA
            bitmapDescriptor =
                BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)
        }
    }
}