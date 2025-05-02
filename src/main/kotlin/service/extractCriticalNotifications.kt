package service

import com.example.models.request.Notification
import kotlinx.serialization.json.JsonObject
import kotlin.collections.*

fun extractCriticalNotifications(payload: JsonObject): List<Notification> {
    val stats = extractNotificationStats(payload)
    return stats.critical
}
