package com.woynex.parasayar.core.utils

import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.UUID

fun generateUUID(): String{
    return UUID.randomUUID().toString()
}

fun millisecondToLocalDate(millis: Long): LocalDate{
    return Instant.ofEpochMilli(millis).atZone(ZoneId.systemDefault()).toLocalDate()
}

fun millisecondToLocalDateTime(millis: Long): LocalDateTime {
    return Instant.ofEpochMilli(millis).atZone(ZoneId.systemDefault()).toLocalDateTime()
}