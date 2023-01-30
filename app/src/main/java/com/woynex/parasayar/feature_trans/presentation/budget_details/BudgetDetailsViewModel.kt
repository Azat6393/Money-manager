package com.woynex.parasayar.feature_trans.presentation.budget_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.woynex.parasayar.core.utils.convertToDailyTransList
import com.woynex.parasayar.feature_trans.domain.model.BarStatisticsItem
import com.woynex.parasayar.feature_trans.domain.model.BudgetItem
import com.woynex.parasayar.feature_trans.domain.model.DailyTrans
import com.woynex.parasayar.feature_trans.domain.repository.TransRepository
import com.woynex.parasayar.feature_trans.domain.use_case.budget.BudgetUseCases
import com.woynex.parasayar.feature_trans.domain.use_case.trans.DailyUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class BudgetDetailsViewModel @Inject constructor(
    private val repo: TransRepository,
    private val budgetUseCases: BudgetUseCases,
    private val dailyUseCases: DailyUseCases
) : ViewModel() {

    private val _dailyTrans = MutableStateFlow<List<DailyTrans>>(emptyList())
    val dailyTrans = _dailyTrans.asStateFlow()

    private val _budgetInfo = MutableStateFlow<BudgetItem?>(null)
    val budgetInfo = _budgetInfo.asStateFlow()

    private val _barStatistics = MutableStateFlow<List<BarStatisticsItem>>(emptyList())
    val barStatistics = _barStatistics.asStateFlow()

    fun getTransByCategory(category_id: Int, currency: String, date: LocalDate) {
        println(category_id)
        repo.getTransByCategory(
            category_id = category_id,
            currency = currency,
            month = date.monthValue,
            year = date.year
        ).onEach {
            _dailyTrans.value = it.convertToDailyTransList()
        }.launchIn(viewModelScope)
    }

    fun getTransBySubcategory(subcategory_id: Int, currency: String, date: LocalDate) {
        repo.getTransBySubcategory(
            subcategory_id = subcategory_id,
            currency = currency,
            month = date.monthValue,
            year = date.year
        ).onEach {
            _dailyTrans.value = it.convertToDailyTransList()
        }.launchIn(viewModelScope)
    }

    fun getCategoryBudgetInfo(category_id: Int, date: LocalDate, currency: String) {
        budgetUseCases.getAllBudget(
            month = date.monthValue,
            year = date.year,
            currency = currency
        ).onEach { list ->
            val result = list.find { it.categoryBudget.category_id == category_id }
            if (result != null) {
                val budgetItem = BudgetItem(
                    name = result.categoryBudget.name,
                    budgetAmount = result.categoryBudget.budgetAmount,
                    expenses = result.categoryBudget.expenses,
                    percentage = result.categoryBudget.percentage,
                    currency = currency,
                    category_id = category_id,
                    category_name = result.categoryBudget.category_name
                )
                _budgetInfo.value = budgetItem
            } else {
                _budgetInfo.value = null
            }
        }.launchIn(viewModelScope)
    }

    fun getSubcategoryBudgetInfo(subcategory_id: Int, date: LocalDate, currency: String) {
        budgetUseCases.getAllBudget(
            month = date.monthValue,
            year = date.year,
            currency = currency
        ).onEach { list ->
            val budgetList =
                list.map { it1 ->
                    it1.subcategoryBudget.find { it.subcategory_id == subcategory_id }
                }
            val result = budgetList.find { it?.subcategory_id == subcategory_id }
            if (result != null) {
                _budgetInfo.value = result
            } else {
                _budgetInfo.value = null
            }
        }.launchIn(viewModelScope)
    }

    fun getBarStatistics(isCategory: Boolean, year: Int, id: Int, currency: String) {
        dailyUseCases.getBarStatistics(isCategory, id, year, currency).onEach {
            _barStatistics.value = it
        }.launchIn(viewModelScope)
    }
}