package com.example.ajaibprotect

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.File

class MyReceivedApksActivity : AppCompatActivity() {

    companion object {
        const val REQUEST_PERMISSION = 101
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_received_apks)

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

    // Mendapatkan daftar file APK yang diterima dari direktori unduhan
    private fun getReceivedApkFiles(): List<File> {
        val receivedApks = mutableListOf<File>()

        // Menggunakan direktori unduhan sebagai contoh
        val downloadDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)

        // Memeriksa apakah direktori unduhan ada
        if (downloadDirectory.isDirectory) {
            receivedApks.addAll(downloadDirectory.listFiles { file ->
                file.isFile && file.extension.equals("apk", ignoreCase = true)
            })
        }

        // Mengembalikan daftar file APK yang ditemukan
        return receivedApks
    }
}
