package com.example.ajaibprotect

import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private val duration: Long = 3000 // Durasi tampilan gambar (dalam milidetik)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Menjalankan timer untuk memunculkan gambar selama durasi yang ditentukan
        Handler().postDelayed({
            // Kode yang akan dijalankan setelah durasi selesai
            // Misalnya, navigasi ke layar berikutnya atau tindakan lainnya
        }, duration)
    }
}
