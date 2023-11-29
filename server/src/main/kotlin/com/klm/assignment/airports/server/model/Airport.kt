package com.klm.assignment.airports.server.model

data class Airport(
    val icao: String,
    val iata: String?,
    val name: String,
    val location: Location?
)
