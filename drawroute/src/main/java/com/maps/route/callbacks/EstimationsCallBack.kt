package com.maps.route.callbacks

import com.maps.route.model.Legs

interface EstimationsCallBack{
    fun estimatedTimeOfArrival(legs: Legs?)
}