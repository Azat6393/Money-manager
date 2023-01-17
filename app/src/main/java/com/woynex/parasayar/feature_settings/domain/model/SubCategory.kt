package com.woynex.parasayar.feature_settings.domain.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.woynex.parasayar.core.utils.RoomConstants
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = RoomConstants.SUB_CATEGORY_TABLE_NAME)
data class SubCategory(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val name: String,
    val category_name: String,
    val category_id: Int
): Parcelable
