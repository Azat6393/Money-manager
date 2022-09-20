package com.woynex.parasayar.feature_settings.domain.repository

import com.woynex.parasayar.feature_settings.domain.model.Category

interface SettingsRepository {

    suspend fun insertCategory(category: Category)

    suspend fun updateCategory(category: Category)

    suspend fun deleteCategory(category: Category)

    fun getAllCategory(): List<Category>
}