package com.example.network

import io.ktor.client.*

interface HttpService {
    fun getClient() : HttpClient
}