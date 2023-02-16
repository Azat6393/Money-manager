package com.woynex.parasayar.core.utils

import android.content.Context
import android.util.TypedValue
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
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

fun getJsonFromAssets(context: Context, fileName: String): String? {
    var jsonString = ""
    try {
        val inputStream = context.assets.open(fileName)
        val size = inputStream.available()
        val buffer = ByteArray(size)
        inputStream.read(buffer)
        inputStream.close()
        jsonString = String(buffer)
    } catch (e: Exception) {
        e.printStackTrace()
        return null
    }
    return jsonString
}

@ColorInt
fun Context.getColorFromAttr(
    @AttrRes attrColor: Int,
    typedValue: TypedValue = TypedValue(),
    resolveRefs: Boolean = true
): Int {
    theme.resolveAttribute(attrColor, typedValue, resolveRefs)
    return typedValue.data
}