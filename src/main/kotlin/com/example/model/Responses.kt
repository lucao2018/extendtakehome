package com.example.model

data class GetCardsResponse(val pagination: Pagination? = null, val virtualCards: List<Card>)

data class RefreshTokenResponse(val user: User, val token: String? = null, val refreshToken: String? = null)

data class GetTransactionsResponse(val transactions: List<Transaction>)

data class Pagination(
    val page: Int? = null,
    val pageItemCount: Int? = null,
    val totalItems: Int? = null,
    val numberOfPages: Int? = null
)