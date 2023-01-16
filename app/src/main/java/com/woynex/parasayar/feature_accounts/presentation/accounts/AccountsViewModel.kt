package com.woynex.parasayar.feature_accounts.presentation.accounts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.woynex.parasayar.core.domain.model.Currency
import com.woynex.parasayar.core.utils.SharedPreferencesHelper
import com.woynex.parasayar.feature_accounts.domain.model.Account
import com.woynex.parasayar.feature_accounts.domain.use_case.AccountsUseCases
import com.woynex.parasayar.feature_trans.domain.repository.TransRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountsViewModel @Inject constructor(
    private val accountsUseCases: AccountsUseCases,
    private val repo: TransRepository,
    private val preferencesHelper: SharedPreferencesHelper
) : ViewModel() {

    private val _accounts = MutableStateFlow<List<Account>>(emptyList())
    val accounts = _accounts.asStateFlow()

    private val _selectedCurrency = MutableStateFlow<Currency?>(null)
    val selectedCurrency = _selectedCurrency.asStateFlow()

    private val _currencies = MutableStateFlow<List<Currency>>(emptyList())
    val currencies = _currencies.asStateFlow()

    init {
        updateCurrency(preferencesHelper.getDefaultCurrency())
        getAccounts()
    }

    fun updateCurrency(currency: Currency) {
        _selectedCurrency.value = currency
    }

    fun getCurrencies() = viewModelScope.launch {
        repo.getAllCurrency().onEach {
            _currencies.value = it
        }.launchIn(viewModelScope)
    }

    fun getAccounts() {
        selectedCurrency.value?.symbol?.let { currency ->
            accountsUseCases.getAccounts(currency).onEach {
                _accounts.value = it
            }.launchIn(viewModelScope)
        }
    }
}