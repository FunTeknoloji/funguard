package com.funguard.app

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class IntroActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val prefs = getSharedPreferences("FunGuardPrefs", Context.MODE_PRIVATE)
        if (prefs.getBoolean("intro_completed", false)) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        setContentView(R.layout.activity_intro)

        findViewById<Button>(R.id.btn_start).setOnClickListener {
            prefs.edit().putBoolean("intro_completed", true).apply()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}
