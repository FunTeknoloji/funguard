package com.funguard.app

import okhttp3.*
import java.io.IOException

object UrlScanner {
    private val client = OkHttpClient()
    private var usomUrls: List<String> = emptyList()

    fun updateDatabase(callback: (Boolean) -> Unit) {
        val request = Request.Builder()
            .url("https://www.usom.gov.tr/url-list.txt")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback(false)
            }

            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()
                if (body != null) {
                    usomUrls = body.lines().filter { it.isNotBlank() }
                    callback(true)
                } else {
                    callback(false)
                }
            }
        })
    }

    fun isMalicious(url: String): Boolean {
        // AI Logic placeholder (would call user's AI link)
        // For now, check USOM list
        val cleanUrl = url.replace("https://", "").replace("http://", "").split("/")[0]
        return usomUrls.any { it.contains(cleanUrl, ignoreCase = true) } || checkAiScan(url)
    }

    private fun checkAiScan(url: String): Boolean {
        // This is where the AI Link integration would go.
        // Since no specific AI link was provided, we simulate a check.
        return url.contains("sahte", ignoreCase = true) || url.contains("phish", ignoreCase = true)
    }
}
