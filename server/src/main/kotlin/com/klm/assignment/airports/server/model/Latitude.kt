package com.klm.assignment.airports.server.model

data class Latitude(val degrees: Int, val minutes: Int, val seconds: Int, val direction: LatitudeDirection) {
    enum class LatitudeDirection {
        N, S
    }
}
