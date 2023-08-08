package com.example.ajaibprotect

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class Homeincidentwa : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_incident_wa)

        val buttonViewAplikasiWA = findViewById<Button>(R.id.buttonviewaplikasiwa)
        buttonViewAplikasiWA.setOnClickListener {
            val intent = Intent(this, ReceivedApksActivity::class.java)
            startActivity(intent)
        }
    }
}
