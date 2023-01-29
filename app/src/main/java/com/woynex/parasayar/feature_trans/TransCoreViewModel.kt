package com.woynex.parasayar.feature_trans

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.woynex.parasayar.core.domain.model.Currency
import com.woynex.parasayar.core.utils.SharedPreferencesHelper
import com.woynex.parasayar.core.utils.convertToYearInfo
import com.woynex.parasayar.feature_trans.domain.model.YearInfo
import com.woynex.parasayar.feature_trans.domain.repository.TransRepository
import com.woynex.parasayar.feature_trans.domain.use_case.trans.DailyUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class TransCoreViewModel @Inject constructor(
    private val transUseCases: DailyUseCases,
    private val repo: TransRepository,
    private val preferencesHelper: SharedPreferencesHelper
) : ViewModel() {

    private val _yearInfo = MutableStateFlow<YearInfo?>(null)
    val yearInfo = _yearInfo.asStateFlow()

    private val _selectedDate = MutableStateFlow<LocalDate>(LocalDate.now())
    val selectedDate = _selectedDate.asStateFlow()

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

    fun getTransByYear() {
        transUseCases.getTransByMonth(
            _selectedDate.value.monthValue,
            _selectedDate.value.year,
            _selectedCurrency.value?.symbol ?: preferencesHelper.getDefaultCurrency().symbol
        )
            .onEach { result ->
                _yearInfo.value = result.convertToYearInfo()
            }.launchIn(viewModelScope)
    }

    fun getCurrencies() {
        repo.getAllCurrency().onEach {
            _currencies.value = it
        }.launchIn(viewModelScope)
    }
}