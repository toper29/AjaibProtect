package com.example.ajaibprotect

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.widget.ImageView
import com.example.ajaibprotect.R

class MainActivity : AppCompatActivity() {
    private val delayMillis: Long = 5000 // Waktu penundaan dalam milidetik

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val imageView = findViewById<ImageView>(R.id.imageView)

        // Set gambar di ImageView
        imageView.setImageResource(R.drawable.tampilanawal) // Ganti "your_image" dengan nama gambar yang ingin ditampilkan

        // Membuat objek Handler
        val handler = Handler()

        // Membuat objek Runnable
        val runnable = Runnable {
            // Memindahkan pengguna ke halaman selanjutnya setelah waktu penundaan
            val intent = Intent(this@MainActivity, HomeActivity::class.java)
            startActivity(intent)
            finish() // Menutup MainActivity agar tidak dapat kembali ke halaman ini
        }

        // Memulai penundaan menggunakan Handler
        handler.postDelayed(runnable, delayMillis)
    }
}

