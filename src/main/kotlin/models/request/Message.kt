package com.example.models.request

import kotlinx.serialization.Serializable

@Serializable
data class Message(val role: String, val content: String)