package com.woynex.parasayar.core.presentation.currency

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.woynex.parasayar.core.data.repository.CurrencyRepository
import com.woynex.parasayar.core.domain.model.Currency
import com.woynex.parasayar.core.utils.fromJsonToCurrency
import com.woynex.parasayar.core.utils.getJsonFromAssets
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CurrenciesViewModel @Inject constructor(
    private val repo: CurrencyRepository
): ViewModel() {

    private val _currencies = MutableStateFlow<List<Currency>>(emptyList())
    val currencies = _currencies.asStateFlow()

    private val _allCurrencies = MutableStateFlow<List<Currency>?>(emptyList())
    val allCurrencies = _allCurrencies.asStateFlow()

    fun getCurrencies(context: Context) = viewModelScope.launch{
        val currencyList =
            getJsonFromAssets(context, "currency.json")?.fromJsonToCurrency()
        _allCurrencies.value = currencyList
    }

    fun getAllCurrencies(){
        repo.getAllCurrency().onEach {
            _currencies.value = it
        }.launchIn(viewModelScope)
    }

    fun insertCurrency(currency: Currency) = viewModelScope.launch {
        repo.insertCurrency(currency)
    }

    fun deleteCurrency(currency: Currency) = viewModelScope.launch {
        repo.deleteCurrency(currency)
    }
}