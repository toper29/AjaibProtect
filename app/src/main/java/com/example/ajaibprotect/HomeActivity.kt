package com.example.ajaibprotect

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
    }

    // Fungsi untuk membuka halaman TipsAndSupportActivity
    fun openTipsAndSupportActivity(view: View) {
        val intent = Intent(this, TipsAndSupportActivity::class.java)
        startActivity(intent)
    }
}
