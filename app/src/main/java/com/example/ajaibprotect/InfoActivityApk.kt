package com.example.ajaibprotect

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
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
        val appInfo: ApplicationInfo

        try {
            appInfo = packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA)
        } catch (e: PackageManager.NameNotFoundException) {
            Toast.makeText(this, "Aplikasi tidak ditemukan", Toast.LENGTH_SHORT).show()
            finish() // Menutup activity jika aplikasi tidak ditemukan
            return
        }

        // Inisialisasi view
        val textViewAppName = findViewById<TextView>(R.id.textViewAppName)
        val textViewInstallerName = findViewById<TextView>(R.id.textViewInstallerName)
        val textViewSize = findViewById<TextView>(R.id.textViewSize)
        val textViewPath = findViewById<TextView>(R.id.textViewPath)
        val textViewAsalDownload = findViewById<TextView>(R.id.textViewAsalDownload)
        val appIcon = findViewById<ImageView>(R.id.appIcon)

        // Menampilkan informasi kapasitas aplikasi
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
        val apkMalwareScore = detectSuspiciousAppName(textViewAppName.text.toString(), packageName)
        val sizeScore = detectAppSize(file.length()) // Menambahkan deteksi ukuran aplikasi
        val predictionScore = calculatePredictionScore(asalDownload, permissions, packageName, score, malwareScore, sizeScore)
        val finalPredictionScore = predictionScore + apkMalwareScore

        val textPredictionScore = findViewById<TextView>(R.id.textPredictionScore)
        textPredictionScore.text = "$finalPredictionScore"

        // Menampilkan hasil scanning
        val scanningResult = getScanningResultStyled(finalPredictionScore)
        val textHasilScanning = findViewById<TextView>(R.id.textHasilScanning)
        textHasilScanning.text = scanningResult

        // Melihat status aplikasi
        val textAplikasiStatus = findViewById<TextView>(R.id.textAplikasiStatus)
        val isAppRunning = isAppRunning(packageName)
        textAplikasiStatus.text = if (isAppRunning) "Sedang Berjalan" else "Tidak Berjalan"

        // Klik untuk tombol uninstall
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
        val uid = packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA).uid
        val foregroundDataUsage = TrafficStats.getUidRxBytes(uid).toFloat() / (1024 * 1024) // MB
        val backgroundDataUsage = TrafficStats.getUidTxBytes(uid).toFloat() / (1024 * 1024) // MB

        var score = 0.0f
        if (backgroundDataUsage > 500) score += 0.3f
        if (backgroundDataUsage > foregroundDataUsage) score += 0.5f

        return score
    }

    // Mendapatkan daftar izin aplikasi
    private fun getPermissionsList(packageName: String): List<String> {
        val permissionsList = mutableListOf<String>()
        try {
            val packageInfo = packageManager.getPackageInfo(packageName, PackageManager.GET_PERMISSIONS)
            packageInfo.requestedPermissions?.let { permissionsList.addAll(it) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return permissionsList
    }

    // Melihat status aplikasi
    private fun isAppRunning(packageName: String): Boolean {
        val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        return activityManager.runningAppProcesses.any { it.processName == packageName }
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
    private fun calculatePredictionScore(asalDownload: String, permissions: List<String>, packageName: String, dataUsageInLastMonth: Float, malwareScore: Float, sizeScore: Float): Float {
        var asalDownloadScore = when (asalDownload) {
            "Google Play Store" -> 0.1f
            "WhatsApp" -> 0.3f
            "Telegram" -> 0.7f
            "Google Chrome" -> 0.6f
            "Browser" -> 0.6f
            "Amazon Appstore" -> 0.4f
            "Samsung Galaxy Store" -> 0.4f
            "Xiaomi App Store" -> 0.4f
            "Aptoide" -> 0.6f
            "Huawei AppGallery" -> 0.4f
            else -> 1.0f
        }

        // Skor berdasarkan izin yang diminta
        var izinScore = 0.0f
        for (permission in permissions) {
            when (permission) {
                // Izin Sistem (Berisiko tinggi jika disalahgunakan)
                "android.permission.REBOOT" -> izinScore += 0.5f // Mengizinkan aplikasi untuk memulai ulang perangkat, sangat jarang digunakan oleh aplikasi normal.
                "android.permission.SHUTDOWN" -> izinScore += 0.5f // Mengizinkan aplikasi untuk mematikan perangkat, sering digunakan oleh malware untuk mengganggu.
                "android.permission.CHANGE_CONFIGURATION" -> izinScore += 0.3f // Mengubah konfigurasi sistem (misalnya orientasi layar), risiko sedang.
                "android.permission.SYSTEM_ALERT_WINDOW" -> izinScore += 0.4f // Menampilkan jendela di atas aplikasi lain, sering digunakan malware untuk phishing.
                "android.permission.SET_WALLPAPER" -> izinScore += 0.2f // Mengubah wallpaper, risiko rendah.

                // Izin Koneksi Jaringan (Malware sering membutuhkan ini untuk berkomunikasi dengan server C2)
                "android.permission.INTERNET" -> izinScore += 0.2f // Diperlukan untuk mengakses internet, banyak digunakan oleh semua aplikasi.
                "android.permission.ACCESS_NETWORK_STATE" -> izinScore += 0.1f // Mengecek status jaringan, risiko rendah.
                "android.permission.ACCESS_WIFI_STATE" -> izinScore += 0.1f // Mengecek status Wi-Fi, risiko rendah.
                "android.permission.CHANGE_NETWORK_STATE" -> izinScore += 0.2f // Mengubah status jaringan, dapat digunakan malware untuk manipulasi koneksi.

                // Izin Privasi dan Data Sensitif (Berisiko tinggi jika disalahgunakan)
                "android.permission.CAMERA" -> izinScore += 0.4f // Mengakses kamera, digunakan oleh spyware untuk merekam tanpa sepengetahuan pengguna.
                "android.permission.RECORD_AUDIO" -> izinScore += 0.4f // Merekam audio, sering digunakan oleh malware untuk menguping percakapan.
                "android.permission.READ_CONTACTS" -> izinScore += 0.3f // Membaca daftar kontak, digunakan oleh malware untuk pencurian data pribadi.
                "android.permission.READ_PHONE_NUMBERS" -> izinScore += 0.3f // Membaca nomor telepon pengguna, data sensitif.
                "android.permission.PACKAGE_USAGE_STATS" -> izinScore += 0.4f // Melihat aktivitas aplikasi, sering digunakan malware untuk memantau aktivitas pengguna.

                // Izin Lokasi (Digunakan malware untuk pelacakan)
                "android.permission.ACCESS_FINE_LOCATION" -> izinScore += 0.3f // Mengakses lokasi akurat, sangat sensitif.
                "android.permission.ACCESS_COARSE_LOCATION" -> izinScore += 0.2f // Mengakses lokasi perkiraan, kurang sensitif dibanding lokasi akurat.
                "android.permission.ACCESS_BACKGROUND_LOCATION" -> izinScore += 0.4f // Mengakses lokasi di latar belakang, sering digunakan untuk pelacakan.

                // Izin Penyimpanan (Risiko sedang hingga tinggi tergantung konteks)
                "android.permission.READ_EXTERNAL_STORAGE" -> izinScore += 0.3f // Membaca file dari penyimpanan eksternal, dapat digunakan malware untuk mencuri data.
                "android.permission.WRITE_EXTERNAL_STORAGE" -> izinScore += 0.2f // Menulis ke penyimpanan eksternal, dapat digunakan untuk menyimpan file berbahaya.
                "android.permission.MANAGE_EXTERNAL_STORAGE" -> izinScore += 0.4f // Mengelola semua file di penyimpanan, sering digunakan malware untuk mengakses data secara bebas.

                // Izin Pesan dan Telepon (Digunakan malware untuk penipuan atau pengintaian)
                "android.permission.SEND_SMS" -> izinScore += 0.5f // Mengirim SMS, sering digunakan oleh malware untuk scam.
                "android.permission.RECEIVE_SMS" -> izinScore += 0.3f // Menerima SMS, dapat digunakan untuk mencuri OTP.
                "android.permission.READ_SMS" -> izinScore += 0.4f // Membaca SMS, dapat digunakan untuk mencuri data sensitif seperti OTP.
                "android.permission.CALL_PHONE" -> izinScore += 0.3f // Melakukan panggilan telepon, dapat digunakan untuk panggilan tidak sah.
                "android.permission.READ_CALL_LOG" -> izinScore += 0.3f // Membaca riwayat panggilan, data moderat sensitif.

                // Izin Sistem dan Latar Belakang
                "android.permission.FOREGROUND_SERVICE" -> izinScore +=  0.4f // Menjalankan layanan di latar depan, sering digunakan malware untuk terus berjalan.
                "android.permission.WAKE_LOCK" -> izinScore += 0.3f // Menjaga perangkat tetap aktif, digunakan malware untuk tetap berjalan di latar belakang.

                // Izin Biometrik dan Sensor
                "android.permission.USE_BIOMETRIC" -> izinScore += 0.4f // Menggunakan data biometrik, sangat sensitif.
                "android.permission.BODY_SENSORS" -> izinScore += 0.3f // Mengakses sensor tubuh, risiko sedang.
                "android.permission.ACTIVITY_RECOGNITION" -> izinScore += 0.2f // Mengenali aktivitas fisik, risiko sedang.

                // Izin Lain
                "android.permission.REQUEST_INSTALL_PACKAGES" -> izinScore += 0.5f // Menginstal aplikasi, sering digunakan malware untuk menyebarkan dirinya.
            }
        }

        // Mengurangi skor untuk aplikasi resmi
        val predictionScore = asalDownloadScore + izinScore + dataUsageInLastMonth + malwareScore + sizeScore



        // Mengurangi skor untuk aplikasi resmi
        if (officialApps.values.contains(packageName)) {
            return when {
                predictionScore >= 10.0 -> predictionScore - 5.0f
                predictionScore >= 9.0 -> predictionScore - 5.0f
                predictionScore >= 8.0 -> predictionScore - 4.0f
                predictionScore >= 7.0 -> predictionScore - 3.0f
                predictionScore >= 6.0 -> predictionScore - 2.0f
                predictionScore >= 4.5 -> predictionScore - 1.0f
                else -> predictionScore
            }
        }

        return predictionScore
    }

    // Deteksi ukuran aplikasi yang tidak wajar
    private fun detectAppSize(size: Long): Float {
        return when {
            size < 1024 * 5 -> 3.0f // Terlalu kecil (kurang dari 5 MB)
            size > 1024 * 120 -> 3.0f // Terlalu besar (lebih dari 120 MB)
            else -> 0.0f // Ukuran normal
        }
    }

    // Skor berdasarkan pemantauan sumber daya
    private fun detectMalware(packageName: String): Float {
        val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val processes = activityManager.runningAppProcesses

        var cpuUsage = 0L
        var memoryUsage = 0L

        for (processInfo in processes) {
            if (processInfo.processName == packageName) {
                val pInfo = Debug.MemoryInfo()
                Debug.getMemoryInfo(pInfo)
                cpuUsage = pInfo.getTotalPss().toLong()
                memoryUsage = pInfo.getTotalPss().toLong()
                break
            }
        }

        var malwareScore = 0.0f
        if (cpuUsage > 500) {
            malwareScore += 0.5f
        }
        if (memoryUsage > 500 * 1024) {
            malwareScore += 0.5f
        }

        return malwareScore
    }

    // Daftar aplikasi resmi dengan nama paketnya
    val officialApps = mapOf(
        // Media Sosial
        "Facebook" to "com.facebook.katana", "Instagram" to "com.instagram.android", "WhatsApp" to "com.whatsapp",
        "WhatsApp Business" to "com.whatsapp.w4b", "Twitter" to "com.twitter.android", "YouTube" to "com.google.android.youtube",
        "TikTok" to "com.ss.android.ugc.trill", "Telegram" to "org.telegram.messenger", "Pinterest" to "com.pinterest",
        "LinkedIn" to "com.linkedin.android", "Discord" to "com.discord",

        // Aplikasi Streaming
        "Netflix" to "com.netflix.mediaclient", "Spotify" to "com.spotify.music",

        // Aplikasi Video Conferencing
        "Zoom" to "us.zoom.videomeetings", "Google Meet" to "com.google.android.apps.meetings",
        "Microsoft Teams" to "com.microsoft.teams", "Skype" to "com.skype.raider", "Cisco Webex Meetings" to "com.cisco.webex.meetings",

        // Aplikasi Produktivitas
        "Trello" to "com.trello", "Dropbox" to "com.dropbox.android", "Adobe Acrobat Reader" to "com.adobe.reader",
        "Google Maps" to "com.google.android.apps.maps", "Google Drive" to "com.google.android.apps.docs",
        "Google Docs" to "com.google.android.apps.docs.editors.docs", "Google Sheets" to "com.google.android.apps.docs.editors.sheets",
        "Google Slides" to "com.google.android.apps.docs.editors.slides", "Microsoft Office" to "com.microsoft.office.officehubrow",
        "Microsoft Word" to "com.microsoft.office.word", "Microsoft Excel" to "com.microsoft.office.excel",
        "Microsoft PowerPoint" to "com.microsoft.office.powerpoint", "OneNote" to "com.microsoft.office.onenote", "Microsoft To-Do" to "com.microsoft.todos",

        // Aplikasi E-Commerce
        "Tokopedia" to "com.tokopedia.tkpd", "Tokopedia Seller" to "com.tokopedia.sellerapp", "Bukalapak" to "com.bukalapak.android",
        "Shopee" to "com.shopee.id", "Lazada" to "com.lazada.android", "Blibli" to "blibli.mobile.commerce",
        "JD.ID" to "com.jd.id", "Zalora" to "com.zalora.android",

        // Aplikasi Bank
        "Bank Mandiri" to "com.bankmandiri.mandirionline", "Bank BCA" to "com.bca", "Bank BRI" to "bri.mobile", "Bank CIMB Niaga" to "com.cimbniaga.movingforward",
        "Bank Danamon" to "com.danamon.go", "Bank BTN" to "com.btn.mobilebanking", "Bank Mega" to "com.bankmega.mobile", "BNIMobilenew" to "src.com.bni",

        // Aplikasi Dompet Digital
        "OVO" to "ovo.id", "GoPay" to "com.gojek.gopay", "DANA" to "id.dana", "LinkAja" to "com.telkomsel.tcash", "PayPal" to "com.paypal.android.p2pmobile",

        // Game Terpercaya
        "PUBG Mobile" to "com.tencent.ig", "Call of Duty: Mobile" to "com.activision.callofduty.shooter", "Mobile Legends: Bang Bang" to "com.mobile.legends",
        "Genshin Impact" to "com.miHoYo.GenshinImpact", "Clash of Clans" to "com.supercell.clashofclans",

        // Aplikasi Pengeditan
        "Canva" to "com.canva.editor", "Adobe Photoshop Express" to "com.adobe.psmobile", "PicsArt" to "com.picsart.studio",
        "Snapseed" to "com.niksoftware.snapseed", "InShot" to "com.camerasideas.instashot", "KineMaster" to "com.nexstreaming.app.kinemasterfree",
        "VivaVideo" to "com.quvideo.vivavideo", "FilmoraGo" to "com.wondershare.filmorago", "Adobe Lightroom" to "com.adobe.lrmobile",

        // Aplikasi Resmi Google
        "Gmail" to "com.google.android.gm", "Google Chrome" to "com.android.chrome", "Google Photos" to "com.google.android.apps.photos",
        "Google Calendar" to "com.google.android.calendar", "Google Keep" to "com.google.android.keep",

        // Aplikasi Resmi Microsoft
        "Outlook" to "com.microsoft.office.outlook", "OneDrive" to "com.microsoft.skydrive", "Microsoft Authenticator" to "com.azure.authenticator"

    )

    // Deteksi nama aplikasi mencurigakan
    private fun detectSuspiciousAppName(appName: String, packageName: String): Float {
        var score = 0.0f

        // Kriteria panjang nama
        if (appName.length > 20) {
            score += 0.5f // Skor tinggi untuk nama aplikasi yang terlalu panjang
        }

        // Kriteria penggunaan angka
        if (appName.matches(Regex(".*\\d.*"))) {
            score += 0.5f // Skor untuk nama aplikasi yang mengandung angka
        }

        // Kriteria penggunaan kata kunci mencurigakan
        val suspiciousKeywords = listOf("hack", "free", "download", "crack", "virus", "malware", "spy", "trojan", "phishing", "scam", "fake", "premium", "unlock", "keygen", "bypass")
        for (keyword in suspiciousKeywords) {
            if (appName.contains(keyword, ignoreCase = true)) {
                score += 1.0f // Skor tinggi untuk nama aplikasi yang mengandung kata kunci mencurigakan
                break // Hentikan pencarian setelah menemukan satu kata kunci
            }
        }

        // Kriteria penggunaan karakter khusus
        if (appName.matches(Regex(".*[!@#$%^&*(),.?\":{}|<>].*"))) {
            score += 0.5f // Skor untuk nama aplikasi yang mengandung karakter khusus
        }

        // Kriteria kombinasi kata yang tidak biasa
        if (appName.split(" ").size > 3) {
            score += 0.5f // Skor untuk nama aplikasi yang terdiri dari lebih dari 3 kata
        }

        // Kriteria penggunaan huruf kapital yang tidak biasa
        if (appName.matches(Regex(".*[A-Z]{2,}.*"))) {
            score += 0.5f // Skor untuk nama aplikasi yang mengandung huruf kapital berlebihan
        }

        // Kriteria nama yang mirip dengan aplikasi populer
        for ((popularApp, officialPackage) in officialApps) {
            if (appName.contains(popularApp, ignoreCase = true) && packageName != officialPackage) {
                score += 5.0f // Skor tinggi untuk nama aplikasi yang mirip dengan aplikasi populer, tetapi bukan aplikasi resmi
                break // Hentikan pencarian setelah menemukan satu aplikasi populer
            }
        }

        // Kriteria penggunaan kata "official"
        if (appName.contains("official", ignoreCase = true)) {
            score += 0.5f // Skor untuk nama aplikasi yang mengandung kata "official"
        }

        // Kriteria penggunaan kata "beta"
        if (appName.contains("beta", ignoreCase = true)) {
            score += 0.5f // Skor untuk nama aplikasi yang mengandung kata "beta"
        }

        // Kriteria penggunaan kata "new"
        if (appName.contains("new", ignoreCase = true)) {
            score += 0.5f // Skor untuk nama aplikasi yang mengandung kata "new"
        }

        // Jika aplikasi resmi terdeteksi, kurangi skor
        if (officialApps.containsValue(packageName)) {
            score = 0.0f // Set skor ke 0 jika aplikasi resmi terdeteksi
        }

        return score
    }

    private fun getScanningResultStyled(finalPredictionScore: Float): CharSequence {
        return when {
            finalPredictionScore >= 5.9 -> HtmlCompat.fromHtml("<font color='#FF1400'> Berisiko (Mohon periksa izin yang diminta dan perilaku aplikasi dengan lebih teliti.) </font>", HtmlCompat.FROM_HTML_MODE_LEGACY)
            finalPredictionScore >= 5.0 -> HtmlCompat.fromHtml("<font color='#EDAE00'> Waspada </font>", HtmlCompat.FROM_HTML_MODE_LEGACY)
            else -> HtmlCompat.fromHtml("<font color='#00B438'>  Normal </font>", HtmlCompat.FROM_HTML_MODE_LEGACY)
        }
    }
}