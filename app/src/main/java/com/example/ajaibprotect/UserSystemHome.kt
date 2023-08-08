@file:Suppress("DEPRECATION")

package com.example.ajaibprotect

import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.example.ajaibprotect.adapters.SystemListAdapter

class UserSystemHome : AppCompatActivity() {

    private lateinit var listViewApps: ListView
    private lateinit var systemAppsList: List<ApplicationInfo>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_system_apps_home)

        // Mendapatkan daftar sistem aplikasi bawaan
        systemAppsList = getSystemAppsList()

        // Mendapatkan referensi ListView
        listViewApps = findViewById(R.id.listViewApps)

        // Mengatur adapter untuk ListView
        val appListAdapter = SystemListAdapter(this, systemAppsList)
        listViewApps.adapter = appListAdapter

        // Menambahkan click listener untuk setiap item di ListView
        listViewApps.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val appInfo = systemAppsList[position]
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

    // Mendapatkan daftar sistem aplikasi bawaan
    private fun getSystemAppsList(): List<ApplicationInfo> {
        val packageManager: PackageManager = packageManager
        return packageManager.getInstalledApplications(PackageManager.GET_META_DATA)
            .filter { it.flags and ApplicationInfo.FLAG_SYSTEM != 0 } // Hanya aplikasi sistem
    }

    // Fungsi untuk membuka halaman aplikasi pengguna
    fun openUserAppsPage(view: View) {
        val intent = Intent(this, UserAppsHome::class.java)
        startActivity(intent)
    }
}
