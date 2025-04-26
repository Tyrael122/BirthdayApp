package org.contoso.birthdayapp

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.coroutines.delay
import org.contoso.birthdayapp.ui.Constants
import org.contoso.birthdayapp.ui.components.VideoPlayer
import org.contoso.birthdayapp.ui.notification.CountdownForegroundService
import org.contoso.birthdayapp.ui.screens.BirthdayCountdownScreen
import org.contoso.birthdayapp.ui.screens.StartServiceScreen
import org.contoso.birthdayapp.ui.theme.BirthdayAppTheme
import java.time.Duration
import java.time.LocalDateTime

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), 0
                )
            }
        }

        setContent {
            BirthdayAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    var isServiceRunning by remember { mutableStateOf(CountdownForegroundService.isServiceRunning(context)) }

    val targetDate = remember { Constants.BirthdayDate.targetBirthdayDate }

    val currentDate by produceState(initialValue = LocalDateTime.now()) {
        while (true) {
            value = LocalDateTime.now()
            delay(1000) // Update every second
        }
    }

    val hasDatePassed = currentDate >= targetDate

    Box(modifier = modifier) {
        if (!isServiceRunning) {
            StartServiceScreen(onStart = {
                isServiceRunning = CountdownForegroundService.isServiceRunning(context)
            })
        } else {
            if (hasDatePassed) {
                VideoPlayer(modifier = Modifier.fillMaxSize())
            } else {
                BirthdayCountdownScreen(
                    targetDate = targetDate,
                    currentDate = currentDate,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

data class TimeRemaining(
    val days: Long,
    val hours: Long,
    val minutes: Long,
    val seconds: Long
) {
    val isExpired: Boolean
        get() = days <= 0 && hours <= 0 && minutes <= 0 && seconds <= 0
}

fun calculateTimeRemaining(targetDate: LocalDateTime, currentDate: LocalDateTime): TimeRemaining {
    val currentDateTime = LocalDateTime.now()

    if (currentDate >= targetDate) {
        return TimeRemaining(0, 0, 0, 0)
    }

    val duration = Duration.between(currentDateTime, targetDate)
    val days = duration.toDays()
    val hours = duration.toHours() % 24
    val minutes = duration.toMinutes() % 60
    val seconds = duration.seconds % 60

    return TimeRemaining(days, hours, minutes, seconds)
}