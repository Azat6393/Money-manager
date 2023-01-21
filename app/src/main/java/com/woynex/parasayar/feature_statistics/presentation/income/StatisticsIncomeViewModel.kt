package com.woynex.parasayar.feature_statistics.presentation.income

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.woynex.parasayar.core.utils.TransTypes
import com.woynex.parasayar.core.utils.toMillisecond
import com.woynex.parasayar.feature_statistics.domain.model.CategoryStatistics
import com.woynex.parasayar.feature_statistics.domain.use_case.StatisticsUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class StatisticsIncomeViewModel @Inject constructor(
    private val statisticsUseCases: StatisticsUseCases
) : ViewModel() {

    private val _categoryStatistics = MutableStateFlow<List<CategoryStatistics>>(emptyList())
    val categoryStatistics = _categoryStatistics.asStateFlow()

    fun getWeeklyTrans(startDate: LocalDate, endDate: LocalDate, currency: String) {
        statisticsUseCases.getStatisticsTransByPeriod(
            startDate = startDate.toMillisecond(),
            endDate = endDate.toMillisecond(),
            currency = currency,
            type = TransTypes.INCOME
        ).onEach {
            _categoryStatistics.value = it
        }.launchIn(viewModelScope)
    }

    fun getMonthlyTrans(date: LocalDate, currency: String) {
        statisticsUseCases.getStatisticsTransByMonth(
            month = date.monthValue,
            year = date.year,
            type = TransTypes.INCOME,
            currency = currency
        ).onEach {
            _categoryStatistics.value = it
        }.launchIn(viewModelScope)
    }

    fun getAnnuallyTrans(date: LocalDate, currency: String) {
        statisticsUseCases.getStatisticsTransByYear(
            year = date.year,
            type = TransTypes.INCOME,
            currency = currency
        ).onEach {
            _categoryStatistics.value = it
        }.launchIn(viewModelScope)
    }

    fun getPeriodTrans(startDate: LocalDate, endDate: LocalDate, currency: String) {
        statisticsUseCases.getStatisticsTransByPeriod(
            startDate = startDate.toMillisecond(),
            endDate = endDate.toMillisecond(),
            currency = currency,
            type = TransTypes.INCOME
        ).onEach {
            _categoryStatistics.value = it
        }.launchIn(viewModelScope)
    }
}