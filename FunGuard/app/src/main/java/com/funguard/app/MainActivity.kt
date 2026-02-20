package com.funguard.app

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupNavigation()
        requestPermissions()
        startScannerService()

        // Initial fragment
        showFragment("home")
    }

    private fun setupNavigation() {
        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> showFragment("home")
                R.id.nav_scan -> showFragment("scan")
                R.id.nav_settings -> showFragment("settings")
            }
            true
        }
    }

    private fun showFragment(type: String) {
        val fragmentLayout = when (type) {
            "home" -> R.layout.fragment_home
            "scan" -> R.layout.fragment_scan
            "settings" -> R.layout.fragment_settings
            else -> R.layout.fragment_home
        }

        val container = findViewById<FrameLayout>(R.id.fragment_container)
        val view = layoutInflater.inflate(fragmentLayout, null)
        container.removeAllViews()
        container.addView(view)

        // Handle specific fragment buttons/switches if needed
        if (type == "settings") {
            setupSettingsView(view)
        }
    }

    private fun setupSettingsView(view: View) {
        // Find switches and set listeners
        // This is a simplified version since we are using raw layouts
    }

    private fun requestPermissions() {
        val permissions = mutableListOf(
            Manifest.permission.RECEIVE_SMS,
            Manifest.permission.READ_SMS
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissions.add(Manifest.permission.POST_NOTIFICATIONS)
        }

        val toRequest = permissions.filter {
            ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }

        if (toRequest.isNotEmpty()) {
            ActivityCompat.requestPermissions(this, toRequest.toTypedArray(), 100)
        }

        // Overlay Permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
            val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:$packageName"))
            startActivity(intent)
        }

        // Battery Optimization
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val intent = Intent()
            val pm = getSystemService(POWER_SERVICE) as android.os.PowerManager
            if (!pm.isIgnoringBatteryOptimizations(packageName)) {
                intent.action = Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
                intent.data = Uri.parse("package:$packageName")
                // startActivity(intent) // Disabled for auto-build convenience, user should be prompted
            }
        }
    }

    private fun startScannerService() {
        val intent = Intent(this, ScannerService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent)
        } else {
            startService(intent)
        }

        UrlScanner.updateDatabase { success ->
            runOnUiThread {
                if (success) {
                    Toast.makeText(this, "USOM Veritabanı Güncellendi", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
