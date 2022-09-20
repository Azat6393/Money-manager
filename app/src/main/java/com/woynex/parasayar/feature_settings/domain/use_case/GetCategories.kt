package com.woynex.parasayar.feature_settings.domain.use_case

import com.woynex.parasayar.feature_settings.domain.model.Category
import com.woynex.parasayar.feature_settings.domain.repository.SettingsRepository
import javax.inject.Inject

class GetCategories @Inject constructor(
    private val repository: SettingsRepository
) {
    suspend operator fun invoke(category: Category) = repository.getAllCategory()
}