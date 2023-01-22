package com.woynex.parasayar.feature_accounts.presentation.add_account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.woynex.parasayar.feature_accounts.domain.model.Account
import com.woynex.parasayar.feature_accounts.domain.model.AccountGroup
import com.woynex.parasayar.feature_accounts.domain.use_case.AccountGroupUseCases
import com.woynex.parasayar.feature_accounts.domain.use_case.AccountsUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddAccountViewModel @Inject constructor(
    private val accountsUseCases: AccountsUseCases,
    private val accountGroupUseCases: AccountGroupUseCases
) : ViewModel() {

    private val _accountGroups = MutableStateFlow<List<AccountGroup>>(emptyList())
    val accountGroup = _accountGroups.asStateFlow()

    init {
        getAccountGroups()
    }

    private fun getAccountGroups() {
        accountGroupUseCases.getAccountGroups().onEach {
            _accountGroups.value = it
        }.launchIn(viewModelScope)
    }

    fun addAccount(account: Account) = viewModelScope.launch {
        accountsUseCases.insertAccount(account)
    }

    fun updateAccount(account: Account) = viewModelScope.launch {
        accountsUseCases.updateAccount(account)
    }
}