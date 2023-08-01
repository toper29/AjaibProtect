package com.example.ajaibprotect

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button

class Homeincidentwa : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_incident_wa)

        val buttonViewAplikasiWA = findViewById<Button>(R.id.buttonviewaplikasiwa)
        buttonViewAplikasiWA.setOnClickListener {
            val intent = Intent(this, Homeincidentwaapk::class.java)
            startActivity(intent)
        }
    }
}
