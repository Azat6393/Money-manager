package com.woynex.parasayar.feature_settings.domain.use_case

import com.woynex.parasayar.feature_settings.domain.repository.SettingsRepository
import javax.inject.Inject

class GetExpenseCategoryWithSubCategories @Inject constructor(
    private val repo: SettingsRepository
) {
    operator fun invoke() = repo.getExpenseCategoryWithSubCategories()
}