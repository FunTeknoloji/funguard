package com.funguard.app

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val etEmail = findViewById<EditText>(R.id.et_email_reg)
        val etPassword = findViewById<EditText>(R.id.et_password_reg)
        val btnRegister = findViewById<Button>(R.id.btn_register)
        val tvGoToLogin = findViewById<TextView>(R.id.tv_go_to_login)
        val pbLoading = findViewById<ProgressBar>(R.id.pb_loading_reg)

        btnRegister.setOnClickListener {
            val email = etEmail.text.toString()
            val pass = etPassword.text.toString()

            if (email.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Lütfen tüm alanları doldurun", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            pbLoading.visibility = View.VISIBLE
            AuthManager.signUp(email, pass) { success, error ->
                runOnUiThread {
                    pbLoading.visibility = View.GONE
                    if (success) {
                        Toast.makeText(this, "Kayıt başarılı, e-postayı onaylayın", Toast.LENGTH_LONG).show()
                        finish() // Go back to login
                    } else {
                        Toast.makeText(this, "Hata: $error", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }

        tvGoToLogin.setOnClickListener {
            finish()
        }
    }
}
