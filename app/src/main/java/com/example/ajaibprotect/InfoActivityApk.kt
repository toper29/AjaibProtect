package com.example.ajaibprotect

   //activity_info_apk

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.core.text.HtmlCompat
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import android.widget.ImageButton
import java.io.File
import android.app.ActivityManager
import android.app.usage.UsageStatsManager
import android.content.Context

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

        // Inisialisasi view
        val textViewAppName = findViewById<TextView>(R.id.textViewAppName)
        val textViewInstallerName = findViewById<TextView>(R.id.textViewInstallerName)
        val textViewSize = findViewById<TextView>(R.id.textViewSize)
        val textViewPath = findViewById<TextView>(R.id.textViewPath)
        val textViewAsalDownload = findViewById<TextView>(R.id.textViewAsalDownload)
        val appIcon = findViewById<ImageView>(R.id.appIcon)

        // Menampilkan informasi aplikasi
        textViewAppName.text = appInfo.loadLabel(packageManager).toString()
        textViewInstallerName.text = appInfo.packageName

        val file = File(appInfo.sourceDir)
        val sizeInKB = file.length() / 1024
        val sizeInMB = sizeInKB / 1024
        val sizeText = if (sizeInMB > 0) {
            "$sizeInMB MB | $sizeInKB KB"
        } else {
            "$sizeInKB KB"
        }
        textViewSize.text = sizeText

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

        // Menampilkan hasil scanning
        val scanningResult = getScanningResultStyled(predictionScore)
        val textHasilScanning = findViewById<TextView>(R.id.textHasilScanning)
        textHasilScanning.text = scanningResult

        //melihat status aplikasi
        val textAplikasiStatus = findViewById<TextView>(R.id.textAplikasiStatus)
        val isAppRunning = isAppRunning(packageName)
        if (isAppRunning) {
            textAplikasiStatus.text = "Sedang Berjalan"
        } else {
            textAplikasiStatus.text = "Tidak Berjalan"
        }


        // Set click listener for the uninstall button
        val uninstallButton = findViewById<View>(R.id.uninstallButton)
        uninstallButton.setOnClickListener { uninstallApp() }

        // Menambahkan onClickListener untuk tombol kembali
        val backButton = findViewById<ImageButton>(R.id.imageButtonback)
        backButton.setOnClickListener {
            onBackPressed()
        }

    }

    // Mendapatkan daftar izin aplikasi
    private fun getPermissionsList(packageName: String): List<String> {
        val permissionsList = mutableListOf<String>()
        try {
            val packageInfo = packageManager.getPackageInfo(packageName, PackageManager.GET_PERMISSIONS)

            // Periksa apakah ada izin yang diminta oleh aplikasi
            if (packageInfo.requestedPermissions != null) {
                for (permission in packageInfo.requestedPermissions) {
                    // Tambahkan izin ke daftar
                    permissionsList.add(permission)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return permissionsList
    }

    //melihat status aplikasi
    private fun isAppRunning(packageName: String): Boolean {
        val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val processes = activityManager.runningAppProcesses
        for (processInfo in processes) {
            if (processInfo.processName == packageName) {
                return true
            }
        }
        return false
    }




    // Menghapus aplikasi
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

    // Mendapatkan nama asal download
    private fun getInstallerName(packageName: String?): String {
        return when (packageName) {
            "com.android.vending" -> "Google Play Store"
            "com.whatsapp" -> "WhatsApp"
            "org.telegram.messenger" -> "Telegram"
            "com.android.chrome" -> "Google Chrome"
            "com.android.browser" -> "Browser"
            // Tambahkan asal download lain sesuai kebutuhan
            else -> "Tidak diketahui"
        }
    }


    // Menghitung skor prediksi
    private fun calculatePredictionScore(asalDownload: String, permissions: List<String>): Float {
        // Logika perhitungan skor prediksi di sini
        var asalDownloadScore = 0.0f

        when (asalDownload) {
            "Google Play Store" -> asalDownloadScore = 0.1f
            "WhatsApp" -> asalDownloadScore = 2.5f
            "Telegram" -> asalDownloadScore = 2.5f
            "Google Chrome" -> asalDownloadScore = 2.5f
            "Browser" -> asalDownloadScore = 2.5f
            else -> asalDownloadScore = 0.5f
        }

        // Skor berdasarkan izin yang diminta
        var izinScore = 0.0f
        for (permission in permissions) {
            when (permission) {
                // Izin Sistem
                "android.permission.REBOOT" -> izinScore += 0.4f
                "android.permission.SHUTDOWN" -> izinScore += 0.5f
                // Izin Proteksi Privasi
                "android.permission.CAMERA" -> izinScore += 0.5f
                "android.permission.READ_CONTACTS" -> izinScore += 0.4f
                "android.permission.WRITE_CONTACTS",
                "android.permission.READ_CALENDAR" -> izinScore += 0.2f
                "android.permission.WRITE_CALENDAR",
                "android.permission.READ_CALL_LOG" -> izinScore += 0.2f
                "android.permission.WRITE_CALL_LOG",
                // Izin Koneksi Jaringan
                "android.permission.INTERNET" -> izinScore += 0.1f
                "android.permission.ACCESS_NETWORK_STATE"  -> izinScore += 0.2f
                // Izin Penyimpanan
                "android.permission.READ_EXTERNAL_STORAGE" -> izinScore += 0.2f
                "android.permission.WRITE_EXTERNAL_STORAGE",
                // Izin Lokasi
                "android.permission.ACCESS_FINE_LOCATION" -> izinScore += 0.4f
                "android.permission.ACCESS_COARSE_LOCATION" -> izinScore += 0.5f
                // Izin Pemrosesan Pesan
                "android.permission.SEND_SMS" -> izinScore += 0.2f
                "android.permission.RECEIVE_SMS" -> izinScore += 0.2f
                "android.permission.READ_SMS" -> izinScore += 0.4f
                // Izin Panggilan Telepon
                "android.permission.CALL_PHONE" -> izinScore += 0.2f
                "android.permission.READ_PHONE_STATE" -> izinScore += 0.2f
                //Izin Sistem
                "android.permission.WAKE_LOCK"  -> izinScore += 0.5f // Mengizinkan aplikasi untuk menjaga perangkat tetap aktif saat layar mati.
                // Izin Media
                "android.permission.RECORD_AUDIO" -> izinScore += 0.5f //   Mengizinkan aplikasi untuk merekam audio.
                // Izin Pengelolaan File
                "android.permission.MANAGE_EXTERNAL_STORAGE"-> izinScore += 0.4f  //Memerlukan izin khusus untuk mengelola penyimpanan eksternal
            }
        }

        // Menghitung total skor berdasarkan asal unduh dan izin
        val predictionScore = asalDownloadScore + izinScore
        return predictionScore
    }

    private fun getScanningResultStyled(predictionScore: Float): CharSequence {
        return when {
            predictionScore >= 6.0f -> HtmlCompat.fromHtml("<font color='#FF1400'><b> Malware</b></font>", HtmlCompat.FROM_HTML_MODE_LEGACY)
            predictionScore >= 4.5f -> HtmlCompat.fromHtml("<font color='#EDAE00'> Warning</font>", HtmlCompat.FROM_HTML_MODE_LEGACY)
            else -> HtmlCompat.fromHtml("<font color='#00B438'> Normal</font>", HtmlCompat.FROM_HTML_MODE_LEGACY)
        }
    }




}
