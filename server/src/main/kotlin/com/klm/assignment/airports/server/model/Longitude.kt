package com.klm.assignment.airports.server.model

data class Longitude(val degrees: Int, val minutes: Int, val seconds: Int, val direction: LongitudeDirection) {
    enum class LongitudeDirection {
        E, W
    }
}
