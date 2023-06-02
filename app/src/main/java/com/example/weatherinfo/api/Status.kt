package com.example.weatherinfo.api

/**
 * Provides Response status.
 */
enum class Status {
    /**
     * Waiting for response.
     */
    Waiting,

    /**
     * Obtained response successfully.
     */
    SUCCESS,

    /**
     * Failed to get response.
     */
    ERROR

}