package com.example.ajaibprotect

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.widget.TextViewCompat


class TipsAndSupportActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tips_and_support)

        val textView = findViewById<TextView>(R.id.textView)
        textView.text = """
            Tips Mengatasi Malware pada Aplikasi Android
            
            1.Hapus Aplikasi yang Mencurigakan
             Jika kamu merasa ada aplikasi yang aneh atau nggak pernah diunduh tapi muncul di perangkatmu, segera hapus. Pergi ke Pengaturan > Aplikasi > Aplikasi Terinstal, cari aplikasi yang mencurigakan, lalu pilih Uninstall.


            2.Matikan Koneksi Internet
             Putuskan Wi-Fi atau matikan data seluler. Ini mencegah malware mengunduh data tambahan atau mengirim informasi pribadi ke server penjahat siber.


            3.Masuk ke Safe Mode
             Restart perangkatmu dalam mode aman (Safe Mode). Caranya biasanya dengan menahan tombol daya, lalu tekan dan tahan opsi Matikan Daya hingga muncul pilihan Safe Mode. Mode ini hanya menjalankan aplikasi sistem bawaan, sehingga malware tidak aktif.


            4.Gunakan Pemindai Keamanan Bawaan
             Banyak perangkat Android memiliki fitur pemindai keamanan bawaan, seperti Google Play Protect. Buka aplikasi Play Store, pilih Profil > Play Protect, lalu jalankan pemindaian.


            5.Perbarui Sistem Operasi
             Pastikan perangkat Android kamu selalu menjalankan versi terbaru dari sistem operasi. Pembaruan ini sering mengandung perbaikan untuk celah keamanan.


            6.Periksa Izin Aplikasi
             Buka pengaturan izin aplikasi di perangkatmu (Pengaturan > Privasi > Izin Aplikasi) dan periksa aplikasi mana saja yang punya akses ke kamera, mikrofon, lokasi, atau data lainnya. Hapus izin dari aplikasi mencurigakan.


            7.Gunakan Alat Penghapus Malware
             Unduh aplikasi penghapus malware terpercaya dari Google Play Store, seperti Malwarebytes atau Bitdefender Mobile Security, untuk membersihkan perangkat.


            8.Reset ke Pengaturan Pabrik (Factory Reset)
             Kalau malware tetap nggak bisa dihapus, reset perangkat ke pengaturan pabrik. Sebelum melakukannya, cadangkan semua data penting ke penyimpanan eksternal. Caranya:
               Buka Pengaturan > Sistem > Reset > Reset Data Pabrik.
               
               
            9.Aktifkan Otentikasi Dua Faktor (2FA)
             Segera amankan akun Google atau aplikasi lain dengan 2FA. Ini penting kalau malware sudah mencuri data login kamu.


            10.Hindari Aplikasi dari Sumber Tidak Resmi
             Jangan pernah mengunduh aplikasi dari luar Google Play Store atau menggunakan aplikasi bajakan. Aplikasi dari sumber tak resmi sering disusupi malware.

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
