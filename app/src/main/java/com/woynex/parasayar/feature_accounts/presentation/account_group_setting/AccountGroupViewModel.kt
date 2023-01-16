package com.woynex.parasayar.feature_accounts.presentation.account_group_setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.woynex.parasayar.feature_accounts.domain.model.AccountGroup
import com.woynex.parasayar.feature_accounts.domain.repository.AccountsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountGroupViewModel @Inject constructor(
    private val repo: AccountsRepository
) : ViewModel() {

    private val _accountGroups = MutableStateFlow<List<AccountGroup>>(emptyList())
    val accountGroup = _accountGroups.asStateFlow()

    init {
        getAccountGroups()
    }

    private fun getAccountGroups() {
        repo.getAccountGroups().onEach {
            _accountGroups.value = it
        }.launchIn(viewModelScope)
    }

    fun insertAccountGroup(accountGroup: AccountGroup) = viewModelScope.launch {
        repo.insertAccountGroup(accountGroup)
    }

    fun updateAccountGroup(accountGroup: AccountGroup) = viewModelScope.launch {
        repo.updateAccountGroup(accountGroup)
    }

    fun deleteAccountGroup(accountGroup: AccountGroup) = viewModelScope.launch {
        repo.deleteAccountGroup(accountGroup)
    }
}