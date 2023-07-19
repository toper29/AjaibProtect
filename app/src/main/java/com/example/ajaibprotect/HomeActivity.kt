package com.example.ajaibprotect

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
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

    /// Fungsi untuk membuka InfoActivity
    fun openInfoActivity(view: View) {
        val intent = Intent(this, InfoActivity::class.java)
        startActivity(intent)
    }
}
