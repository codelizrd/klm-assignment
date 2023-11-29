package com.klm.assignment.airports.server.controller

import com.klm.assignment.airports.server.config.CorrelationIdFilter
import com.klm.assignment.airports.server.model.Airport
import com.klm.assignment.airports.server.service.AirportsServerService
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import jakarta.validation.Valid
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.Pattern
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.client.HttpClientErrorException
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono

@RestController
@RequestMapping("/api")
class AirportsServerController(private val airportsServerService: AirportsServerService) {

    companion object {
        private val log = LoggerFactory.getLogger(AirportsServerController::class.java)
    }

    @GetMapping("/search")
    @ResponseStatus(code = HttpStatus.OK)
    @ApiResponses(value = [ApiResponse(responseCode = "200", description = "Airports with comply with search string")])
    fun search(
        @Valid @Pattern(regexp = "^[A-Z]+$") query: String,
        @Min(0) @Valid @RequestParam(value = "page", required = false, defaultValue = "0") page: Int,
        @Min(1) @Max(10) @Valid @RequestParam(value = "size", required = false, defaultValue = "5") size: Int,
        @RequestHeader(name = CorrelationIdFilter.HEADER_NAME, required = false) correlationId: String?,
    ): Mono<Page<Airport>> {
        log.info("Searching for {} (page {} with size {})", query, page, size)
        return this.airportsServerService.search(query, Pageable.ofSize(size).withPage(page)).toMono()
    }

    @GetMapping("/{code}")
    @ResponseStatus(code = HttpStatus.OK)
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Airport with given code"),
            ApiResponse(responseCode = "404", description = "Airport not found")
        ]
    )
    fun getById(
        @Valid @Pattern(regexp = "^[A-Z]{3,4}$") @PathVariable("code") code: String,
        @Min(0) @Valid @RequestParam(value = "page", required = false, defaultValue = "0") page: Int,
        @Min(1) @Max(10) @Valid @RequestParam(value = "size", required = false, defaultValue = "5") size: Int,
        @RequestHeader(name = CorrelationIdFilter.HEADER_NAME, required = false) correlationId: String?,
    ): Mono<Airport> {
        log.info("Retrieving airport {}", code)
        return when (code.length) {
            4 -> this.airportsServerService.getByIcao(code).toMono()
            3 -> this.airportsServerService.getByIata(code).toMono()
            else -> throw HttpClientErrorException(HttpStatus.CONFLICT, "Invalid code")
        }
    }

}
