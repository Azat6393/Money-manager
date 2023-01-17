package com.woynex.parasayar.feature_settings.presentation.subcategory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.woynex.parasayar.feature_settings.domain.model.Category
import com.woynex.parasayar.feature_settings.domain.model.CategoryWithSubCategories
import com.woynex.parasayar.feature_settings.domain.model.SubCategory
import com.woynex.parasayar.feature_settings.domain.repository.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SubcategoryViewModel @Inject constructor(
    private val repo: SettingsRepository
) : ViewModel() {

    private val _subcategories = MutableStateFlow<CategoryWithSubCategories?>(null)
    val subcategories = _subcategories.asStateFlow()

    fun getCategories(id: Int) {
        repo.getCategoryWithSubCategoriesByIn(id).onEach {
            _subcategories.value = it
        }.launchIn(viewModelScope)
    }

    fun updateCategory(category: Category) = viewModelScope.launch {
        repo.updateCategory(category)
    }

    fun deleteSubcategory(subcategory: SubCategory) = viewModelScope.launch {
        repo.deleteSubCategory(category = subcategory)
    }
}