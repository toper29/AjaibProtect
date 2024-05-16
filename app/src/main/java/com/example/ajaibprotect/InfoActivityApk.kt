package com.example.ajaibprotect

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.net.TrafficStats
import android.net.Uri
import android.os.Bundle
import android.os.Debug
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import java.io.File

// Informasi Aplikasi saat di Scan

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
        val score = getDataUsageInLastMonth(packageName)
        val malwareScore = detectMalware(packageName)
        val predictionScore = calculatePredictionScore(asalDownload, permissions, packageName, score, malwareScore)
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

    // Menghitung penggunaan data aplikasi dalam satu bulan terakhir berdasarkan package name aplikasi
    private fun getDataUsageInLastMonth(packageName: String): Float {
        // Mendapatkan UID aplikasi berdasarkan package name
        val uid = packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA).uid

        // Menghitung penggunaan data aplikasi dalam satuan MB
        val foregroundDataUsage = TrafficStats.getUidRxBytes(uid).toFloat() / (1024 * 1024) // MB
        val backgroundDataUsage = TrafficStats.getUidTxBytes(uid).toFloat() / (1024 * 1024) // MB

        // Inisialisasi skor
        var score = 0.0f

        // Jika penggunaan data aplikasi di latar belakang lebih dari 500 MB selama 1 bulan terakhir, tambahkan 0.3 pada skor
        if (backgroundDataUsage > 500) {
            score += 0.3f
        }

        // Jika penggunaan data aplikasi di latar belakang lebih besar dari penggunaan di latar depan, tambahkan 0.5 pada skor
        if (backgroundDataUsage > foregroundDataUsage) {
            score += 0.5f
        }

        // Mengembalikan skor
        return score
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
            else -> "Unknown"
        }
    }

    // Menghitung skor prediksi
    private fun calculatePredictionScore(asalDownload: String, permissions: List<String>, packageName: String, dataUsageInLastMonth: Float, malwareScore: Float): Float {
        // Perhitungan skor prediksi di sini
        var asalDownloadScore = 0.0f

        when (asalDownload) {
            "Google Play Store" -> asalDownloadScore = 0.1f
            "WhatsApp" -> asalDownloadScore = 0.7f
            "Telegram" -> asalDownloadScore = 0.7f
            "Google Chrome" -> asalDownloadScore = 0.6f
            "Browser" -> asalDownloadScore = 0.6f
            "Amazon Appstore" -> asalDownloadScore = 0.4f
            "Samsung Galaxy Store" -> asalDownloadScore = 0.4f
            "Xiaomi App Store" -> asalDownloadScore = 0.4f
            "Aptoide" -> asalDownloadScore = 0.6f
            "Huawei AppGallery" -> asalDownloadScore = 0.4f
            else -> asalDownloadScore = 1.0f // Sumber unduhan tidak resmi atau berpotensi mengandung malware
        }

        // Skor berdasarkan izin yang diminta
        var izinScore = 0.0f
        for (permission in permissions) {
            when (permission) {
                // Izin Sistem (Berisiko tinggi jika disalahgunakan)
                "android.permission.REBOOT" -> izinScore += 0.5f // Mengizinkan aplikasi untuk memulai ulang perangkat, sangat jarang digunakan oleh aplikasi normal.
                "android.permission.SHUTDOWN" -> izinScore += 0.5f // Mengizinkan aplikasi untuk mematikan perangkat, sering digunakan oleh malware untuk mengganggu.
                "android.permission.CHANGE_CONFIGURATION" -> izinScore += 0.2f // Mengubah konfigurasi sistem (misalnya orientasi layar), risiko sedang.
                "android.permission.SYSTEM_ALERT_WINDOW" -> izinScore += 0.5f // Menampilkan jendela di atas aplikasi lain, sering digunakan malware untuk phishing.
                "android.permission.SET_WALLPAPER" -> izinScore += 0.1f // Mengubah wallpaper, risiko rendah.

                // Izin Koneksi Jaringan (Malware sering membutuhkan ini untuk berkomunikasi dengan server C2)
                "android.permission.INTERNET" -> izinScore += 0.3f // Diperlukan untuk mengakses internet, banyak digunakan oleh semua aplikasi.
                "android.permission.ACCESS_NETWORK_STATE" -> izinScore += 0.2f // Mengecek status jaringan, risiko rendah.
                "android.permission.ACCESS_WIFI_STATE" -> izinScore += 0.2f // Mengecek status Wi-Fi, risiko rendah.
                "android.permission.CHANGE_NETWORK_STATE" -> izinScore += 0.3f // Mengubah status jaringan, dapat digunakan malware untuk manipulasi koneksi.

                // Izin Privasi dan Data Sensitif (Berisiko tinggi jika disalahgunakan)
                "android.permission.CAMERA" -> izinScore += 0.4f // Mengakses kamera, digunakan oleh spyware untuk merekam tanpa sepengetahuan pengguna.
                "android.permission.RECORD_AUDIO" -> izinScore += 0.4f // Merekam audio, sering digunakan oleh malware untuk menguping percakapan.
                "android.permission.READ_CONTACTS" -> izinScore += 0.5f // Membaca daftar kontak, digunakan oleh malware untuk pencurian data pribadi.
                "android.permission.READ_PHONE_NUMBERS" -> izinScore += 0.4f // Membaca nomor telepon pengguna, data sensitif.
                "android.permission.PACKAGE_USAGE_STATS" -> izinScore += 0.5f // Melihat aktivitas aplikasi, sering digunakan malware untuk memantau aktivitas pengguna.

                // Izin Lokasi (Digunakan malware untuk pelacakan)
                "android.permission.ACCESS_FINE_LOCATION" -> izinScore += 0.5f // Mengakses lokasi akurat, sangat sensitif.
                "android.permission.ACCESS_COARSE_LOCATION" -> izinScore += 0.3f // Mengakses lokasi perkiraan, kurang sensitif dibanding lokasi akurat.
                "android.permission.ACCESS_BACKGROUND_LOCATION" -> izinScore += 0.5f // Mengakses lokasi di latar belakang, sering digunakan untuk pelacakan.

                // Izin Penyimpanan (Risiko sedang hingga tinggi tergantung konteks)
                "android.permission.READ_EXTERNAL_STORAGE" -> izinScore += 0.4f // Membaca file dari penyimpanan eksternal, dapat digunakan malware untuk mencuri data.
                "android.permission.WRITE_EXTERNAL_STORAGE" -> izinScore += 0.3f // Menulis ke penyimpanan eksternal, dapat digunakan untuk menyimpan file berbahaya.
                "android.permission.MANAGE_EXTERNAL_STORAGE" -> izinScore += 0.5f // Mengelola semua file di penyimpanan, sering digunakan malware untuk mengakses data secara bebas.

                // Izin Pesan dan Telepon (Digunakan malware untuk penipuan atau pengintaian)
                "android.permission.SEND_SMS" -> izinScore += 0.5f // Mengirim SMS, sering digunakan oleh malware untuk scam.
                "android.permission.RECEIVE_SMS" -> izinScore += 0.3f // Menerima SMS, dapat digunakan untuk mencuri OTP.
                "android.permission.READ_SMS" -> izinScore += 0.4f // Membaca SMS, dapat digunakan untuk mencuri data sensitif seperti OTP.
                "android.permission.CALL_PHONE" -> izinScore += 0.4f // Melakukan panggilan telepon, dapat digunakan untuk panggilan tidak sah.
                "android.permission.READ_CALL_LOG" -> izinScore += 0.3f // Membaca riwayat panggilan, data moderat sensitif.

                // Izin Sistem dan Latar Belakang
                "android.permission.FOREGROUND_SERVICE" -> izinScore += 0.5f // Menjalankan layanan di latar depan, sering digunakan malware untuk terus berjalan.
                "android.permission.WAKE_LOCK" -> izinScore += 0.4f // Menjaga perangkat tetap aktif, digunakan malware untuk tetap berjalan di latar belakang.

                // Izin Biometrik dan Sensor
                "android.permission.USE_BIOMETRIC" -> izinScore += 0.5f // Menggunakan data biometrik, sangat sensitif.
                "android.permission.BODY_SENSORS" -> izinScore += 0.4f // Mengakses sensor tubuh, risiko sedang.
                "android.permission.ACTIVITY_RECOGNITION" -> izinScore += 0.3f // Mengenali aktivitas fisik, risiko moderat.

                // Izin Lain
                "android.permission.REQUEST_INSTALL_PACKAGES" -> izinScore += 0.5f // Menginstal aplikasi, sering digunakan malware untuk menyebarkan dirinya.
            }
        }

        // Menghitung total skor berdasarkan asal unduh, izin, dan pemantauan sumber daya
        val predictionScore = asalDownloadScore + izinScore + dataUsageInLastMonth + malwareScore
        return predictionScore
    }

    // Skor berdasarkan pemantauan sumber daya
    private fun detectMalware(packageName: String): Float {
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

        // Menghitung skor deteksi malware berdasarkan penggunaan CPU dan memori
        var malwareScore = 0.0f
        // Menyesuaikan ambang batas dan logika penilaian berdasarkan kebutuhan
        // Jika penggunaan CPU lebih dari 500 KB, tambahkan 0.5 ke skor
        if (cpuUsage > 500) {
            malwareScore += 0.5f
        }
        // Jika penggunaan memori lebih dari 500 MB (dalam KB), tambahkan 0.5 ke skor
        if (memoryUsage > 500 * 1024) { // 500 MB dalam KB
            malwareScore += 0.5f
        }

        return malwareScore
    }

    private fun getScanningResultStyled(predictionScore: Float): CharSequence {
        return when {
            predictionScore >= 5.6f -> HtmlCompat.fromHtml("<font color='#FF1400'><b> Malware</b></font>", HtmlCompat.FROM_HTML_MODE_LEGACY)
            predictionScore >= 5.0f -> HtmlCompat.fromHtml("<font color='#EDAE00'> Warning</font>", HtmlCompat.FROM_HTML_MODE_LEGACY)
            else -> HtmlCompat.fromHtml("<font color='#00B438'> Normal</font>", HtmlCompat.FROM_HTML_MODE_LEGACY)
        }
    }

}
