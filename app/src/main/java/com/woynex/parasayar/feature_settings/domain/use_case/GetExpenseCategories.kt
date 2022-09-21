package com.woynex.parasayar.feature_settings.domain.use_case

import com.woynex.parasayar.feature_settings.domain.model.Category
import com.woynex.parasayar.feature_settings.domain.repository.SettingsRepository
import javax.inject.Inject

class GetExpenseCategories @Inject constructor(
    private val repository: SettingsRepository
) {
    operator fun invoke(category: Category) = repository.getAllExpenseCategory()
}