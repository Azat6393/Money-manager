package com.woynex.parasayar.feature_trans.domain.use_case.budget

import com.woynex.parasayar.feature_trans.domain.model.CategoryBudget
import com.woynex.parasayar.feature_trans.domain.repository.BudgetRepository
import javax.inject.Inject

class UpsetCategoryBudget @Inject constructor(
    private val repo: BudgetRepository
) {
    suspend operator fun invoke(categoryBudget: CategoryBudget) {
        val isCategoryBudgetExist =
            repo.isCategoryBudgetExist(categoryBudget.category_name, categoryBudget.currency)
        if (isCategoryBudgetExist) {
            repo.updateCategoryBudget(
                name = categoryBudget.category_name,
                amount = categoryBudget.amount
            )
        } else {
            repo.insertCategoryBudget(categoryBudget)
        }
    }
}