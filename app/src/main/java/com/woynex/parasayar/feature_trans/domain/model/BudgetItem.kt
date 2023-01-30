package com.woynex.parasayar.feature_trans.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class BudgetItem(
    val name: String,
    val budgetAmount: Double,
    val expenses: Double,
    val percentage: Int,
    val currency: String,
    val category_id: Int?,
    val category_name: String,
    val subcategory_id: Int? = null,
    val subcategory_name: String? = null
): Parcelable
