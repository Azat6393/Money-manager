package com.woynex.parasayar.feature_trans.data.room

import androidx.room.*
import com.woynex.parasayar.core.utils.TransTypes
import com.woynex.parasayar.feature_trans.domain.model.*
import kotlinx.coroutines.flow.Flow

@Dao
interface BudgetDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategoryBudget(categoryBudget: CategoryBudget)

    @Query("UPDATE category_budget SET amount=:amount WHERE category_name=:name")
    suspend fun updateCategoryBudget(name: String, amount: Double)

    @Delete
    suspend fun deleteCategoryBudget(categoryBudget: CategoryBudget)

    @Query("SELECT EXISTS(SELECT * FROM category_budget WHERE category_name=:categoryName AND currency=:currency)")
    suspend fun isCategoryBudgetExist(categoryName: String, currency: String): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSubcategoryBudget(subcategoryBudget: SubcategoryBudget)

    @Query("UPDATE subcategory_budget SET amount=:amount WHERE subcategory_name=:name")
    suspend fun updateSubcategoryBudget(name: String, amount: Double)

    @Delete
    suspend fun deleteSubcategoryBudget(subcategoryBudget: SubcategoryBudget)

    @Query("SELECT EXISTS(SELECT * FROM subcategory_budget WHERE subcategory_name=:subcategoryName AND currency=:currency)")
    suspend fun isSubcategoryBudgetExist(subcategoryName: String, currency: String): Boolean

    @Transaction
    @Query("SELECT * FROM category_budget WHERE currency=:currency")
    fun getAllCategoryWithSubcategoryBudget(currency: String): Flow<List<CategoryWithSubcategoryBudget>>

    @Query("SELECT * FROM trans WHERE month=:month AND year=:year AND currency=:currency AND category_id=:categoryId AND type=:type")
    fun getCategoriesTrans(
        month: Int,
        year: Int,
        currency: String,
        categoryId: Int,
        type: String = TransTypes.EXPENSE
    ): Flow<List<Trans>>

    @Query("SELECT * FROM trans WHERE month=:month AND year=:year AND currency=:currency AND subcategory_id=:subcategoryId AND type=:type")
    fun getSubcategoriesTrans(
        month: Int,
        year: Int,
        currency: String,
        subcategoryId: Int,
        type: String = TransTypes.EXPENSE
    ): Flow<List<Trans>>

}