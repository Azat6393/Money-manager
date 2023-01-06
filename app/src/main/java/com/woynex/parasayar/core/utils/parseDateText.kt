package com.woynex.parasayar.core.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun parseDateText(date: LocalDate): String {
    return DateTimeFormatter.ofPattern("LLL y").format(date)
}

fun parseYear(date: LocalDate): String {
    return DateTimeFormatter.ofPattern("y").format(date)
}

fun parseFullDate(date: LocalDateTime): String {
    return DateTimeFormatter.ofPattern("d/LL/y (E)  HH:mm").format(date)
}

fun parseTimeDate(date: LocalDateTime): String {
    return DateTimeFormatter.ofPattern("HH:mm").format(date)
}