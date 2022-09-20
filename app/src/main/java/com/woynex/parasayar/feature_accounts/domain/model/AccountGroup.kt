package com.woynex.parasayar.feature_accounts.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.woynex.parasayar.core.utils.RoomConstants

@Entity(tableName = RoomConstants.ACCOUNT_GROUP_TABLE_NAME)
data class AccountGroup(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val name: String
)
