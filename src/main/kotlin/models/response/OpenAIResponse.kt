package models.response

import kotlinx.serialization.Serializable

@Serializable
data class OpenAIResponse(val choices: List<Choice>)