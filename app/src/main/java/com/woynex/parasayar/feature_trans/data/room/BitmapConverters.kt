package com.woynex.parasayar.feature_trans.data.room

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.room.TypeConverter
import java.io.ByteArrayOutputStream

object BitmapConverters {

    @TypeConverter
    fun fromBitmap(bitmap: Bitmap?): ByteArray?{
        val outputStream = ByteArrayOutputStream()
        bitmap?.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        return outputStream.toByteArray()
    }

    @TypeConverter
    fun toBitmap(byteArray: ByteArray?): Bitmap?{
        return byteArray?.size?.let { BitmapFactory.decodeByteArray(byteArray, 0, it) }
    }
}