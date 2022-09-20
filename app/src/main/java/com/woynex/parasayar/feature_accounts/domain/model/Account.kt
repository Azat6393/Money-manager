package com.woynex.parasayar.feature_accounts.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.woynex.parasayar.core.utils.RoomConstants

@Entity(tableName = RoomConstants.ACCOUNT_TABLE_NAME)
data class Account(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val name: String,
    val deposit: Double,
    val withdrawal: Double,
    val group_name: String
)