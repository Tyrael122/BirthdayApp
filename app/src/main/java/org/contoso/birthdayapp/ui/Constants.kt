package org.contoso.birthdayapp.ui

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Month

object Constants {
    object BirthdayDate {
        val targetBirthdayDate: LocalDateTime = LocalDate.of(2025, Month.APRIL, 28).atStartOfDay()
    }
}