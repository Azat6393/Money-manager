package com.woynex.parasayar.feature_settings.presentation.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.woynex.parasayar.core.utils.CategoryTypes
import com.woynex.parasayar.feature_settings.domain.model.Category
import com.woynex.parasayar.feature_settings.domain.model.CategoryWithSubCategories
import com.woynex.parasayar.feature_settings.domain.repository.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val repo: SettingsRepository
) : ViewModel() {

    private val _categories = MutableStateFlow<List<CategoryWithSubCategories>>(emptyList())
    val categories = _categories.asStateFlow()

    fun getCategories(type: String) {
        if (type == CategoryTypes.EXPENSE) {
            repo.getExpenseCategoryWithSubCategories().onEach {
                _categories.value = it
            }.launchIn(viewModelScope)
        }
        if (type == CategoryTypes.INCOME) {
            repo.getIncomeCategoryWithSubCategories().onEach {
                _categories.value = it
            }.launchIn(viewModelScope)
        }
    }

    fun insertCategory(category: Category) = viewModelScope.launch {
        repo.insertCategory(category)
    }

    fun deleteCategory(category: CategoryWithSubCategories) = viewModelScope.launch {
        repo.deleteCategory(category.category)
        category.subCategoryList.forEach { subCategory ->
            repo.deleteSubCategory(subCategory)
        }
    }
}