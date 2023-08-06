package com.example.ajaibprotect

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.io.File

class InfoActivityApk : AppCompatActivity() {

    private lateinit var packageName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info_apk)

        // Mengambil package name dari intent
        packageName = intent.getStringExtra("PACKAGE_NAME") ?: ""

        // Mendapatkan informasi aplikasi menggunakan package manager
        val packageManager: PackageManager = packageManager
        val appInfo = packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA)

        val textViewAppName = findViewById<TextView>(R.id.textViewAppName)
        val textViewInstallerName = findViewById<TextView>(R.id.textViewInstallerName)
        val textViewSize = findViewById<TextView>(R.id.textViewSize)
        val textViewPath = findViewById<TextView>(R.id.textViewPath)

        // Menampilkan informasi aplikasi
        textViewAppName.text = appInfo.loadLabel(packageManager).toString()
        textViewInstallerName.text = appInfo.packageName

        val file = File(appInfo.sourceDir)
        val sizeInKB = file.length() / 1024
        textViewSize.text = "$sizeInKB KB"

        textViewPath.text = appInfo.sourceDir


    }
}
