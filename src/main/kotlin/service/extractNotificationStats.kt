package service

import com.example.models.request.Notification
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.intOrNull
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import models.request.NotificationStats

fun extractNotificationStats(payload: JsonObject): NotificationStats {
    val threads = payload["payload"]?.jsonObject?.get("notificationThreads")?.jsonArray
        ?: return NotificationStats(0, 0, 0, emptyList())

    val notifications = mutableListOf<Notification>()
    var total = 0
    var unread = 0

    threads.forEach { item ->
        val obj = item.jsonObject
        val title = obj["threadTitle"]?.jsonPrimitive?.contentOrNull ?: return@forEach
        val body = obj["latestMessage"]?.jsonPrimitive?.contentOrNull ?: return@forEach
        val timestamp = obj["latestAt"]?.jsonPrimitive?.contentOrNull ?: return@forEach
        val unreadCount = obj["unreadCount"]?.jsonPrimitive?.intOrNull ?: 0

        total++
        if (unreadCount > 0) unread++

        if (title.contains("System", ignoreCase = true) ||
            body.contains("login", ignoreCase = true) ||
            unreadCount >= 10) {
            notifications.add(Notification(title, body, timestamp))
        }
    }

    return NotificationStats(
        total = total,
        read = total - unread,
        unread = unread,
        critical = notifications.sortedByDescending { it.timestamp }.take(3)
    )
}


