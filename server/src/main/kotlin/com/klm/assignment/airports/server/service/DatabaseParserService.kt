package com.klm.assignment.airports.server.service

import com.klm.assignment.airports.server.model.*
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.Resource
import org.springframework.stereotype.Service

@Service
class DatabaseParserService(
    @Value("classpath:GlobalAirportDatabase.txt")
    private val resourceFile: Resource
) {
    companion object {
        private val log = LoggerFactory.getLogger(DatabaseParserService::class.java)
    }

    class ParseException(message: String) : Exception(message)

    enum class Columns(private val position: Int, private val nullValue: String? = null) {
        ICAO(0),
        IATA(1, "N/A"),
        NAME(2, "N/A"),
        CITY(3, "N/A"),
        COUNTRY(4, "N/A"),
        LAT_DEGREES(5, "000"),
        LAT_MINUTES(6, "000"),
        LAT_SECONDS(7, "000"),
        LAT_DIRECTION(8, "U"),
        LONG_DEGREES(9, "000"),
        LONG_MINUTES(10, "000"),
        LONG_SECONDS(11, "000"),
        LONG_DIRECTION(12, "U"),
        ALTITUDE(13, "00000");

        fun isNull(line: List<String>): Boolean = line[this.position] == this.nullValue
        fun readAsString(line: List<String>): String? = if (isNull(line)) null else line[this.position]
        fun readAsRequiredString(line: List<String>): String =
            this.readAsString(line) ?: throw ParseException("Null not expected")

        fun readAsInt(line: List<String>): Int? = this.readAsString(line)?.toInt(10)
        fun readAsRequiredInt(line: List<String>): Int = this.readAsRequiredString(line).toInt(10)
    }

    fun retrieve(): List<Airport> {
        log.info("Reading in airport database")
        return this.resourceFile.getContentAsString(Charsets.UTF_8)
            .split("\n")
            .filter { it.trim().isNotBlank() }
            .map { it.split(":") }
            .filter { it.size == 16 && !Columns.NAME.isNull(it) }
            .map { readAirport(it) }
    }

    fun readAirport(line: List<String>): Airport {
        return Airport(
            icao = Columns.ICAO.readAsRequiredString(line),
            iata = Columns.IATA.readAsString(line),
            name = Columns.NAME.readAsRequiredString(line),
            location = readLocation(line),
        )
    }

    fun readLocation(line: List<String>): Location? {
        val coordinates = readCoordinates(line)
        return if (coordinates != null && !Columns.COUNTRY.isNull(line) && !Columns.CITY.isNull(line)) Location(
            country = Columns.COUNTRY.readAsRequiredString(line),
            city = Columns.CITY.readAsRequiredString(line),
            coordinates = coordinates,
            altitude = Columns.ALTITUDE.readAsInt(line)
        ) else null
    }

    fun readCoordinates(line: List<String>): Coordinates? {
        return if (
            !Columns.LAT_DEGREES.isNull(line)
            && !Columns.LAT_MINUTES.isNull(line)
            && !Columns.LAT_SECONDS.isNull(line)
            && !Columns.LAT_DIRECTION.isNull(line)
            && !Columns.LONG_DEGREES.isNull(line)
            && !Columns.LONG_MINUTES.isNull(line)
            && !Columns.LONG_SECONDS.isNull(line)
            && !Columns.LONG_DIRECTION.isNull(line)
        ) Coordinates(
            latitude = Latitude(
                degrees = Columns.LAT_DEGREES.readAsRequiredInt(line),
                minutes = Columns.LAT_MINUTES.readAsRequiredInt(line),
                seconds = Columns.LONG_SECONDS.readAsRequiredInt(line),
                direction = when (Columns.LAT_DIRECTION.readAsRequiredString(line)) {
                    "N" -> Latitude.LatitudeDirection.N
                    "S" -> Latitude.LatitudeDirection.S
                    else -> throw ParseException("Invalid direction")
                }
            ),
            longitude = Longitude(
                degrees = Columns.LONG_DEGREES.readAsRequiredInt(line),
                minutes = Columns.LONG_MINUTES.readAsRequiredInt(line),
                seconds = Columns.LONG_SECONDS.readAsRequiredInt(line),
                direction = when (Columns.LONG_DIRECTION.readAsRequiredString(line)) {
                    "E" -> Longitude.LongitudeDirection.E
                    "W" -> Longitude.LongitudeDirection.W
                    else -> throw ParseException("Invalid direction")
                }
            )
        ) else null
    }

}
