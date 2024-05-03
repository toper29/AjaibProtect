// Untuk notifikasi ada unduhan atau uninstall aplikasi
package com.example.ajaibprotect

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class AppInstallReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            Intent.ACTION_PACKAGE_ADDED -> {
                val packageName = intent.data?.encodedSchemeSpecificPart
                Log.d("AppInstallReceiver", "Package added: $packageName")
                createNotificationChannel(context)
                showInstallNotification(context)
            }
            Intent.ACTION_PACKAGE_REMOVED -> {
                val packageName = intent.data?.encodedSchemeSpecificPart
                Log.d("AppInstallReceiver", "Package removed: $packageName")
                createNotificationChannel(context)
                showUninstallNotification(context)
            }
        }
    }

    private fun showInstallNotification(context: Context) {
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("Instalasi Berhasil")
            .setContentText("Instalasi aplikasi baru telah berhasil. Apakah Anda ingin memeriksa aplikasi yang baru diinstal?")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        with(NotificationManagerCompat.from(context)) {
            notify(NOTIFICATION_ID, builder.build())
        }
    }

    private fun showUninstallNotification(context: Context) {
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("Uninstall Berhasil")
            .setContentText("Uninstall aplikasi telah berhasil. Mohon periksa kembali apakah aplikasi tersebut masih ada dan hapus folder aplikasi yang baru dihapus dari perangkat Anda.")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        with(NotificationManagerCompat.from(context)) {
            notify(NOTIFICATION_ID, builder.build())
        }
    }

    private fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Install Uninstall Notifications"
            val descriptionText = "Notifications for successful install and uninstall actions"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    companion object {
        private const val CHANNEL_ID = "InstallUninstallChannel"
        private const val NOTIFICATION_ID = 1
    }
}

