package com.woynex.parasayar.feature_settings.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.woynex.parasayar.core.utils.RoomConstants

@Entity(tableName = RoomConstants.SUB_CATEGORY_TABLE_NAME)
data class SubCategory(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val name: String,
    val category_name: String
)
