package com.maps.route.data

// Result wrapper for handling success and failure cases
internal sealed class DataState<out T> {
    data class Success<out T>(val data: T) : DataState<T>()
}