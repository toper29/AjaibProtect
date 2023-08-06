package com.example.ajaibprotect

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.ImageButton

class ScanningHomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scanning_home)

        val imageButtonBack = findViewById<ImageButton>(R.id.imageButtonback)
        imageButtonBack.setOnClickListener {
            onBackPressed() // Memanggil metode onBackPressed()
        }

        val buttonScanningOffline = findViewById<Button>(R.id.buttonScanningOffline)
        buttonScanningOffline.setOnClickListener {
            val intent = Intent(this, HomeScanningActivity::class.java)
            startActivity(intent)
        }
    }

}
