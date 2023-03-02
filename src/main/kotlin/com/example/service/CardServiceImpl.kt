package com.example.service

import com.beust.klaxon.Klaxon
import com.example.model.Card
import com.example.model.GetCardsResponse
import com.example.model.GetTransactionsResponse
import com.example.model.Transaction
import com.example.network.HttpService
import io.ktor.client.call.*
import io.ktor.client.request.*

/**
 * Implements functionality for retrieving information related to cards from the Extend API
 */
class CardServiceImpl(
    private val httpService: HttpService
) : CardService {
    /**
     * Retrieves a list of all virtual cards belonging to a user. Supports all query parameters that the Extend API
     * supports
     * @param count Limit of items per page (maximum: 50)
     * @param page The page to start from
     * @param sortDirection Sort direction
     * @param sortField Field to sort by
     * @param statuses Status of the Virtual Cards
     * @return the list of cards
     */
    override suspend fun getCards(
        count: String?,
        page: String?,
        sortDirection: String?,
        sortField: String?,
        statuses: String?
    ): List<Card> {
        val response = httpService.getClient().get(
            BASE_CARDS_URL
        ) {
            url {
                if (count != null) {
                    parameters.append("count", count)
                }
                if (page != null) {
                    parameters.append("page", page)
                }
                if (sortDirection != null) {
                    parameters.append("sortDirection", sortDirection)
                }
                if (sortField != null) {
                    parameters.append("sortField", sortField)
                }
                if (statuses != null) {
                    parameters.append("statuses", statuses)
                }
            }
        }
        val responseBody: String = response.body()
        return Klaxon().parse<GetCardsResponse>(responseBody)!!.virtualCards
    }

    /**
     * Retrieves a list of all transactions associated with a virtual card. Supports all query parameters that
     * the Extend API supports
     * @param id ID of the virtual card to get transactions for
     * @param after Timestamp cursor used for pagination. If present, will return count results after this timestamp
     * @param before Timestamp cursor used for pagination. If present, will return count results before this timestamp
     * @param count Number of results to return. Defaults to 25. Maximum of 500.
     * @param status Comma-delimited list of Transaction statuses to filter by. Defaults to PENDING
     * @return the list of transactions
     */
    override suspend fun getCardTransactions(
        id: String,
        after: String?,
        before: String?,
        count: String?,
        status: String?
    ): List<Transaction> {
        val response = httpService.getClient().get(
            "$BASE_CARDS_URL/$id/transactions"
        ) {
            url {
                if (after != null) {
                    parameters.append("count", after)
                }
                if (before != null) {
                    parameters.append("page", before)
                }
                if (count != null) {
                    parameters.append("count", count.toString())
                }
                if (status != null) {
                    parameters.append("status", status)
                }
            }
        }
        val responseBody: String = response.body()
        return Klaxon().parse<GetTransactionsResponse>(responseBody)!!.transactions
    }

    /**
     * Retrieves the information for a specific transaction
     * @param id ID of the transaction to get information for
     *
     * @return The transaction associated with [id]
     */
    override suspend fun getTransaction(id: String): Transaction {
        val response = httpService.getClient().get(
            "$BASE_TRANSACTIONS_URL/$id"
        )
        val responseBody: String = response.body()
        return Klaxon().parse<Transaction>(responseBody)!!
    }

    companion object {
        private const val BASE_CARDS_URL = "https://api.paywithextend.com/virtualcards"
        private const val BASE_TRANSACTIONS_URL = "https://api.paywithextend.com/transactions"
    }
}