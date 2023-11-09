package com.example.ajaibprotect

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.File

class ReceivedApksActivity : AppCompatActivity() {

    companion object {
        const val REQUEST_PERMISSION = 101
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_incident_wa_apk)

        // Inisialisasi ListView
        val apksListView: ListView = findViewById(R.id.apksListView)

        // Memeriksa izin penyimpanan
        if (checkStoragePermission()) {
            // Jika izin disetujui, mendapatkan daftar file APK yang diterima
            val apkFiles = getReceivedApkFiles()

            // Membuat adapter dan mengaturnya ke dalam ListView
            val adapter = AppWaListAdapter(this, apkFiles)
            apksListView.adapter = adapter
        } else {
            // Jika izin belum diberikan, meminta izin penyimpanan
            requestStoragePermission()
        }
    }

    // Memeriksa apakah izin penyimpanan sudah diberikan atau belum
    private fun checkStoragePermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }

    // Meminta izin penyimpanan jika belum diberikan
    private fun requestStoragePermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
            REQUEST_PERMISSION
        )
    }

    // Mendapatkan daftar file APK yang diterima dari intent
    private fun getReceivedApkFiles(): List<File> {
        val receivedApks = mutableListOf<File>()

        // Mendapatkan intent dari aktivitas sebelumnya
        val intent: Intent? = intent

        // Memeriksa apakah intent memiliki aksi ACTION_VIEW
        if (intent?.action == Intent.ACTION_VIEW) {
            // Mendapatkan URI dari intent
            val uri: Uri? = intent.data

            // Memeriksa apakah URI tidak null
            if (uri != null) {
                // Mendapatkan path dari URI
                val filePath = uri.path

                // Memeriksa apakah path tidak null
                if (filePath != null) {
                    // Mendapatkan direktori induk dari file APK
                    val directory = File(filePath).parentFile

                    // Memeriksa apakah direktori tidak null dan merupakan direktori
                    if (directory != null && directory.isDirectory) {
                        // Menambahkan semua file APK ke dalam daftar receivedApks
                        receivedApks.addAll(directory.listFiles { file ->
                            file.isFile && file.extension.equals("apk", ignoreCase = true)
                        })
                    }
                }
            }
        }

        // Mengembalikan daftar file APK yang diterima
        return receivedApks
    }
}
