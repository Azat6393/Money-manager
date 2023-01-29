package com.woynex.parasayar.feature_trans.domain.model

data class Budget(
    val categoryBudget: BudgetItem,
    val subcategoryBudget: List<BudgetItem>
)
