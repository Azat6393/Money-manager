package com.woynex.parasayar.feature_trans.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.woynex.parasayar.core.utils.RoomConstants

@Entity(tableName = RoomConstants.SUBCATEGORY_BUDGET_NAME)
data class SubcategoryBudget(
    @PrimaryKey
    val id: Int? = null,
    val category_id: Int,
    val category_name: String,
    val amount: Double,
    val currency: String,
    val subcategory_id: Int,
    val subcategory_name: String
)
