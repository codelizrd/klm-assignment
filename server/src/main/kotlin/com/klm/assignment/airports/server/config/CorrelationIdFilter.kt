package com.klm.assignment.airports.server.config

import org.slf4j.MDC
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono

@Component
class CorrelationIdFilter : WebFilter {
    companion object {
        const val HEADER_NAME = "X-Correlation-ID"
        const val CONTEXT_KEY = "CorrelationId"
    }

    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
        val headers = exchange.request.headers.toSingleValueMap()
        val traceId = if (headers.containsKey(HEADER_NAME)) headers[HEADER_NAME] else "None"
        MDC.put(CONTEXT_KEY, traceId)
        return chain.filter(exchange)
    }
}
