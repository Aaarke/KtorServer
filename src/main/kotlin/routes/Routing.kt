package com.example.routes

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.request.receive
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.JsonObject
import service.extractCriticalNotifications
import service.extractNotificationStats
import service.summarizeNotificationsWithOpenAI

fun Application.configureRouting() {
    routing {
        post("/summarize") {
            try {
                val payload = call.receive<JsonObject>()
                val stats = extractNotificationStats(payload)
                val summary = summarizeNotificationsWithOpenAI(stats)
                call.respond(HttpStatusCode.OK, mapOf("summary" to summary))
            } catch (e: Exception) {
                call.application.environment.log.error("Failed to summarize notifications", e)
                call.respond(HttpStatusCode.InternalServerError, mapOf("error" to (e.message ?: "Unknown error")))
            }
        }
    }
}
