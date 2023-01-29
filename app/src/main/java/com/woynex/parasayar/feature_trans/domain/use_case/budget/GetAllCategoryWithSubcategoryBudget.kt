package com.woynex.parasayar.feature_trans.domain.use_case.budget

import com.woynex.parasayar.feature_trans.domain.model.CategoryWithSubcategoryBudget
import com.woynex.parasayar.feature_trans.domain.repository.BudgetRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetAllCategoryWithSubcategoryBudget @Inject constructor(
    private val repository: BudgetRepository
) {
    operator fun invoke(currency: String): Flow<List<CategoryWithSubcategoryBudget>> = flow {
        val resultOriginal = repository.getAllCategoryWithSubcategoryBudget(currency).first()
        val result = arrayListOf<CategoryWithSubcategoryBudget>()
        resultOriginal.forEach { categoryList ->
            val newList = categoryList.subcategoryBudgetList.filter { it.currency == currency }
            result.add(
                CategoryWithSubcategoryBudget(
                    categoryBudget = categoryList.categoryBudget,
                    subcategoryBudgetList = newList
                )
            )
        }
        val newList = arrayListOf<CategoryWithSubcategoryBudget>()
        result.forEach { categoryWithSubcategoryBudget ->
            if (categoryWithSubcategoryBudget.categoryBudget.amount == 0.00) {
                val amount = categoryWithSubcategoryBudget.subcategoryBudgetList.sumOf { it.amount }
                newList.add(
                    categoryWithSubcategoryBudget.copy(
                        categoryBudget = categoryWithSubcategoryBudget.categoryBudget.copy(amount = amount)
                    )
                )
            } else {
                newList.add(categoryWithSubcategoryBudget)
            }
        }
        emit(newList)
    }
}