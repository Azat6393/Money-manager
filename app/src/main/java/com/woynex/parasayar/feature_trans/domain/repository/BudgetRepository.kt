package com.woynex.parasayar.feature_trans.domain.repository

import androidx.room.Query
import com.woynex.parasayar.core.utils.TransTypes
import com.woynex.parasayar.feature_trans.domain.model.*
import kotlinx.coroutines.flow.Flow

interface BudgetRepository {
    suspend fun insertCategoryBudget(categoryBudget: CategoryBudget)
    suspend fun updateCategoryBudget(name: String, amount: Double)
    suspend fun deleteCategoryBudget(categoryBudget: CategoryBudget)
    suspend fun isCategoryBudgetExist(categoryName: String, currency: String): Boolean
    suspend fun insertSubcategoryBudget(subcategoryBudget: SubcategoryBudget)
    suspend fun updateSubcategoryBudget(name: String, amount: Double)
    suspend fun deleteSubcategoryBudget(subcategoryBudget: SubcategoryBudget)
    suspend fun isSubcategoryBudgetExist(categoryName: String, currency: String): Boolean
    fun getAllCategoryWithSubcategoryBudget(currency: String): Flow<List<CategoryWithSubcategoryBudget>>
    fun getCategoriesTrans(
        month: Int,
        year: Int,
        currency: String,
        categoryId: Int
    ): Flow<List<Trans>>
    fun getSubcategoriesTrans(
        month: Int,
        year: Int,
        currency: String,
        subcategoryId: Int
    ): Flow<List<Trans>>
}