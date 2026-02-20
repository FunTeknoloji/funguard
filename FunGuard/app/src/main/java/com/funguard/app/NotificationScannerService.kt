package com.funguard.app

import android.content.Intent
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log

class NotificationScannerService : NotificationListenerService() {

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        val packageName = sbn.packageName
        if (packageName == "com.funguard.app") return

        val extras = sbn.notification.extras
        val title = extras.getString("android.title") ?: ""
        val text = extras.getCharSequence("android.text")?.toString() ?: ""

        Log.d("FunGuard", "Notification from $packageName: $title - $text")

        val combinedText = "$title $text"
        val links = extractLinks(combinedText)

        for (link in links) {
            if (UrlScanner.isMalicious(link)) {
                triggerAlert("Bildirimde Şüpheli Bağlantı: $link")
            }
        }

        if (combinedText.contains("kazandınız", ignoreCase = true) ||
            combinedText.contains("ödül", ignoreCase = true) ||
            combinedText.contains("borcunuz", ignoreCase = true)) {
            triggerAlert("Şüpheli Bildirim İçeriği: $title")
        }
    }

    private fun extractLinks(text: String): List<String> {
        val regex = "(https?://[\\\\w-]+(\\\\.[\\\\w-]+)+(/[\\\\w- ./?%&=]*)?)".toRegex()
        return regex.findAll(text).map { it.value }.toList()
    }

    private fun triggerAlert(message: String) {
        val intent = Intent(this, OverlayService::class.java).apply {
            putExtra("message", message)
        }
        startService(intent)
    }
}
