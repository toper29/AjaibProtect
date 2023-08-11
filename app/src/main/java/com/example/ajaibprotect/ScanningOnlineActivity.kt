@file:Suppress("DEPRECATION")

package com.example.ajaibprotect

import android.content.pm.ApplicationInfo
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

class ScanningOnlineActivity : AppCompatActivity() {

    private val apiKey = "037c377e63835ae36a3c1b3ebea83aa94dc7461abeb27ff9ea354681c940aa1e"
    private val client = OkHttpClient()

    private lateinit var appInfo: ApplicationInfo
    private lateinit var resultTextView: TextView
    private lateinit var sb: StringBuilder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scanning_online)

        appInfo = intent.getParcelableExtra("appInfo") ?: ApplicationInfo()
        resultTextView = findViewById(R.id.resultTextView)
        sb = StringBuilder()

        performOnlineScan()
    }

    private fun performOnlineScan() {
        val request = Request.Builder()
            .url("https://www.virustotal.com/api/v3/files/{id}/analyse")
            .addHeader("x-apikey", apiKey)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    resultTextView.text = "Scan failed: ${e.message}"
                }
            }

            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string()

                // Print the response body for debugging
                println("Response Body: $responseBody")

                try {
                    val jsonResponse = JSONObject(responseBody)
                    val positives = if (jsonResponse.has("positives")) jsonResponse.getInt("positives") else 0
                    val total = if (jsonResponse.has("total")) jsonResponse.getInt("total") else 0

                    runOnUiThread {
                        sb.append("Scan for ${appInfo.loadLabel(packageManager)}:\n")
                        sb.append("Detected: $positives / $total\n")
                        sb.append("\n")
                        resultTextView.text = sb.toString()
                    }
                } catch (e: JSONException) {
                    runOnUiThread {
                        resultTextView.text = "Error parsing JSON response"
                    }
                }
            }
        })
    }
}
