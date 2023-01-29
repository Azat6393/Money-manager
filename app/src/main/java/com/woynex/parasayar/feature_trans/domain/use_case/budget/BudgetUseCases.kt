package com.woynex.parasayar.feature_trans.domain.use_case.budget

data class BudgetUseCases(
    val getAllBudget: GetAllBudget,
    val getAllCategoryWithSubcategoryBudget: GetAllCategoryWithSubcategoryBudget,
    val upsetCategoryBudget: UpsetCategoryBudget,
    val upsetSubcategoryBudget: UpsetSubcategoryBudget
)