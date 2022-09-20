package com.woynex.parasayar.feature_settings.data.room

import androidx.room.*
import com.woynex.parasayar.feature_settings.domain.model.Category

@Dao
interface SettingsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(category: Category)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateCategory(category: Category)

    @Delete
    suspend fun deleteCategory(category: Category)

    @Query("SELECT * FROM category")
    fun getAllCategory(): List<Category>
}