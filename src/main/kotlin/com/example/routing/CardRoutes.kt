package com.example.routing

import com.example.network.storage
import com.example.service.CardService
import io.ktor.client.plugins.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.cardRouting() {
    val service by inject<CardService>()
    /**
     * Lists all the cards associated with the logged-in user
     */
    route("/api/v1/cards") {
        get {
            if (storage.isEmpty()) {
                return@get call.respondText(
                    "Please authentic at /api/v1/login first",
                    status = HttpStatusCode.Unauthorized
                )
            }
            val count = call.request.queryParameters["count"]
            val page = call.request.queryParameters["page"]
            val sortDirection = call.request.queryParameters["sortDirection"]
            val sortField = call.request.queryParameters["sortField"]
            val statuses = call.request.queryParameters["sortField"]
            try {
                val result = service.getCards(count, page, sortDirection, sortField, statuses)
                call.respond(result)
            } catch (e: RedirectResponseException) {
                call.application.environment.log.info("A redirect exception occurred when trying to call the Extend API: $e")
                call.respondText("The server failed to retrieve the cards", status = HttpStatusCode.InternalServerError)
            } catch (e: ClientRequestException) {
                call.application.environment.log.info("A client request exception occurred when trying to call the Extend API: $e")
                call.respondText("The server failed to retrieve the cards", status = HttpStatusCode.InternalServerError)
            } catch (e: ServerResponseException) {
                call.application.environment.log.info("A server response exception occurred when trying to call the Extend API: $e")
                call.respondText("The server failed to retrieve the cards", status = HttpStatusCode.InternalServerError)
            } catch (e: Exception) {
                call.application.environment.log.info("An exception occurred while trying to retrieve the cards: $e")
                call.respondText("The server failed to retrieve the cards", status = HttpStatusCode.InternalServerError)
            }
        }

        /**
         * Lists all the transactions associated with a card
         */
        get("{id?}/transactions") {
            if (storage.isEmpty()) {
                return@get call.respondText(
                    "Please authentic at /api/v1/login first",
                    status = HttpStatusCode.Unauthorized
                )
            }
            val id = call.parameters["id"] ?: return@get call.respondText(
                "No card id was provided",
                status = HttpStatusCode.BadRequest
            )
            val after = call.request.queryParameters["after"]
            val before = call.request.queryParameters["before"]
            val count = call.request.queryParameters["count"]
            val status = call.request.queryParameters["status"]
            try {
                val result = service.getCardTransactions(id, after, before, count, status)
                call.respond(result)
            } catch (e: RedirectResponseException) {
                call.application.environment.log.info("A redirect exception occurred when trying to call the Extend API: $e")
                call.respondText(
                    "Failed to retrieve transactions for card with $id",
                    status = HttpStatusCode.InternalServerError
                )
            } catch (e: ClientRequestException) {
                call.application.environment.log.info("A client request exception occurred when trying to call the Extend API: $e")
                call.respondText(
                    "Failed to retrieve transactions for card with $id",
                    status = HttpStatusCode.InternalServerError
                )
            } catch (e: ServerResponseException) {
                call.application.environment.log.info("A server response exception occurred when trying to call the Extend API: $e")
                call.respondText(
                    "Failed to retrieve transactions for card with $id",
                    status = HttpStatusCode.InternalServerError
                )
            } catch (e: Exception) {
                call.application.environment.log.info("An exception occurred while trying to retrieve transactions for card with $id: $e")
                call.respondText(
                    "Failed to retrieve transactions for card with $id",
                    status = HttpStatusCode.InternalServerError
                )
            }

        }

        /**
         * View the details of an individual transaction
         */
        get("{id?}/transactions/{transactionId?}") {
            if (storage.isEmpty()) {
                return@get call.respondText(
                    "Please authentic at /api/v1/login first",
                    status = HttpStatusCode.Unauthorized
                )
            }
            val transactionId = call.parameters["transactionId"] ?: return@get call.respond("No id")
            try {
                val result = service.getTransaction(transactionId)
                call.respond(result)
            } catch (e: RedirectResponseException) {
                call.application.environment.log.info("A redirect exception occurred when trying to call the Extend API: $e")
                call.respondText(
                    "Failed to retrieve information about transaction with id $transactionId",
                    status = HttpStatusCode.InternalServerError
                )
            } catch (e: ClientRequestException) {
                call.application.environment.log.info("A client request exception occurred when trying to call the Extend API: $e")
                call.respondText(
                    "Failed to retrieve information about transaction with id $transactionId",
                    status = HttpStatusCode.InternalServerError
                )
            } catch (e: ServerResponseException) {
                call.application.environment.log.info("A server response exception occurred when trying to call the Extend API: $e")
                call.respondText(
                    "Failed to retrieve information about transaction with id $transactionId",
                    status = HttpStatusCode.InternalServerError
                )
            } catch (e: Exception) {
                call.application.environment.log.info("An internal server error occurred: $e")
                call.respondText(
                    "Failed to retrieve information about transaction with id $transactionId",
                    status = HttpStatusCode.InternalServerError
                )
            }
        }
    }

}