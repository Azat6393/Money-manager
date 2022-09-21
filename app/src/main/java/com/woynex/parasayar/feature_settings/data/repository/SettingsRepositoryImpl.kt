package com.woynex.parasayar.feature_settings.data.repository

import com.woynex.parasayar.core.utils.CategoryTypes
import com.woynex.parasayar.feature_settings.data.room.SettingsDao
import com.woynex.parasayar.feature_settings.domain.model.Category
import com.woynex.parasayar.feature_settings.domain.model.CategoryWithSubCategories
import com.woynex.parasayar.feature_settings.domain.model.SubCategory
import com.woynex.parasayar.feature_settings.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SettingsRepositoryImpl @Inject constructor(
    private val dao: SettingsDao
) : SettingsRepository {
    override suspend fun insertCategory(category: Category) {
        dao.insertCategory(category)
    }

    override suspend fun updateCategory(category: Category) {
        dao.updateCategory(category)
    }

    override suspend fun deleteCategory(category: Category) {
        dao.deleteCategory(category)
    }

    override fun getAllExpenseCategory(): Flow<List<Category>> {
        return dao.getAllCategory(CategoryTypes.EXPENSE)
    }

    override fun getAllIncomeCategory(): Flow<List<Category>> {
        return dao.getAllCategory(CategoryTypes.INCOME)
    }

    override suspend fun insertSubCategory(category: SubCategory) {
        dao.insertSubCategory(category)
    }

    override suspend fun updateSubCategory(category: SubCategory) {
        dao.updateSubCategory(category)
    }

    override suspend fun deleteSubCategory(category: SubCategory) {
        dao.deleteSubCategory(category)
    }

    override fun getAllSubCategory(): Flow<List<SubCategory>> {
        return dao.getAllSubCategory()
    }

    override fun getExpenseCategoryWithSubCategories(): Flow<List<CategoryWithSubCategories>> {
        return dao.getCategoryWithSubCategories(CategoryTypes.EXPENSE)
    }

    override fun getIncomeCategoryWithSubCategories(): Flow<List<CategoryWithSubCategories>> {
        return dao.getCategoryWithSubCategories(CategoryTypes.INCOME)
    }
}