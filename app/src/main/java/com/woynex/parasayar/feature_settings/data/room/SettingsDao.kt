package com.woynex.parasayar.feature_settings.data.room

import androidx.room.*
import com.woynex.parasayar.feature_settings.domain.model.Category
import com.woynex.parasayar.feature_settings.domain.model.CategoryWithSubCategories
import com.woynex.parasayar.feature_settings.domain.model.SubCategory
import kotlinx.coroutines.flow.Flow

@Dao
interface SettingsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(category: Category)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateCategory(category: Category)

    @Delete
    suspend fun deleteCategory(category: Category)

    @Query("SELECT * FROM category WHERE type=:type")
    fun getAllCategory(type: String): Flow<List<Category>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSubCategory(category: SubCategory)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateSubCategory(category: SubCategory)

    @Delete
    suspend fun deleteSubCategory(category: SubCategory)

    @Query("SELECT * FROM subcategory")
    fun getAllSubCategory(): Flow<List<SubCategory>>

    @Transaction
    @Query("SELECT * FROM category WHERE type=:type")
    fun getCategoryWithSubCategories(type: String): Flow<List<CategoryWithSubCategories>>

    @Transaction
    @Query("SELECT * FROM category WHERE id=:id")
    fun getCategoryWithSubCategoriesByIn(id: Int): Flow<CategoryWithSubCategories>
}