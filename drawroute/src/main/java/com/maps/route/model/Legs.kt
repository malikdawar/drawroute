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
package com.maps.route.model

import com.google.gson.annotations.SerializedName

class Legs {
    @SerializedName("start_address")
    var startAddress: String? = null
    @SerializedName("end_address")
    var endAddress: String? = null
    @SerializedName("start_location")
    var startLocation: GeoPoint? = null
    @SerializedName("end_location")
    var endLocation: GeoPoint? = null

    var steps: List<Step>? = null
    var distance: Distance? = null
    var duration: Duration? = null
}