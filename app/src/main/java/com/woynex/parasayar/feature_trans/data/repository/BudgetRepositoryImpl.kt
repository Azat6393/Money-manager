package com.woynex.parasayar.feature_trans.data.repository

import com.woynex.parasayar.feature_trans.data.room.BudgetDao
import com.woynex.parasayar.feature_trans.domain.model.*
import com.woynex.parasayar.feature_trans.domain.repository.BudgetRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class BudgetRepositoryImpl @Inject constructor(
    private val dao: BudgetDao
) : BudgetRepository {

    override suspend fun insertCategoryBudget(categoryBudget: CategoryBudget) {
        dao.insertCategoryBudget(categoryBudget)
    }

    override suspend fun updateCategoryBudget(name: String, amount: Double) {
        dao.updateCategoryBudget(name, amount)
    }

    override suspend fun deleteCategoryBudget(categoryBudget: CategoryBudget) {
        dao.deleteCategoryBudget(categoryBudget)
    }

    override suspend fun isCategoryBudgetExist(categoryName: String, currency: String): Boolean {
        return dao.isCategoryBudgetExist(categoryName, currency)
    }

    override suspend fun insertSubcategoryBudget(subcategoryBudget: SubcategoryBudget) {
        return dao.insertSubcategoryBudget(subcategoryBudget)
    }

    override suspend fun updateSubcategoryBudget(name: String, amount: Double) {
        dao.updateSubcategoryBudget(name, amount)
    }

    override suspend fun deleteSubcategoryBudget(subcategoryBudget: SubcategoryBudget) {
        dao.deleteSubcategoryBudget(subcategoryBudget)
    }

    override suspend fun isSubcategoryBudgetExist(categoryName: String, currency: String): Boolean {
        return dao.isSubcategoryBudgetExist(categoryName, currency)
    }

    override fun getAllCategoryWithSubcategoryBudget(currency: String): Flow<List<CategoryWithSubcategoryBudget>> {
        return dao.getAllCategoryWithSubcategoryBudget(currency)
    }

    override fun getCategoriesTrans(
        month: Int,
        year: Int,
        currency: String,
        categoryId: Int
    ): Flow<List<Trans>> {
        return dao.getCategoriesTrans(
            month = month,
            year = year,
            currency = currency,
            categoryId = categoryId
        )
    }

    override fun getSubcategoriesTrans(
        month: Int,
        year: Int,
        currency: String,
        subcategoryId: Int
    ): Flow<List<Trans>> {
        return dao.getSubcategoriesTrans(
            month = month,
            year = year,
            currency = currency,
            subcategoryId = subcategoryId
        )
    }


}