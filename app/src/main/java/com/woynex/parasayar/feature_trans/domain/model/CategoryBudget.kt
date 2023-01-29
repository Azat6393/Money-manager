package com.woynex.parasayar.feature_trans.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.woynex.parasayar.core.utils.RoomConstants

@Entity(tableName = RoomConstants.CATEGORY_BUDGET_NAME)
data class CategoryBudget(
    @PrimaryKey
    val id: Int? = null,
    val category_id: Int,
    val category_name: String,
    val amount: Double,
    val currency: String
)
