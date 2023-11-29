package com.klm.assignment.airports.server.service

import com.klm.assignment.airports.server.model.Airport
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.client.HttpClientErrorException
import kotlin.random.Random

@Service
class AirportsServerService(databaseParserService: DatabaseParserService) {
    companion object {
        private val log = LoggerFactory.getLogger(AirportsServerService::class.java)
    }

    private val airports = databaseParserService.retrieve()

    fun search(text: String, pageable: Pageable): Page<Airport> {
        log.info("Searching airports using {}", text)
        val filtered = this.airports.filter { it.matches(text) }

        return this.accessDatabase(1000 + filtered.size * 5, 2500 + filtered.size * 10) {
            log.info("Found a total of {} airports", filtered.size)
            PageImpl(
                filtered.subList(
                    pageable.offset.toInt(),
                    (pageable.offset.toInt() + pageable.pageSize).coerceAtMost(filtered.size)
                ),
                pageable,
                filtered.size.toLong()
            )
        }
    }

    fun getByIata(iataCode: String): Airport {
        log.info("Fetching by iataCode = {}", iataCode)
        return this.accessDatabase {
            this.airports.find { it.iata == iataCode } ?: throw HttpClientErrorException(
                HttpStatus.NOT_FOUND,
                "Airport not found"
            )
        }
    }

    fun getByIcao(icaoCode: String): Airport {
        log.info("Fetching by icaoCode = {}", icaoCode)
        return this.accessDatabase {
            this.airports.find { it.icao == icaoCode } ?: throw HttpClientErrorException(
                HttpStatus.NOT_FOUND,
                "Airport not found"
            )
        }
    }

    private fun Airport.matches(text: String): Boolean {
        return this.name.startsWith(text) || this.location?.city?.contains(text) == true
    }

    private fun <T> accessDatabase(waitMin: Int = 1000, waitMax: Int = 2000, execute: () -> T): T {
        val itTakes = Random.nextInt(waitMin, waitMax).toLong()
        log.info("Accessing database")
        Thread.sleep(itTakes)
        val result = execute()
        log.info("Accessing database took {}msec", itTakes)
        return result
    }

}
