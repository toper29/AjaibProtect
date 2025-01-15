package com.example.ajaibprotect

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.widget.TextViewCompat
class InfoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)

        val textView = findViewById<TextView>(R.id.textView)
        textView.text = """
            AjaibProtect - Solusi Keamanan untuk Android Anda
            Dari Lab Ajaib Universitas Katolik Atma Jaya
            Versi 1.0
            
            AjaibProtect adalah aplikasi deteksi malware canggih yang dirancang untuk melindungi perangkat Android Anda dari ancaman keamanan. Dengan teknologi mutakhir dan metode hybrid analysis, AjaibProtect memberikan perlindungan terbaik untuk menjaga privasi dan kinerja perangkat Anda tetap optimal.
            
            Fitur Utama:
            1.Pendeteksian Malware yang Akurat:
            Aplikasi ini memanfaatkan metode hybrid analysis (statis dan dinamis) untuk mendeteksi berbagai ancaman keamanan, termasuk malware tersembunyi.
            2.Analisis Menyeluruh dan Cepat:
            Lakukan pemindaian menyeluruh untuk mendeteksi izin mencurigakan, pola nama aplikasi yang aneh, hingga aplikasi berisiko tinggi berdasarkan pola penggunaan sumber daya.
            3.Pemantauan Aktif:
            Dengan fitur pemantauan izin, data, dan sumber daya aplikasi, AjaibProtect membantu Anda melakukan pemantauan perangkat secara berkala untuk menjaga keamanan dan melindungi data pribadi Anda.
            4.Identifikasi Aplikasi Berbahaya:
            Secara otomatis mendeteksi dan memberi peringatan tentang aplikasi yang berpotensi berbahaya, sehingga Anda dapat menghapusnya sebelum menimbulkan kerusakan.
            5.Efisiensi Perangkat:
            Selain perlindungan, AjaibProtect membantu meningkatkan kinerja perangkat Anda dengan mengidentifikasi aplikasi yang menggunakan sumber daya secara berlebihan.
            
            Catatan untuk Pengguna:
            Aplikasi bawaan sistem (system apps) sering kali memerlukan akses ke berbagai izin untuk menjalankan fungsi penting perangkat. Hal ini dapat menyebabkan skor risiko menjadi lebih

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
