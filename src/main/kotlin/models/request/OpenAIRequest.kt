package com.example.models.request

import kotlinx.serialization.Serializable

@Serializable
data class OpenAIRequest(
    val model: String = "gpt-4",
    val messages: List<Message>,
    val temperature: Double = 0.5
)