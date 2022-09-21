package com.woynex.parasayar.feature_settings.domain.use_case

import com.woynex.parasayar.feature_settings.domain.model.SubCategory
import com.woynex.parasayar.feature_settings.domain.repository.SettingsRepository
import javax.inject.Inject

class InsertSubCategory @Inject constructor(
    private val repository: SettingsRepository
) {
    suspend operator fun invoke(subcategory: SubCategory) =
        repository.insertSubCategory(subcategory)
}