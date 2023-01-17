package com.woynex.parasayar.feature_settings.presentation.add_subcategory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.woynex.parasayar.core.utils.CategoryTypes
import com.woynex.parasayar.feature_settings.domain.model.Category
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
class AddSubcategoryViewModel @Inject constructor(
    private val repo: SettingsRepository
): ViewModel(){

    private val _categoryList = MutableStateFlow<List<Category>>(emptyList())
    val categoryList = _categoryList.asStateFlow()

    fun getCategories(type: String){
        if (type == CategoryTypes.INCOME){
            repo.getAllIncomeCategory().onEach {
                _categoryList.value = it
            }.launchIn(viewModelScope)
        }
        if (type == CategoryTypes.EXPENSE){
            repo.getAllExpenseCategory().onEach {
                _categoryList.value = it
            }.launchIn(viewModelScope)
        }
    }

    fun updateSubcategory(subcategory: SubCategory) = viewModelScope.launch{
        repo.updateSubCategory(subcategory)
    }

    fun addSubcategory(subcategory: SubCategory) = viewModelScope.launch {
        repo.insertSubCategory(subcategory)
    }
}