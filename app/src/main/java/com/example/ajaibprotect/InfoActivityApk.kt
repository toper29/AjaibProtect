package com.example.ajaibprotect

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.io.File

@Suppress("DEPRECATION")
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
        val textViewAsalDownload = findViewById<TextView>(R.id.textViewAsalDownload) // Tambahkan TextView untuk asal download
        val appIcon = findViewById<ImageView>(R.id.appIcon)

        // Menampilkan informasi aplikasi
        textViewAppName.text = appInfo.loadLabel(packageManager).toString()
        textViewInstallerName.text = appInfo.packageName

        val file = File(appInfo.sourceDir)
        val sizeInKB = file.length() / 1024
        textViewSize.text = "$sizeInKB KB"

        textViewPath.text = appInfo.sourceDir

        // Menampilkan logo aplikasi
        val appLogo: Drawable = packageManager.getApplicationIcon(appInfo)
        appIcon.setImageDrawable(appLogo)

        // Mengambil dan menampilkan asal download aplikasi
        val installerPackageName = packageManager.getInstallerPackageName(packageName)
        val asalDownload = getInstallerName(installerPackageName)
        textViewAsalDownload.text = asalDownload

        // Menampilkan daftar izin aplikasi
        val permissions = getPermissionsList(packageName)
        val permissionListView = findViewById<ListView>(R.id.permissionListView)
        val permissionListAdapter = PermissionListAdapter(this, permissions)
        permissionListView.adapter = permissionListAdapter

        // Menghitung dan menampilkan skor prediksi
        val predictionScore = calculatePredictionScore(asalDownload, permissions)
        val textPredictionScore = findViewById<TextView>(R.id.textPredictionScore)
        textPredictionScore.text = "$predictionScore"

        // Set click listener for the uninstall button
        val uninstallButton = findViewById<View>(R.id.uninstallButton)
        uninstallButton.setOnClickListener { uninstallApp() }
    }

    private fun getPermissionsList(packageName: String): List<String> {
        val permissionsList = mutableListOf<String>()
        try {
            val packageInfo = packageManager.getPackageInfo(packageName, PackageManager.GET_PERMISSIONS)
            val permissions = packageInfo.requestedPermissions
            if (permissions != null) {
                for (permission in permissions) {
                    val permissionInfo = packageManager.getPermissionInfo(permission, 0)
                    permissionsList.add(permissionInfo.loadLabel(packageManager).toString())
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return permissionsList
    }

    private fun uninstallApp() {
        try {
            val uninstallIntent = Intent(Intent.ACTION_UNINSTALL_PACKAGE)
            uninstallIntent.data = Uri.parse("package:$packageName")
            startActivity(uninstallIntent)
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Failed to uninstall the app", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getInstallerName(packageName: String?): String {
        return when (packageName) {
            "com.android.vending" -> "Google Play Store"
            "com.amazon.venezia" -> "Amazon Appstore"
            // Tambahkan asal download lain sesuai kebutuhan
            else -> "Tidak diketahui"
        }
    }

    private fun calculatePredictionScore(asalDownload: String, permissions: List<String>): Float {
        // Logika perhitungan skor prediksi di sini
        var asalDownloadScore = 0.0f

        when (asalDownload) {
            "Google Play Store" -> asalDownloadScore = 0.1f
        }
        // Bisa menambahkan berbagai aturan atau parameter yang Anda anggap perlu
        // Skor berdasarkan izin yang diminta
        var izinScore = 0.0f

        if (permissions.contains("android.permission.READ_EXTERNAL_STORAGE")) {
            izinScore += 0.1f
        }

        if (permissions.contains("android.permission.WRITE_EXTERNAL_STORAGE")) {
            izinScore += 0.1f
        }

        if (permissions.contains("android.permission.READ_PHONE_STATE")) {
            izinScore += 0.1f
        }

        if (permissions.contains("android.permission.ACCESS_COARSE_LOCATION")) {
            izinScore += 0.1f
        }

        if (permissions.contains("android.permission.ACCESS_FINE_LOCATION")) {
            izinScore += 0.2f
        }

        if (permissions.contains("android.permission.CAMERA")) {
            izinScore += 0.2f
        }

        if (permissions.contains("android.permission.READ_CONTACTS")) {
            izinScore += 0.3f
        }

        if (permissions.contains("android.permission.CALL_PHONE")) {
            izinScore += 0.3f
        }

        if (permissions.contains("android.permission.RECORD")) {
            izinScore += 0.2f
        }

        if (permissions.contains("android.permission.GET_ACCOUNTS")) {
            izinScore += 0.2f
        }

        if (permissions.contains("android.permission.RECORD_AUDIO")) {
            izinScore += 0.1f
        }

        if (permissions.contains("android.permission.READ_PHONE_NUMBERS")) {
            izinScore += 0.1f
        }

        if (permissions.contains("android.permission.READ_CALL_LOG")) {
            izinScore += 0.5f
        }

        if (permissions.contains("android.permission.WRITE_CALL_LOG")) {
            izinScore += 0.5f
        }

        // Menghitung total skor berdasarkan asal unduh dan izin
        val predictionScore = asalDownloadScore + izinScore
        return predictionScore
    }





}
