package com.example.network

import com.beust.klaxon.Klaxon
import com.example.model.RefreshTokenResponse
import de.sharpmind.ktor.EnvConfig
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.util.*

val storage = mutableListOf<BearerTokens>()

class HttpServiceImpl : HttpService {
    override fun getClient(): HttpClient {
        return HttpClient(CIO) {
            expectSuccess = true
            install(ContentNegotiation) {}
            install(DefaultRequest) {
                headers.appendIfNameAbsent(HttpHeaders.ContentType, "application/json")
                headers.appendIfNameAbsent(HttpHeaders.Accept, API_VERSION)
            }
            install(Auth) {
                bearer {
                    loadTokens {
                        storage.last()
                    }

                    refreshTokens {
                        val client = HttpClient(CIO) {
                            install(DefaultRequest) {
                                headers.appendIfNameAbsent(HttpHeaders.ContentType, "application/json")
                                headers.appendIfNameAbsent(HttpHeaders.Accept, API_VERSION)
                            }
                        }
                        // Re-authenticate by signing in again since Auth Endpoint doesn't seem to work
                        val response: String = client.post(SIGN_IN_ENDPOINT) {
                            contentType(ContentType.Application.Json)
                            setBody(
                                "{\n" +
                                        "    \"email\" : \"${EnvConfig.getString("username")}\",\n" +
                                        "    \"password\" : \"${EnvConfig.getString("password")}\"\n" +
                                        "}"
                            )
                            markAsRefreshTokenRequest()
                        }.body()
                        val creds = Klaxon().parse<RefreshTokenResponse>(response)!!
                        creds.token?.let { creds.refreshToken?.let { it1 -> BearerTokens(it, it1) } }
                            ?.let { storage.add(it) }
                        storage.last()
                    }
                }
            }
        }
    }

    companion object {
        private const val API_VERSION = "application/vnd.paywithextend.v2021-03-12+json"
        private const val RENEW_ENDPOINT = "https://api.paywithextend.com/renewauth"
        private const val SIGN_IN_ENDPOINT = "https://api.paywithextend.com/signin"
    }
}