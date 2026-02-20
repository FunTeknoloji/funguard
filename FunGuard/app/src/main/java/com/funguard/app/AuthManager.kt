package com.funguard.app

import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException

object AuthManager {
    private const val SUPABASE_URL = "https://eiecuiberhqmyvvlrakn.supabase.co"
    private const val SUPABASE_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImVpZWN1aWJlcmhxbXl2dmxyYWtuIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NzEyNjEzNDcsImV4cCI6MjA4NjgzNzM0N30.fq7MTsxB86XfZzfkRXS9avf7XK-kAsDAqms6WI84qbM"

    private val client = OkHttpClient()
    private val jsonMediaType = "application/json; charset=utf-8".toMediaType()

    fun signUp(email: String, pass: String, callback: (Boolean, String?) -> Unit) {
        val json = JSONObject().apply {
            put("email", email)
            put("password", pass)
        }

        val request = Request.Builder()
            .url("$SUPABASE_URL/auth/v1/signup")
            .addHeader("apikey", SUPABASE_KEY)
            .post(json.toString().toRequestBody(jsonMediaType))
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback(false, e.message)
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    callback(true, null)
                } else {
                    val errorBody = response.body?.string()
                    val message = JSONObject(errorBody ?: "{}").optString("msg", "Kayıt hatası")
                    callback(false, message)
                }
            }
        })
    }

    fun signIn(email: String, pass: String, callback: (Boolean, String?) -> Unit) {
        val json = JSONObject().apply {
            put("email", email)
            put("password", pass)
        }

        val request = Request.Builder()
            .url("$SUPABASE_URL/auth/v1/token?grant_type=password")
            .addHeader("apikey", SUPABASE_KEY)
            .post(json.toString().toRequestBody(jsonMediaType))
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback(false, e.message)
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    callback(true, null)
                } else {
                    val errorBody = response.body?.string()
                    val message = JSONObject(errorBody ?: "{}").optString("error_description", "Giriş hatası")
                    callback(false, message)
                }
            }
        })
    }
}
