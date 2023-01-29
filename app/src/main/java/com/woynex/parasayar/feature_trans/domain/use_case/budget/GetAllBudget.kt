package com.woynex.parasayar.feature_trans.domain.use_case.budget

import com.woynex.parasayar.feature_trans.domain.model.Budget
import com.woynex.parasayar.feature_trans.domain.model.BudgetItem
import com.woynex.parasayar.feature_trans.domain.repository.BudgetRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetAllBudget @Inject constructor(
    private val repo: BudgetRepository
) {
    operator fun invoke(month: Int, year: Int, currency: String): Flow<List<Budget>> = flow {
        val categoryWithSubcategoryBudget =
            repo.getAllCategoryWithSubcategoryBudget(currency).first()
        val budgetList = arrayListOf<Budget>()
        categoryWithSubcategoryBudget.forEach { categoryWithSubcategoryBudget ->
            val categoryExpenses = repo.getCategoriesTrans(
                month = month,
                year = year,
                currency = currency,
                categoryId = categoryWithSubcategoryBudget.categoryBudget.category_id
            ).first()
            val categoryBudgetAmount =
                if (categoryWithSubcategoryBudget.categoryBudget.amount == 0.00) {
                    categoryWithSubcategoryBudget.subcategoryBudgetList.sumOf { it.amount }
                } else categoryWithSubcategoryBudget.categoryBudget.amount
            val categoryBudgetExpenses = categoryExpenses.sumOf { it.amount }
            val categoryPercentage = (categoryBudgetExpenses / categoryBudgetAmount) * 100
            val categoryBudget = BudgetItem(
                name = categoryWithSubcategoryBudget.categoryBudget.category_name,
                budgetAmount = categoryBudgetAmount,
                expenses = categoryBudgetExpenses,
                percentage = categoryPercentage.toInt(),
                currency = currency
            )
            val subCategoryBudgetList = arrayListOf<BudgetItem>()
            categoryWithSubcategoryBudget.subcategoryBudgetList.forEach { subcategoryBudget ->
                val subcategoryExpenses = repo.getSubcategoriesTrans(
                    month = month,
                    year = year,
                    currency = currency,
                    subcategoryId = subcategoryBudget.subcategory_id
                ).first()
                val subcategoryBudgetAmount = subcategoryBudget.amount
                val subcategoryBudgetExpenses = subcategoryExpenses.sumOf { it.amount }
                val subcategoryPercentage = (categoryBudgetExpenses / categoryBudgetAmount) * 100
                val subcategoryBudgetItem = BudgetItem(
                    name = subcategoryBudget.subcategory_name,
                    budgetAmount = subcategoryBudgetAmount,
                    expenses = subcategoryBudgetExpenses,
                    percentage = subcategoryPercentage.toInt(),
                    currency = currency
                )
                subCategoryBudgetList.add(subcategoryBudgetItem)
            }
            budgetList.add(
                Budget(
                    categoryBudget = categoryBudget,
                    subcategoryBudget = subCategoryBudgetList.toList()
                )
            )
        }
        emit(budgetList)
    }
}