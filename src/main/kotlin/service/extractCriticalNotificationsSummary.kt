package service

import kotlinx.serialization.json.JsonObject

fun extractCriticalNotificationsSummary(payload: JsonObject): String {
    val stats = extractNotificationStats(payload)
    val summaryHeader = "Total: ${stats.total}, Read: ${stats.read}, Unread: ${stats.unread}"
    val criticalSummary = stats.critical.joinToString("\n") {
        "- ${it.title}: ${it.body} (${it.timestamp})"
    }
    return "$summaryHeader\n\nTop Critical Notifications:\n$criticalSummary"
}