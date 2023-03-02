package com.example.routing

import com.beust.klaxon.Klaxon
import com.example.model.LoginInformation
import com.example.model.RefreshTokenResponse
import com.example.network.storage
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.auth.providers.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

private const val SIGN_IN_ENDPOINT = "https://api.paywithextend.com/signin"

fun Route.authRouting() {
    /**
     * Logs a user in to the Extend API with their credentials
     */
    route("/api/v1/login") {
        post {
            val client = HttpClient(CIO)
            val body = call.receiveText()
            val loginInformation: LoginInformation
            try {
                loginInformation = Klaxon().parse<LoginInformation>(body)!!
            } catch (e: Exception) {
                return@post call.respond(HttpStatusCode.BadRequest, "Email and password not provided")
            }
            val response: String = client.post(SIGN_IN_ENDPOINT) {
                headers {
                    append(HttpHeaders.ContentType, "application/json")
                    append(HttpHeaders.Accept, "application/vnd.paywithextend.v2021-03-12+json")
                }
                contentType(ContentType.Application.Json)
                setBody(
                    "{\"email\" : \"${loginInformation.email}\", \n \"password\" : \"${loginInformation.password}\"}"
                )
            }.body()
            val creds = Klaxon().parse<RefreshTokenResponse>(response)!!
            creds.token?.let { it1 -> creds.refreshToken?.let { it2 -> BearerTokens(it1, it2) } }
                ?.let { it2 -> storage.add(it2) }
            client.close()
            call.respondText("Successfully signed in")
        }
    }
}