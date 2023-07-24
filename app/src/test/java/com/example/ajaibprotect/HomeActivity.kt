package com.example.ajaibprotect

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.example.ajaibprotect.R

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
    }

    fun openSecuritySettings(view: View) {}
    fun openBackupAndResetSettings(view: View) {}
    fun openScanningHomeActivity(view: View) {}
}
