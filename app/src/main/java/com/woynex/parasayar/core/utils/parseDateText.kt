package com.woynex.parasayar.core.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


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

fun parseMonth(date: LocalDate): String {
    return DateTimeFormatter.ofPattern("LLLL").format(date)
}

@SuppressLint("SimpleDateFormat")
fun parseDayOfMonthDate(date: Long): String {
    val formatter = SimpleDateFormat("dd")
    val calendar: Calendar = Calendar.getInstance()
    calendar.timeInMillis = date
    return formatter.format(calendar.time)
}

@SuppressLint("SimpleDateFormat")
fun parseDayOfWeekDate(date: Long): String {
    val formatter = SimpleDateFormat("ccc")
    val calendar: Calendar = Calendar.getInstance()
    calendar.timeInMillis = date
    return formatter.format(calendar.time)
}

@SuppressLint("SimpleDateFormat")
fun parseMonthYearDate(date: Long): String {
    val formatter = SimpleDateFormat("LL.yyyy")
    val calendar: Calendar = Calendar.getInstance()
    calendar.timeInMillis = date
    return formatter.format(calendar.time)
}