package models.request

import com.example.models.request.Notification
import kotlinx.serialization.Serializable

@Serializable
data class NotificationStats(
    val total: Int,
    val read: Int,
    val unread: Int,
    val critical: List<Notification>
)