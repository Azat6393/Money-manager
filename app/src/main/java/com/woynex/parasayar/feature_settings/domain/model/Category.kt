package com.woynex.parasayar.feature_settings.domain.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.woynex.parasayar.core.utils.RoomConstants
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = RoomConstants.CATEGORY_TABLE_NAME)
data class Category(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val name: String,
    val type: String,
): Parcelable
