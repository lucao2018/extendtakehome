package com.example.service

import com.example.model.Card
import com.example.model.GetCardsResponse
import com.example.model.Transaction

interface CardService {
    suspend fun getCards(count : String?, page : String?, sortDirection : String?,
                         sortField : String?, statuses : String?) : List<Card>

    suspend fun getCardTransactions(id : String, after : String?, before : String?, count : String?, status : String?) : List<Transaction>

    suspend fun getTransaction(id : String) : Transaction
}