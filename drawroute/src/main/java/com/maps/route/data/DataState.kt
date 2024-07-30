package com.maps.route.data
/**
 * A sealed class representing the state of data in an operation, encapsulating success and failure outcomes.
 * It provides a type-safe way to manage results, ensuring that each result is either a success or a failure.
 *
 * @param T The type of data being handled in a successful operation.
 */
internal sealed class DataState<out T> {

    /**
     * Represents a successful outcome of an operation.
     *
     * @param T The type of data that the operation returns upon success.
     * @property data The successful result data returned by the operation.
     */
    data class Success<out T>(val data: T) : DataState<T>()
}
