package org.contoso.birthdayapp.ui.screens

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import org.contoso.birthdayapp.R
import org.contoso.birthdayapp.ui.notification.CountdownForegroundService

@Composable
fun StartServiceScreen(onStart: () -> Unit) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.countdown_notification_start_service_description),
            textAlign = TextAlign.Center,
            fontSize = 18.sp,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        Button(
            onClick = {
                startForegroundService(context)
                onStart()
            }
        ) {
            Text(text = stringResource(R.string.countdown_notification_start_service_button))
        }
    }
}

private fun startForegroundService(context: Context) {
    val intent = Intent(context, CountdownForegroundService::class.java)
    ContextCompat.startForegroundService(context, intent)
}
