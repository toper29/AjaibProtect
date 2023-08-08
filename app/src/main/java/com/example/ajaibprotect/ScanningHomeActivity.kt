package com.example.ajaibprotect

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity

class ScanningHomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scanning_home)

        val imageButtonBack = findViewById<ImageButton>(R.id.imageButtonback)
        imageButtonBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        val buttonScanningOffline = findViewById<Button>(R.id.buttonScanningOffline)
        buttonScanningOffline.setOnClickListener {
            val intent = Intent(this, UserAppsHome::class.java)
            startActivity(intent)
        }

        val backCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Lakukan sesuatu di sini sebelum kembali
                onBackPressedDispatcher.onBackPressed()
            }
        }
        onBackPressedDispatcher.addCallback(this, backCallback)
    }
}
