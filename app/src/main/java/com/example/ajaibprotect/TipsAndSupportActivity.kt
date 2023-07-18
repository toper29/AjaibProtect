package com.example.ajaibprotect

import android.os.Bundle
import android.support.v4.widget.TextViewCompat
import android.support.v7.app.AppCompatActivity
import android.widget.ImageButton
import android.widget.TextView
import com.example.ajaibprotect.R

class TipsAndSupportActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tips_and_support)

        val textView = findViewById<TextView>(R.id.textView)
        textView.text = """
            Tips tetap aman saat daring dan menghindar dari malware
            
            1. Gunakan antivirus yang bagus dan tetap up-to-date. 
            Sangat penting untuk menggunakan antivirus berkualitas baik dan selalu memperbaruinya agar tetap terdepan dalam ancaman dunia maya terbaru.
            
            2. Selalu perbarui perangkat lunak dan sistem operasi. 
            Selalu perbarui sistem operasi dan aplikasi Anda. Setiap kali pembaruan dirilis untuk perangkat Anda, unduh dan instal segera. Pembaruan ini sering mencakup perbaikan keamanan, tambalan kerentanan, dan pemeliharaan lain yang diperlukan.
            
            3. Berhati-hatilah saat memasang program dan aplikasi. 
            Perhatikan baik-baik layar penginstalan dan perjanjian lisensi saat menginstal perangkat lunak. Opsi penginstalan kustom atau lanjutan sering mengungkapkan perangkat lunak pihak ketiga yang juga sedang diinstal. Berhati-hatilah dalam setiap tahap proses dan pastikan Anda tahu apa yang Anda setujui sebelum mengklik "Berikutnya".
            
            4. Instal pemblokir iklan. 
            Gunakan pemblokir konten berbasis browser, seperti AdGuard. Pemblokir konten membantu menghentikan iklan berbahaya, Trojan, phishing, dan konten tidak diinginkan lainnya yang mungkin tidak dapat dihentikan oleh produk antivirus saja.
            
            5. Berhati-hatilah dengan apa yang Anda unduh. 
            Tujuan utama penjahat dunia maya adalah mengelabui Anda agar mengunduh malware—program atau aplikasi yang membawa malware atau mencoba mencuri informasi. Malware ini dapat disamarkan sebagai aplikasi: mulai dari game populer hingga sesuatu yang memeriksa lalu lintas atau cuaca.
            
            6. Waspadai orang yang mencoba menipu Anda. 
            Baik itu email, ponsel, messenger, atau aplikasi lainnya, selalu waspada dan waspada terhadap seseorang yang mencoba mengelabui Anda agar mengeklik tautan atau membalas pesan. Ingatlah bahwa memalsukan nomor telepon itu mudah, jadi nama atau nomor yang dikenal tidak membuat pesan menjadi lebih tepercaya.
            
            7. Cadangkan data Anda. 
            Cadangkan data Anda sesering mungkin dan periksa apakah data cadangan Anda dapat dipulihkan. Anda dapat melakukan ini secara manual pada stik HDD/USB eksternal, atau secara otomatis menggunakan perangkat lunak pencadangan. Ini juga merupakan cara terbaik untuk melawan ransomware. Jangan pernah menghubungkan drive cadangan ke komputer jika Anda menduga bahwa komputer tersebut terinfeksi malware.
            
            8. Pilih kata sandi yang kuat. 
            Gunakan kata sandi yang kuat dan unik untuk setiap akun Anda. Hindari penggunaan informasi pribadi atau kata-kata yang mudah ditebak dalam kata sandi Anda. Aktifkan autentikasi dua faktor (2FA) di akun Anda jika memungkinkan.
            
            9. Hati-hati di mana Anda mengklik. 
            Berhati-hatilah saat mengklik tautan atau mengunduh lampiran dari sumber yang tidak dikenal. Ini berpotensi mengandung malware atau penipuan phishing.
            
            10. Jangan gunakan perangkat lunak bajakan. 
            Hindari penggunaan program berbagi file Peer-to-Peer (P2P), keygen, crack, dan perangkat lunak bajakan lainnya yang sering kali dapat membahayakan data, privasi, atau keduanya.
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
            onBackPressed() // Fungsi untuk kembali ke halaman sebelumnya
        }
    }
}
