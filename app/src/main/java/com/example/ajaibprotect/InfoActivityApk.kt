package com.example.ajaibprotect

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.BatteryManager
import android.os.Bundle
import android.os.Debug
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import android.widget.*

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
        val predictionScore = calculatePredictionScore(asalDownload, permissions, packageName)
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
            "com.amazon.venezia" -> "Amazon Appstore"
            "com.sec.android.app.samsungapps" -> "Samsung Galaxy Store"
            "com.miui.supermarket" -> "Xiaomi App Store"
            "com.aptoide.app" -> "Aptoide"
            "com.huawei.appmarket" -> "Huawei AppGallery"
            else -> "Tidak diketahui"
        }
    }

    // Menghitung skor prediksi
    private fun calculatePredictionScore(asalDownload: String, permissions: List<String>, packageName: String): Float {
        // Logika perhitungan skor prediksi di sini
        var asalDownloadScore = 0.0f

        when (asalDownload) {
            "Google Play Store" -> asalDownloadScore = 0.1f
            "WhatsApp" -> asalDownloadScore = 0.5f
            "Telegram" -> asalDownloadScore = 0.5f
            "Google Chrome" -> asalDownloadScore = 0.3f
            "Browser" -> asalDownloadScore = 0.3f
            "Amazon Appstore" -> asalDownloadScore = 0.4f
            "Samsung Galaxy Store" -> asalDownloadScore = 0.4f
            "Xiaomi App Store" -> asalDownloadScore = 0.4f
            "Aptoide" -> asalDownloadScore = 0.6f
            "Huawei AppGallery" -> asalDownloadScore = 0.4f
            else -> asalDownloadScore = 0.7f // Sumber unduhan tidak resmi atau berpotensi mengandung malware
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

        // Menghitung total skor berdasarkan asal unduh, izin, dan pemantauan sumber daya
        val predictionScore = asalDownloadScore + izinScore + resourceMonitoringScore(packageName)
        return predictionScore
    }

    // Skor berdasarkan pemantauan sumber daya
    private fun resourceMonitoringScore(packageName: String): Float {
        // Mengakses layanan ActivityManager untuk mendapatkan informasi tentang proses yang berjalan
        val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val processes = activityManager.runningAppProcesses

        var cpuUsage = 0L
        var memoryUsage = 0L

        // Memeriksa setiap proses yang berjalan
        for (processInfo in processes) {
            // Jika nama paket aplikasi dari proses sesuai dengan yang dicari
            if (processInfo.processName == packageName) {
                // Mendapatkan informasi memori dari proses menggunakan Debug.MemoryInfo
                val pInfo = Debug.MemoryInfo()
                Debug.getMemoryInfo(pInfo)
                // Mengambil penggunaan CPU dan memori total dalam KB
                cpuUsage = pInfo.getTotalPss().toLong()
                memoryUsage = pInfo.getTotalPss().toLong()
                break
            }
        }

        // Menghitung skor sumber daya berdasarkan penggunaan CPU dan memori
        var resourceScore = 0.0f
        // Menyesuaikan ambang batas dan logika penilaian berdasarkan kebutuhan
        // Jika penggunaan CPU lebih dari 500 KB, tambahkan 0.3 ke skor
        if (cpuUsage > 500) {
            resourceScore += 0.3f
        }
        // Jika penggunaan memori lebih dari 500 MB (dalam KB), tambahkan 0.3 ke skor
        if (memoryUsage > 500 * 1024) { // 500 MB dalam KB
            resourceScore += 0.3f
        }

        return resourceScore
    }


    private fun getScanningResultStyled(predictionScore: Float): CharSequence {
        return when {
            predictionScore >= 5.6f -> HtmlCompat.fromHtml("<font color='#FF1400'><b> Malware</b></font>", HtmlCompat.FROM_HTML_MODE_LEGACY)
            predictionScore >= 5.0f -> HtmlCompat.fromHtml("<font color='#EDAE00'> Warning</font>", HtmlCompat.FROM_HTML_MODE_LEGACY)
            else -> HtmlCompat.fromHtml("<font color='#00B438'> Normal</font>", HtmlCompat.FROM_HTML_MODE_LEGACY)
        }
    }

}

