package br.com.petguard.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import br.com.petguard.R
import br.com.petguard.data.database.Report

class NotificationService(private val context: Context) {
    companion object {
        const val CHANNEL_ID = "petguard_notifications"
        const val CHANNEL_NAME = "PetGuard Notifications"
    }

    init{
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notificações do PetGuard"
            }

            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun sendNotification(report: Report) {
        if (report.reportedByUserId != null) {
            val notification = NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle("✅ Denúncia Atendida")
                .setContentText("Sua denúncia em ${report.description} foi concluída pelos fiscais")
                .setSmallIcon(R.drawable.notification_icon)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .build()

            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.notify(report.id.toInt(), notification)
        }
    }
}