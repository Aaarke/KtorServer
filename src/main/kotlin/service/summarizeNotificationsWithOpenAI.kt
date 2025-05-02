package service

import io.github.cdimascio.dotenv.dotenv
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.buildJsonArray
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import models.request.NotificationStats
import models.response.OpenAIResponse

suspend fun summarizeNotificationsWithOpenAI( stats: NotificationStats): String {
    val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
            })
        }
    }

//    val content = buildString {
//        append("Summarize the following notifications:\n\n")
//        stats.critical?.forEach {
//            append("- ${it.title}: ${it.body} at ${it.timestamp}\n")
//        }
//        append(stats.total)
//        append(stats.read)
//        append(stats.unread)
//    }

    val requestBody = buildJsonObject {
        put("model", "gpt-4")
        put("messages", buildJsonArray {
            add(buildJsonObject {
                put("role", "system")
                put("content", "You are a helpful assistant summarizing app notifications.")
            })
            add(buildJsonObject {
                put("role", "user")
                put("content", """
                Please summarize the following app notifications:

                $stats

                The summary should:
                - Highlight the top 3 critical notifications.
                - Clearly state the total number of notifications.
                - Include how many are read and how many are unread.
                - Present the summary in a concise, user-friendly format.
            """.trimIndent())
            })
        })
    }
    val dotenv = dotenv()
    val apiKey = dotenv["OPENAI_API_KEY"]

    try {
        val response: HttpResponse = client.post("https://api.openai.com/v1/chat/completions") {
            header(
                HttpHeaders.Authorization,
                "Bearer $apiKey"
            )  // ✅ Use this
            contentType(ContentType.Application.Json)
            setBody(requestBody)
        }


        if (!response.status.isSuccess()) {
            throw Exception("OpenAI API returned non-success status: ${response.status}")
        }

        val result = response.body<OpenAIResponse>()
        return result.choices.firstOrNull()?.message?.content ?: "No summary found"
    } catch (e: Exception) {
        throw RuntimeException("Error calling OpenAI API: ${e.message}", e)
    } finally {
        client.close()
    }


}