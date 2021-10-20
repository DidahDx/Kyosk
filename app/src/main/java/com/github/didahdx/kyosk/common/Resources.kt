package com.github.didahdx.kyosk.common

/**
 * Created by Daniel Didah on 10/20/21.
 */
sealed class Resources<T>(val data: T? = null, val message: String? = null) {
    class Success<T>(data: T) : Resources<T>(data)
    class Error<T>(message: String, data: T? = null) : Resources<T>(data, message)
    class Loading<T>(data: T? = null) : Resources<T>(data)
}