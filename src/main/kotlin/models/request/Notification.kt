package com.example.models.request

import kotlinx.serialization.Serializable

@Serializable
data class Notification(val title: String, val body: String, val timestamp: String)