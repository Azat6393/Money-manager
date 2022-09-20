package com.woynex.parasayar.feature_accounts.domain.repository

import androidx.room.*
import com.woynex.parasayar.feature_accounts.domain.model.Account
import com.woynex.parasayar.feature_accounts.domain.model.AccountGroup
import kotlinx.coroutines.flow.Flow

interface AccountsRepository {

    suspend fun insertAccount(account: Account)

    suspend fun deleteAccount(account: Account)

    suspend fun updateAccount(account: Account)

    fun getAccounts(): Flow<List<Account>>

    suspend fun insertAccountGroup(accountGroup: AccountGroup)

    suspend fun deleteAccountGroup(accountGroup: AccountGroup)

    suspend fun updateAccountGroup(accountGroup: AccountGroup)

    fun getAccountGroups(): Flow<List<AccountGroup>>
}