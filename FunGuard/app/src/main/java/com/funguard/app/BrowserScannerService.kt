package com.funguard.app

import android.accessibilityservice.AccessibilityService
import android.content.Intent
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo

class BrowserScannerService : AccessibilityService() {

    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        val rootNode = rootInActiveWindow ?: return
        scanForUrls(rootNode)
    }

    private fun scanForUrls(node: AccessibilityNodeInfo) {
        val text = node.text?.toString() ?: ""
        if (text.isNotBlank() && (text.contains("http") || text.contains(".com") || text.contains(".net") || text.contains(".org") || text.contains(".tr"))) {
             if (UrlScanner.isMalicious(text)) {
                 triggerAlert(text)
             }
        }

        for (i in 0 until node.childCount) {
            val child = node.getChild(i)
            if (child != null) {
                scanForUrls(child)
            }
        }
    }

    private fun triggerAlert(url: String) {
        val intent = Intent(this, OverlayService::class.java).apply {
            putExtra("message", "Şüpheli Web Sitesi!\n$url")
        }
        startService(intent)
    }

    override fun onInterrupt() {}
}
