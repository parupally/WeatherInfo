package com.example.weatherinfo.api

/**
 *
 * Gives the Response received in the form of status, data, and any error message if occurred.
 */
class Resource<T> private constructor(
    val status: Status,
    val data: T?,
    val errorMessage: String? = null
) {

    companion object {
        fun <T> success(data: T): Resource<T> {
            return Resource(
                Status.SUCCESS,
                data
            )
        }

        fun <T> error(errorMessage: String?): Resource<T?> {
            return Resource(
                Status.ERROR,
                null,
                errorMessage
            )
        }
    }
}