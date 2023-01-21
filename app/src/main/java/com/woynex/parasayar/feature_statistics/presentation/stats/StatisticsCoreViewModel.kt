package com.woynex.parasayar.feature_statistics.presentation.stats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.woynex.parasayar.core.domain.model.Currency
import com.woynex.parasayar.core.utils.SharedPreferencesHelper
import com.woynex.parasayar.feature_statistics.domain.use_case.StatisticsUseCases
import com.woynex.parasayar.feature_trans.domain.repository.TransRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class StatisticsCoreViewModel @Inject constructor(
    private val statisticsUseCases: StatisticsUseCases,
    private val repo: TransRepository,
    private val preferencesHelper: SharedPreferencesHelper
) : ViewModel() {

    private val _selectedDate = MutableStateFlow<LocalDate>(LocalDate.now())
    val selectedDate = _selectedDate.asStateFlow()

    private val _selectedStartDate = MutableStateFlow<LocalDate>(LocalDate.now().minusMonths(1))
    val selectedStartDate = _selectedStartDate.asStateFlow()

    private val _selectedEndDate = MutableStateFlow<LocalDate>(LocalDate.now())
    val selectedEndDate = _selectedEndDate.asStateFlow()

    private val _filter = MutableStateFlow<StatisticsFilter>(StatisticsFilter.Monthly)
    val filter = _filter.asStateFlow()

    private val _selectedCurrency = MutableStateFlow<Currency?>(null)
    val selectedCurrency = _selectedCurrency.asStateFlow()

    private val _currencies = MutableStateFlow<List<Currency>>(emptyList())
    val currencies = _currencies.asStateFlow()

    init {
        updateCurrency(preferencesHelper.getDefaultCurrency())
    }

    fun updateCurrency(currency: Currency) {
        _selectedCurrency.value = currency
    }

    fun updateDate(date: LocalDate) {
        _selectedDate.value = date
    }

    fun updateFilter(filter: StatisticsFilter){
        _filter.value = filter
    }

    fun updateStartDate(date: LocalDate) {
        _selectedStartDate.value = date
    }

    fun updateEndDate(date: LocalDate) {
        _selectedEndDate.value = date
    }
    fun getCurrencies() = viewModelScope.launch {
        repo.getAllCurrency().onEach {
            _currencies.value = it
        }.launchIn(viewModelScope)
    }
}