package com.woynex.parasayar.feature_trans.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.woynex.parasayar.core.utils.RoomConstants

@Entity(tableName = RoomConstants.TRANS_TABLE_NAME)
data class Trans(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val amount: Double,
    val category: String,
    val account_name: String,
    val subcategory: String,
    val note: String,
    val description: String,
    val photo: String,
    val currency: String,
    val type: String,
    val date_in_millis: Long,
    val day: Int,
    val month: Int,
    val year: Int,
    val time: String
)
