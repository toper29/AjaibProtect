package com.example.ajaibprotect

import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.example.ajaibprotect.adapters.AppListAdapter

class UserAppsHome : AppCompatActivity() {

    private lateinit var listViewApps: ListView
    private lateinit var appListAdapter: AppListAdapter
    private lateinit var appsList: List<ApplicationInfo>

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
        listViewApps.onItemClickListener = AdapterView.OnItemClickListener { parent, _, position, _ ->
            val appInfo = parent.getItemAtPosition(position) as ApplicationInfo
            // Lakukan tindakan yang diinginkan ketika item diklik, misalnya membuka InfoActivityApk
            // dengan mengirimkan informasi aplikasi terkait
            bukaInfoActivityApk(appInfo.packageName)
        }
    }

    // Fungsi untuk membuka InfoActivityApk dengan mengirimkan informasi aplikasi terkait
    private fun bukaInfoActivityApk(packageName: String) {
        val intent = Intent(this, InfoActivityApk::class.java)
        intent.putExtra("PACKAGE_NAME", packageName)
        startActivity(intent)
    }

    // Mendapatkan daftar aplikasi yang diunduh oleh pengguna
    private fun getListAplikasiPengguna(): List<ApplicationInfo> {
        val packageManager: PackageManager = packageManager
        return packageManager.getInstalledApplications(PackageManager.GET_META_DATA)
            .filter { it.flags and ApplicationInfo.FLAG_SYSTEM == 0 } // Hanya aplikasi pengguna, bukan sistem
    }
}
