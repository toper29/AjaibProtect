package com.example.ajaibprotect

import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.os.StatFs
import android.provider.Settings
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class HomeScanningActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_scan)

        // Mendeteksi kapasitas dan kapasitas yang terpakai
        val totalSpace = getTotalInternalMemorySize()
        val usedSpace = totalSpace - getAvailableInternalMemorySize()

        // Menampilkan informasi kapasitas
        val storageInfo = "$usedSpace GB Terpakai dari $totalSpace GB"
        val textViewStorage = findViewById<TextView>(R.id.textViewStorage)
        textViewStorage.text = storageInfo
    }

    // Mendapatkan kapasitas total penyimpanan internal
    private fun getTotalInternalMemorySize(): Long {
        val path = Environment.getDataDirectory()
        val stat = StatFs(path.path)
        val blockSize: Long = stat.blockSizeLong
        val totalBlocks: Long = stat.blockCountLong
        return totalBlocks * blockSize / (1024 * 1024 * 1024) // GB
    }

    // Mendapatkan kapasitas yang tersedia pada penyimpanan internal
    private fun getAvailableInternalMemorySize(): Long {
        val path = Environment.getDataDirectory()
        val stat = StatFs(path.path)
        val blockSize: Long = stat.blockSizeLong
        val availableBlocks: Long = stat.availableBlocksLong
        return availableBlocks * blockSize / (1024 * 1024 * 1024) // GB
    }


    // Fungsi untuk membuka halaman TipsAndSupportActivity
    fun openTipsAndSupportActivity(view: View) {
        val intent = Intent(this, TipsAndSupportActivity::class.java)
        startActivity(intent)
    }

    // Fungsi untuk membuka pengaturan bagian Backup & Reset
    fun openBackupAndResetSettings(view: View) {
        val intent = Intent(Settings.ACTION_PRIVACY_SETTINGS)
        startActivity(intent)
    }

    // Fungsi untuk membuka pengaturan bagian Security
    fun openSecuritySettings(view: View) {
        val intent = Intent(Settings.ACTION_SECURITY_SETTINGS)
        startActivity(intent)
    }

    // Fungsi untuk membuka InfoActivity
    fun openInfoActivity(view: View) {
        val intent = Intent(this, InfoActivity::class.java)
        startActivity(intent)
    }

    // Fungsi untuk membuka UserAppsHome ketika tombol "User Apps" diklik
    fun openUserAppsHome(view: View) {
        val intent = Intent(this, UserAppsHome::class.java)
        startActivity(intent)
    }

    fun openIncidentActivity(view: View) {
        val intent = Intent(this, IncidentActivity::class.java)
        startActivity(intent)
    }

    // Override metode onBackPressed untuk menutup aplikasi saat tombol Back ditekan
    override fun onBackPressed() {
        finishAffinity() // Menutup semua activity yang terkait dengan aplikasi
    }

    // Fungsi untuk membuka halaman aplikasi sistem
    fun openSystemAppsPage(view: View) {
        val intent = Intent(this, UserSystemHome::class.java)
        startActivity(intent)
    }
}
