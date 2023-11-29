package com.klm.assignment.airports.server.config

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Contact
import io.swagger.v3.oas.annotations.info.Info
import io.swagger.v3.oas.annotations.servers.Server
import org.springframework.context.annotation.Configuration

@OpenAPIDefinition(
    info = Info(
        title = "KLM Airport API",
        description = "Search airports by code or by name",
        contact = Contact(
            name = "KLM",
            url = "https://www.klm.com",
        ),
    ),
    servers = [Server(url = "http://localhost:8000")]
)
@Configuration
class AirportsServiceConfig
