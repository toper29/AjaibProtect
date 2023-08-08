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

        val apksListView: ListView = findViewById(R.id.apksListView)

        if (checkStoragePermission()) {
            val apkFiles = getReceivedApkFiles()
            val adapter = AppWaListAdapter(this, apkFiles)
            apksListView.adapter = adapter
        } else {
            requestStoragePermission()
        }
    }

    private fun checkStoragePermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestStoragePermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
            REQUEST_PERMISSION
        )
    }

    private fun getReceivedApkFiles(): List<File> {
        val receivedApks = mutableListOf<File>()

        val intent: Intent? = intent
        if (intent?.action == Intent.ACTION_VIEW) {
            val uri: Uri? = intent.data
            if (uri != null) {
                val filePath = uri.path
                if (filePath != null) {
                    val directory = File(filePath).parentFile
                    if (directory != null && directory.isDirectory) {
                        receivedApks.addAll(directory.listFiles { file ->
                            file.isFile && file.extension.equals("apk", ignoreCase = true)
                        })
                    }
                }
            }
        }

        return receivedApks
    }
}
