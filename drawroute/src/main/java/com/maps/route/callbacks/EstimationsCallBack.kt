package com.maps.route.callbacks

import com.maps.route.model.Legs

@Deprecated("Use lambda function instead of interface")
interface EstimationsCallBack{
    @Deprecated("Use lambda function instead of routeEstimations to get the ETA")
    fun routeEstimations(legs: Legs?)
}