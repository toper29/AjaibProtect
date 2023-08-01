package com.example.ajaibprotect

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View

class Homeincidentwa : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_incident_wa)
    }

    /// Fungsi untuk membuka InfoActivity
    fun openInfoActivity(view: View) {
        val intent = Intent(this, InfoActivity::class.java)
        startActivity(intent)
    }
}