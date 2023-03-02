package com.example.model

data class Card(
    val id: String,
    val status: String,
    val recipientId: String,
    val cardholderId: String,
    val displayName: String,
    val expires: String,
    val currency: String,
    val limitCents: Int?,
    val spentCents: Int?,
    val lifetimeSpentCents: Int?,
    val last4: String,
    val validFrom: String,
    val validTo: String,
    val creditCardId: String? = null,
    val notes: String? = null,
    val minTransactionCents: Int? = null,
    val maxTransactionCents: Int? = null,
    val maxTransactionCounts: Int? = null,
    val companyName: String? = null,
    val issuer: String? = null
)
