package com.example.di

import com.example.network.HttpService
import com.example.network.HttpServiceImpl
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.bind
import org.koin.dsl.module
import com.example.service.CardService
import com.example.service.CardServiceImpl

val appModule = module {
    singleOf(::CardServiceImpl) { bind<CardService>() }
    singleOf(::HttpServiceImpl) { bind<HttpService>() }
}