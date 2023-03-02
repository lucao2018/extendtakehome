package com.example

import com.example.di.appModule
import com.example.plugins.*
import de.sharpmind.ktor.EnvConfig
import io.ktor.server.application.*
import org.koin.ktor.plugin.Koin

fun main(args: Array<String>): Unit =
    io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.module() {
    configureSerialization()
    configureRouting()
    install(Koin) {
        modules(appModule)
    }
    EnvConfig.initConfig(environment.config)
}
