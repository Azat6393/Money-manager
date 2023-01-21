package com.woynex.parasayar.feature_statistics.domain.model

data class CategoryStatistics(
    val category_name: String,
    val category_id: Int?,
    val total_amount: Double,
    val currency: String,
    val percentage: Int,
    val color: Int
)
