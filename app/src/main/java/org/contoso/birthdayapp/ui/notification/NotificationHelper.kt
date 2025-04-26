package org.contoso.birthdayapp.ui.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import org.contoso.birthdayapp.MainActivity
import org.contoso.birthdayapp.R

class NotificationHelper(private val context: Context) {

    companion object {
        const val CHANNEL_ID = "your_channel_id"
        const val CHANNEL_NAME = "Your Channel Name"
        const val NOTIFICATION_ID = 1
    }

    private val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    private val contentPendingIntent: PendingIntent by lazy {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    fun createNotificationChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_LOW
        ).apply {
            description = "Channel description here"
        }
        notificationManager.createNotificationChannel(channel)
    }

    fun buildNotification(content: String): Notification {
        return NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle(context.getString(R.string.countdown_notification_title))
            .setContentText(content)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setOngoing(true)
            .setContentIntent(contentPendingIntent) // This makes the notification clickable
            .setAutoCancel(false) // Here makes it not dismissible
            .build()
    }

    fun updateNotification(content: String) {
        val notification = buildNotification(content)
        notificationManager.notify(NOTIFICATION_ID, notification)
    }

    fun updateNotificationToMakeDismissible(content: String) {
        val dismissibleNotification = buildDismissibleNotification(content)
        notificationManager.notify(NOTIFICATION_ID, dismissibleNotification)
    }

    private fun buildDismissibleNotification(content: String): Notification {
        return NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle(context.getString(R.string.countdown_notification_title))
            .setContentText(content)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentIntent(contentPendingIntent)
            .setOngoing(false) // Makes it not ongoing
            .setAutoCancel(true) // Makes it dismiss when clicked
            .build()
    }
}
