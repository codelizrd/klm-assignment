package com.klm.assignment.airports.server.config

import org.slf4j.LoggerFactory
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebExceptionHandler
import reactor.core.publisher.Mono


@Component
@Order(-2)
class ExceptionHandler : WebExceptionHandler {
    companion object {
        private val log = LoggerFactory.getLogger(ExceptionHandler::class.java)
    }


    override fun handle(exchange: ServerWebExchange, ex: Throwable): Mono<Void> {
        if (ex is HttpClientErrorException) {
            log.warn("Encountered anticipated exception", ex)
            exchange.response.setStatusCode(ex.statusCode)
            return exchange.response.setComplete()
        } else {
            log.error("Encountered unanticipated exception", ex)
        }
        return Mono.error(ex)
    }
}
