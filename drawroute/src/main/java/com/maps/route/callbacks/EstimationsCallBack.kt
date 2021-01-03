package com.maps.route.callbacks

import com.maps.route.model.Legs

interface EstimationsCallBack{
    fun routeEstimations(legs: Legs?)
}