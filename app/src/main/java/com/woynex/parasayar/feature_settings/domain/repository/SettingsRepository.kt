package com.woynex.parasayar.feature_settings.domain.repository

import com.woynex.parasayar.feature_settings.domain.model.Category
import com.woynex.parasayar.feature_settings.domain.model.CategoryWithSubCategories
import com.woynex.parasayar.feature_settings.domain.model.SubCategory
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {

    suspend fun insertCategory(category: Category)

    suspend fun updateCategory(category: Category)

    suspend fun deleteCategory(category: Category)

    fun getAllExpenseCategory(): Flow<List<Category>>

    fun getAllIncomeCategory(): Flow<List<Category>>

    suspend fun insertSubCategory(category: SubCategory)

    suspend fun updateSubCategory(category: SubCategory)

    suspend fun deleteSubCategory(category: SubCategory)

    fun getAllSubCategory(): Flow<List<SubCategory>>

    fun getExpenseCategoryWithSubCategories(): Flow<List<CategoryWithSubCategories>>

    fun getIncomeCategoryWithSubCategories(): Flow<List<CategoryWithSubCategories>>

}