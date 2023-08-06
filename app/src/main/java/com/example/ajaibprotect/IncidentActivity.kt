package com.example.ajaibprotect

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.ImageButton

class IncidentActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_incident)

        val imageButtonBack = findViewById<ImageButton>(R.id.imageButtonback)
        imageButtonBack.setOnClickListener {
            finish() // Fungsi untuk menutup activity
        }

        val imageButtonIncidentWa = findViewById<ImageButton>(R.id.imageincidentwa)
        imageButtonIncidentWa.setOnClickListener {
            // Buka Homeincidentwa Activity
            val intent = Intent(this, Homeincidentwa::class.java)
            startActivity(intent)
        }
    }
}
