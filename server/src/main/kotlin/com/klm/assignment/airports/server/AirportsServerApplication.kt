package com.klm.assignment.airports.server

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class AirportsServerApplication

fun main(args: Array<String>) {
    runApplication<AirportsServerApplication>(*args)
}
