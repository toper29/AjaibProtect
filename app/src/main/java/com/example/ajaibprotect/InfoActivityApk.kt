package com.example.ajaibprotect

import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.widget.ImageView
import android.widget.ListView
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
        val textViewDeveloper = findViewById<TextView>(R.id.textViewDeveloper) // Tambahkan TextView untuk nama pengembang
        val appIcon = findViewById<ImageView>(R.id.appIcon)

        // Menampilkan informasi aplikasi
        textViewAppName.text = appInfo.loadLabel(packageManager).toString()
        textViewInstallerName.text = appInfo.packageName

        val file = File(appInfo.sourceDir)
        val sizeInKB = file.length() / 1024
        textViewSize.text = "$sizeInKB KB"

        textViewPath.text = appInfo.sourceDir

        // Menampilkan logo aplikasi
        val appLogo: Drawable = packageManager.getApplicationIcon(packageName)
        appIcon.setImageDrawable(appLogo)

        // Menampilkan nama pengembang jika tersedia
        val developerName = appInfo.metaData?.getString("developerName") ?: "Tidak diketahui"
        textViewDeveloper.text = "$developerName"

        // Menampilkan daftar izin aplikasi
        val permissions = getPermissionsList(packageName)
        val permissionListView = findViewById<ListView>(R.id.permissionListView)
        val permissionListAdapter = PermissionListAdapter(this, permissions)
        permissionListView.adapter = permissionListAdapter
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
}
