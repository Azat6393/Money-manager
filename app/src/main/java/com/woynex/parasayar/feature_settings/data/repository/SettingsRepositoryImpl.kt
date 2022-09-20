package com.woynex.parasayar.feature_settings.data.repository

import com.woynex.parasayar.feature_settings.data.room.SettingsDao
import com.woynex.parasayar.feature_settings.domain.model.Category
import com.woynex.parasayar.feature_settings.domain.repository.SettingsRepository
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

    override fun getAllCategory(): List<Category> {
        return dao.getAllCategory()
    }
}