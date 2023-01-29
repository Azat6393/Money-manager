package com.woynex.parasayar.feature_trans.domain.use_case.budget

import com.woynex.parasayar.feature_trans.domain.model.CategoryBudget
import com.woynex.parasayar.feature_trans.domain.model.SubcategoryBudget
import com.woynex.parasayar.feature_trans.domain.repository.BudgetRepository
import javax.inject.Inject

class UpsetSubcategoryBudget @Inject constructor(
    private val repo: BudgetRepository
) {
    suspend operator fun invoke(subcategoryBudget: SubcategoryBudget) {
        val isCategoryBudgetExist =
            repo.isCategoryBudgetExist(subcategoryBudget.category_name, subcategoryBudget.currency)
        if (!isCategoryBudgetExist) {
            repo.insertCategoryBudget(
                CategoryBudget(
                    category_id = subcategoryBudget.category_id,
                    category_name = subcategoryBudget.category_name,
                    amount = 0.00,
                    currency = subcategoryBudget.currency
                )
            )
        }
        val isSubcategoryBudgetExist =
            repo.isSubcategoryBudgetExist(
                subcategoryBudget.subcategory_name,
                subcategoryBudget.currency
            )
        if (isSubcategoryBudgetExist) {
            repo.updateSubcategoryBudget(
                name = subcategoryBudget.subcategory_name,
                amount = subcategoryBudget.amount
            )
        } else {
            repo.insertSubcategoryBudget(subcategoryBudget)
        }
    }
}