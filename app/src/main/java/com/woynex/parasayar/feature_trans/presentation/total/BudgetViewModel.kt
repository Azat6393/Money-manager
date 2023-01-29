package com.woynex.parasayar.feature_trans.presentation.total

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.woynex.parasayar.feature_settings.domain.model.CategoryWithSubCategories
import com.woynex.parasayar.feature_settings.domain.use_case.CategoryUseCases
import com.woynex.parasayar.feature_trans.domain.model.CategoryBudget
import com.woynex.parasayar.feature_trans.domain.model.CategoryWithSubcategoryBudget
import com.woynex.parasayar.feature_trans.domain.model.SubcategoryBudget
import com.woynex.parasayar.feature_trans.domain.repository.BudgetRepository
import com.woynex.parasayar.feature_trans.domain.use_case.budget.BudgetUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BudgetViewModel @Inject constructor(
    private val categoriesUseCases: CategoryUseCases,
    private val budgetUseCases: BudgetUseCases,
    private val budgetRepo: BudgetRepository
) : ViewModel() {

    private val _categoryWithSubCategories =
        MutableStateFlow<List<CategoryWithSubCategories>>(emptyList())
    val categoryWithSubCategories = _categoryWithSubCategories.asStateFlow()

    private val _categoryBudgets =
        MutableStateFlow<List<CategoryWithSubcategoryBudget>>(emptyList())
    val categoryBudgets = _categoryBudgets.asStateFlow()

    fun getCategoryBudgets(currency: String) = viewModelScope.launch {
        budgetUseCases.getAllCategoryWithSubcategoryBudget(currency).onEach {
            _categoryBudgets.value = it
        }.launchIn(viewModelScope)
    }

    fun deleteCategoryBudgets(categoryWithSubcategoryBudget: CategoryWithSubcategoryBudget) =
        viewModelScope.launch {
            categoryWithSubcategoryBudget.subcategoryBudgetList.forEach {
                budgetRepo.deleteSubcategoryBudget(it)
            }
            budgetRepo.deleteCategoryBudget(categoryWithSubcategoryBudget.categoryBudget)
        }

    fun deleteSubcategoryBudget(subcategoryBudget: SubcategoryBudget) = viewModelScope.launch {
        budgetRepo.deleteSubcategoryBudget(subcategoryBudget)
    }

    fun getExpenseCategories() {
        categoriesUseCases.getExpenseCategoryWithSubCategories().onEach {
            _categoryWithSubCategories.value = it
        }.launchIn(viewModelScope)
    }

    fun insertCategoryBudget(categoryBudget: CategoryBudget) = viewModelScope.launch {
        budgetUseCases.upsetCategoryBudget(categoryBudget = categoryBudget)
    }

    fun insertSubcategoryBudget(subcategoryBudget: SubcategoryBudget) = viewModelScope.launch {
        budgetUseCases.upsetSubcategoryBudget(subcategoryBudget)
    }
}