package org.contoso.birthdayapp.ui.notification

import android.app.ActivityManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.contoso.birthdayapp.R
import org.contoso.birthdayapp.calculateTimeRemaining
import org.contoso.birthdayapp.ui.Constants
import java.time.LocalDateTime

class CountdownForegroundService : Service() {

    companion object {
        fun isServiceRunning(context: Context): Boolean {
            val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            for (service in activityManager.getRunningServices(Int.MAX_VALUE)) {
                if (service.service.className == CountdownForegroundService::class.java.name) {
                    return true
                }
            }
            return false
        }
    }

    private lateinit var notificationHelper: NotificationHelper
    private var serviceJob: Job? = null

    override fun onCreate() {
        super.onCreate()
        notificationHelper = NotificationHelper(this)
        notificationHelper.createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (serviceJob != null && serviceJob!!.isActive) {
            return START_STICKY // already running
        }

        startForeground(
            NotificationHelper.NOTIFICATION_ID,
            notificationHelper.buildNotification("Starting...")
        )

        startUpdatingNotification()

        return START_STICKY
    }

    private fun startUpdatingNotification() {
        serviceJob = CoroutineScope(Dispatchers.Default).launch {
            while (true) {
                delay(1000L)

                val timeRemaining = calculateTimeRemaining(Constants.BirthdayDate.targetBirthdayDate, LocalDateTime.now())
                if (timeRemaining.isExpired) {
                    notificationHelper.updateNotificationToMakeDismissible(getString(R.string.countdown_notification_time_is_up))
                    break
                }

                notificationHelper.updateNotification(
                    getString(
                        R.string.countdown_notification,
                        timeRemaining.days,
                        timeRemaining.hours,
                        timeRemaining.minutes,
                        timeRemaining.seconds
                    ))
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceJob?.cancel()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
