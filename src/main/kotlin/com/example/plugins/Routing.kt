package com.example.plugins

import com.example.routing.authRouting
import com.example.routing.cardRouting
import io.ktor.server.routing.*
import io.ktor.server.response.*
import io.ktor.server.application.*

fun Application.configureRouting() {
    routing {
        cardRouting()
        authRouting()
    }
}
