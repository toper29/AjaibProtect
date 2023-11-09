package com.example.ajaibprotect

import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.os.Bundle
import android.view.View
import android.widget.ListView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.ajaibprotect.adapters.AppListAdapter
import android.util.Log


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
}
