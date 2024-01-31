package com.example.ajaibprotect

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.ListView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.ajaibprotect.adapters.AppListAdapter
import android.util.Log
import android.app.NotificationManager

class UserAppsHome : AppCompatActivity() {

    private lateinit var listViewApps: ListView
    private lateinit var appListAdapter: AppListAdapter
    private lateinit var appsList: List<ResolveInfo>

    private val appInfoLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            // Handle the result if needed
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_apps_home)

        // Mendapatkan daftar aplikasi yang diunduh oleh pengguna
        appsList = getListAplikasiPengguna()

        // Mendapatkan referensi ListView
        listViewApps = findViewById(R.id.listViewApps)

        // Mengatur adapter untuk ListView
        appListAdapter = AppListAdapter(appsList, packageManager)
        listViewApps.adapter = appListAdapter

        // Menambahkan click listener untuk setiap item di ListView
        listViewApps.setOnItemClickListener { _, _, position, _ ->
            val appInfo = appsList[position]
            // Lakukan tindakan yang diinginkan ketika item diklik, misalnya membuka InfoActivityApk
            // dengan mengirimkan informasi aplikasi terkait
            bukaInfoActivityApk(appInfo.activityInfo.packageName)
        }
    }

    // Fungsi untuk membuka InfoActivityApk dengan mengirimkan informasi aplikasi terkait
    private fun bukaInfoActivityApk(packageName: String) {
        val intent = Intent(this, InfoActivityApk::class.java)
        intent.putExtra("PACKAGE_NAME", packageName)
        appInfoLauncher.launch(intent)
    }

    // Fungsi untuk menghandle klik tombol back
    fun onBackButtonClick(view: View) {
        val intent = Intent(this, HomeScanningActivity::class.java)
        startActivity(intent)
    }

    // Mendapatkan daftar aplikasi yang diunduh oleh pengguna
    private fun getListAplikasiPengguna(): List<ResolveInfo> {
        val packageManager: PackageManager = packageManager
        val mainIntent = Intent(Intent.ACTION_MAIN, null).addCategory(Intent.CATEGORY_LAUNCHER)
        val appsList = packageManager.queryIntentActivities(mainIntent, PackageManager.GET_RESOLVED_FILTER)
            .filter { resolveInfo -> resolveInfo.activityInfo.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM == 0 }

        // Logging untuk memeriksa hasil
        for (info in appsList) {
            Log.d("Aplikasi", "Nama Aplikasi: ${info.activityInfo.packageName}")
        }

        return appsList
    }

    // Fungsi untuk membuka halaman sistem aplikasi
    fun openSystemAppsPage(view: View) {
        val intent = Intent(this, UserSystemHome::class.java)
        startActivity(intent)
    }

    // Fungsi untuk menampilkan dialog konfirmasi reset preferensi aplikasi
    fun showResetConfirmationDialog(view: View) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Reset Preferensi Aplikasi")
        builder.setMessage(
            "Ini akan mereset semua preferensi untuk:\n" +
                    "- Aplikasi nonaktif\n" +
                    "- Pembatasan notifikasi untuk aplikasi\n" +
                    "- Aplikasi default\n" +
                    "- Pembatasan data latar belakang untuk aplikasi\n" +
                    "- Pembatasan izin\n\n" +
                    "Anda tidak akan kehilangan data aplikasi yang ada."
        )
        builder.setPositiveButton("Reset") { _, _ ->
            resetAppPreferences()
            Toast.makeText(this, "Preferensi aplikasi telah direset", Toast.LENGTH_SHORT).show()
        }
        builder.setNegativeButton("Batal") { _, _ ->
            // Tidak melakukan apa-apa jika dibatalkan
        }
        val dialog = builder.create()
        dialog.show()
    }

    private fun resetAppPreferences() {
        // Hapus cache
        cacheDir.deleteRecursively()

        // Hapus file-file lain yang terkait dengan preferensi aplikasi
        filesDir.deleteRecursively()

        // Reset pengaturan
        val sharedPref = getSharedPreferences("PREF_NAME", Context.MODE_PRIVATE)
        sharedPref.edit().clear().apply()

        // Reset izin
        val packageManager: PackageManager = packageManager
        val appsList = getListAplikasiPengguna()
        for (resolveInfo in appsList) {
            val packageName = resolveInfo.activityInfo.packageName
            packageManager.setApplicationEnabledSetting(
                packageName,
                PackageManager.COMPONENT_ENABLED_STATE_DEFAULT,
                PackageManager.DONT_KILL_APP
            )
        }

        // Reset preferensi notifikasi
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager
        notificationManager?.cancelAll()

        // Reset pengaturan default
        val settingsIntent = Intent(Settings.ACTION_SETTINGS)
        startActivity(settingsIntent)

        // Tampilkan pesan bahwa preferensi aplikasi telah direset
        Toast.makeText(this, "Preferensi aplikasi telah direset", Toast.LENGTH_SHORT).show()
    }
}
