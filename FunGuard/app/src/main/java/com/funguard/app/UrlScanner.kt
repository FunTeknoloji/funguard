package com.funguard.app

import okhttp3.*
import java.io.IOException

object UrlScanner {
    private val client = OkHttpClient()
    private var globalThreatList: List<String> = emptyList()

    fun updateDatabase(callback: (Boolean) -> Unit) {
        // Fetching global threat list (using USOM as source but hiding the name)
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
                    globalThreatList = body.lines().filter { it.isNotBlank() }
                    callback(true)
                } else {
                    callback(false)
                }
            }
        })
    }

    fun isMalicious(url: String): Boolean {
        if (url.isBlank()) return false
        val cleanUrl = url.replace("https://", "").replace("http://", "").split("/")[0].trim()

        return globalThreatList.any { it.trim().equals(cleanUrl, ignoreCase = true) || cleanUrl.contains(it.trim(), ignoreCase = true) } || checkAiScan(url)
    }

    private fun checkAiScan(url: String): Boolean {
        val suspiciousKeywords = listOf("sahte", "login", "banka", "hediye", "kazan", "phish", "verify", "account-update")
        return suspiciousKeywords.any { url.contains(it, ignoreCase = true) }
    }
}
