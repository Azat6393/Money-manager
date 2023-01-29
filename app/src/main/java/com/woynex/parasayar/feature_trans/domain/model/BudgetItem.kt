package com.woynex.parasayar.feature_trans.domain.model

data class BudgetItem(
    val name: String,
    val budgetAmount: Double,
    val expenses: Double,
    val percentage: Int,
    val currency: String
)
