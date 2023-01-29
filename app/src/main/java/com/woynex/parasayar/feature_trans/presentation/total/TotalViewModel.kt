package com.woynex.parasayar.feature_trans.presentation.total

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.woynex.parasayar.feature_trans.domain.model.Budget
import com.woynex.parasayar.feature_trans.domain.repository.BudgetRepository
import com.woynex.parasayar.feature_trans.domain.use_case.budget.BudgetUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class TotalViewModel @Inject constructor(
    private val budgetUseCases: BudgetUseCases,
    private val budgetRepo: BudgetRepository
) : ViewModel() {

    private val _budgets = MutableStateFlow<List<Budget>>(emptyList())
    val budgets = _budgets.asStateFlow()

    fun getBudgets(localDate: LocalDate, currency: String) {
        budgetUseCases.getAllBudget(
            month = localDate.monthValue,
            year = localDate.year,
            currency = currency
        ).onEach { budgetList ->
            _budgets.value = budgetList
        }.launchIn(viewModelScope)
    }
}