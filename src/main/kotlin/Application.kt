package com.example

import com.example.routes.configureRouting
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.*
import io.ktor.server.engine.embeddedServer
import io.ktor.server.engine.sslConnector
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import java.io.File
import java.security.KeyStore

fun main(args: Array<String>) {
    embeddedServer(Netty, configure = {
        sslConnector(
            keyStore = KeyStore.getInstance("JKS").apply {
                val keystoreFile = File("keystore.jks")
                require(keystoreFile.exists()) { "Keystore file not found." }
                load(keystoreFile.inputStream(), "changeit".toCharArray())
            },
            keyAlias = "alias",
            keyStorePassword = { "changeit".toCharArray() },
            privateKeyPassword = { "changeit".toCharArray() }
        ) {
            port = 443
            host = "0.0.0.0"
        }

        connectionGroupSize = 2
        workerGroupSize = 5
        callGroupSize = 10
        shutdownGracePeriod = 2000
        shutdownTimeout = 3000
    }) {
        module()
    }.start(wait = true)
}

fun Application.module() {
    install(ContentNegotiation) {
        json()
    }
    configureRouting()
}
