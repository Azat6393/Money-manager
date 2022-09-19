package com.woynex.parasayar.core.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
fun parseDateText(date: LocalDate): String {
    return DateTimeFormatter.ofPattern("LLL y").format(date)
}

@RequiresApi(Build.VERSION_CODES.O)
fun parseYear(date: LocalDate): String {
    return DateTimeFormatter.ofPattern("y").format(date)
}