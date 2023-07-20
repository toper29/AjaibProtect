package com.example.ajaibprotect

import android.os.Bundle
import android.support.v4.text.HtmlCompat
import android.support.v4.widget.TextViewCompat
import android.support.v7.app.AppCompatActivity
import android.widget.ImageButton
import android.widget.TextView

class InfoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)

        val textView = findViewById<TextView>(R.id.textView)
        textView.text = """
            AjaibShield - Aplikasi Perlindungan Keamanan 
            dari Lab bernama Ajaib
            Versi 1.0
            
            Aplikasi Deteksi Malware adalah solusi anda untuk melindungi perangkat Android Anda dari ancaman malware yang merusak. Dikembangkan dengan teknologi terdepan, aplikasi ini memberikan keamanan tingkat lanjut untuk menjaga privasi dan kinerja perangkat Anda tetap optimal.
            Fitur Utama:
            1.  Pendeteksian Malware yang Canggih:
            Aplikasi ini menggunakan algoritma canggih untuk mendeteksi dan menghapus berbagai jenis malware yang mengancam perangkat Anda.
            
            2. Pemindaian Cepat dan Mendalam: 
            Lakukan pemindaian cepat atau mendalam untuk menemukan dan menghapus malware dengan efektif.
            
            3. Proteksi Aktif: 
            Aplikasi kami memberikan perlindungan 24/7 dengan pembaruan otomatis dan pemantauan yang terus-menerus.
            
            4. Blokir Aplikasi Berbahaya: 
            Identifikasi dan blokir aplikasi berbahaya sebelum mereka dapat merusak perangkat Anda.
            
            5. Peningkatan Kinerja: 
            Selain deteksi malware, aplikasi ini juga membantu meningkatkan kinerja perangkat dengan membersihkan file cache yang tidak perlu dan mengoptimalkan penggunaan memori.
            </body></html>
        """.trimIndent()

        TextViewCompat.setAutoSizeTextTypeUniformWithConfiguration(
            textView,
            12,
            30,
            2,
            TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM
        )

        val imageButtonBack = findViewById<ImageButton>(R.id.imageButtonback)
        imageButtonBack.setOnClickListener {
            finish() // Fungsi untuk menutup activity
        }
    }
}
