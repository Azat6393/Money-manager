package com.woynex.parasayar.feature_accounts.presentation.account_setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.woynex.parasayar.feature_accounts.data.room.toAccount
import com.woynex.parasayar.feature_accounts.domain.model.Account
import com.woynex.parasayar.feature_accounts.domain.repository.AccountsRepository
import com.woynex.parasayar.feature_accounts.domain.use_case.AccountsUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountSettingViewModel @Inject constructor(
    private val accountsUseCases: AccountsUseCases,
    private val repo: AccountsRepository
): ViewModel() {

    private val _accounts = MutableStateFlow<List<Account>>(emptyList())
    val accounts = _accounts.asStateFlow()

    fun deleteAccount(account: Account) = viewModelScope.launch {
        accountsUseCases.deleteAccount(account)
    }

    fun getAccounts() = viewModelScope.launch {
        repo.getAllAccountsDto().onEach { list ->
            _accounts.value = list.map { it.toAccount() }
        }.launchIn(viewModelScope)
    }
}