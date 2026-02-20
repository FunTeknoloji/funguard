package com.funguard.app

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import android.util.Log

class SmsReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Telephony.Sms.Intents.SMS_RECEIVED_ACTION) {
            val messages = Telephony.Sms.Intents.getMessagesFromIntent(intent)
            for (message in messages) {
                val body = message.messageBody
                val sender = message.displayOriginatingAddress

                Log.d("FunGuard", "SMS received from $sender: $body")

                // Extract links
                val links = extractLinks(body)
                for (link in links) {
                    if (UrlScanner.isMalicious(link)) {
                        showWarning(context, "Şüpheli SMS Bağlantısı: $link")
                    }
                }

                // Content scanning (simple AI placeholder)
                if (body.contains("kazandınız", ignoreCase = true) || body.contains("tıklayın", ignoreCase = true)) {
                     showWarning(context, "Dolandırıcılık Girişimi Olabilir: $body")
                }
            }
        }
    }

    private fun extractLinks(text: String): List<String> {
        val regex = "(https?://[\\\\w-]+(\\\\.[\\\\w-]+)+(/[\\\\w- ./?%&=]*)?)".toRegex()
        return regex.findAll(text).map { it.value }.toList()
    }

    private fun showWarning(context: Context, message: String) {
        val intent = Intent(context, OverlayService::class.java).apply {
            putExtra("message", message)
        }
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            context.startForegroundService(intent)
        } else {
            context.startService(intent)
        }
    }
}
