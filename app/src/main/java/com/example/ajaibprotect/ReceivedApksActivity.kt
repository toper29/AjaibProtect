package com.example.ajaibprotect

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity

class ReceivedApksActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_incident_wa_apk)

        val apksListView: ListView = findViewById(R.id.apksListView)
        val apkFiles = getReceivedApkFiles()

        val apkNames = apkFiles.map { it.lastPathSegment }
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, apkNames)
        apksListView.adapter = adapter

        apksListView.setOnItemClickListener { _, _, position, _ ->
            // Melakukan sesuatu ketika item dalam daftar di klik
            val uri = apkFiles[position]
            // Anda dapat menggunakan URI ini untuk melakukan tindakan lebih lanjut jika diperlukan
            // Sebagai contoh, Anda dapat memeriksa APK untuk malware atau melakukan tindakan keamanan lainnya
        }
    }

    private fun getReceivedApkFiles(): List<Uri> {
        val intent: Intent? = intent
        val receivedApks = mutableListOf<Uri>()

        if (intent?.action == Intent.ACTION_VIEW) {
            val uri: Uri? = intent.data
            if (uri != null) {
                receivedApks.add(uri)
            }
        }

        return receivedApks
    }
}
