package com.woynex.parasayar.feature_accounts.presentation.accounts_trans

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.woynex.parasayar.core.domain.model.Currency
import com.woynex.parasayar.core.utils.SharedPreferencesHelper
import com.woynex.parasayar.core.utils.convertToDailyTransList
import com.woynex.parasayar.core.utils.convertToYearInfo
import com.woynex.parasayar.core.utils.convertToYearTrans
import com.woynex.parasayar.feature_accounts.domain.repository.AccountsRepository
import com.woynex.parasayar.feature_trans.domain.model.DailyTrans
import com.woynex.parasayar.feature_trans.domain.model.YearInfo
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
class AccountsTransViewModel @Inject constructor(
    private val repo: AccountsRepository,
    private val transRepo: TransRepository,
    private val preferencesHelper: SharedPreferencesHelper
) : ViewModel() {

    private val _transList = MutableStateFlow<List<DailyTrans>>(emptyList())
    val transList = _transList.asStateFlow()

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

    fun getTrans(accountId: Int) {
        repo.getAccountsTrans(
            _selectedDate.value.monthValue,
            _selectedDate.value.year , accountId,
            _selectedCurrency.value?.symbol ?: preferencesHelper.getDefaultCurrency().symbol
        ).onEach {
            _transList.value = it.convertToDailyTransList()
            _yearInfo.value = it.convertToYearInfo()
        }.launchIn(viewModelScope)
    }

    fun getCurrencies() = viewModelScope.launch {
        transRepo.getAllCurrency().onEach {
            _currencies.value = it
        }.launchIn(viewModelScope)
    }
}