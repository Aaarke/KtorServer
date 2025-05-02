package models.response

import com.example.models.request.Message
import kotlinx.serialization.Serializable

@Serializable
data class Choice(val message: Message)
