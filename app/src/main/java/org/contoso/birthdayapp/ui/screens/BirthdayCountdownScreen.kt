package org.contoso.birthdayapp.ui.screens;

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import org.contoso.birthdayapp.R
import org.contoso.birthdayapp.calculateTimeRemaining
import java.time.LocalDateTime

@Composable
fun BirthdayCountdownScreen(
    targetDate: LocalDateTime,
    currentDate: LocalDateTime,
    modifier: Modifier = Modifier
) {
    val timeRemaining = remember(targetDate, currentDate) {
        calculateTimeRemaining(targetDate, currentDate)
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(24.dp)
        ) {
            Text(
                text = stringResource(R.string.birthday_countdown_screen_headline),
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                TimeUnitDisplay(value = timeRemaining.days, unit = "Days")
                TimeUnitDisplay(value = timeRemaining.hours, unit = "Hours")
                TimeUnitDisplay(value = timeRemaining.minutes, unit = "Minutes")
                TimeUnitDisplay(value = timeRemaining.seconds, unit = "Seconds")
            }
        }
    }
}

@Composable
fun TimeUnitDisplay(value: Long, unit: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(60.dp)
                .background(MaterialTheme.colorScheme.primaryContainer, MaterialTheme.shapes.medium)
        ) {
            Text(
                text = value.toString(),
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = unit,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}