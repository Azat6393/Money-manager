package com.woynex.parasayar.feature_accounts.domain.repository

import com.woynex.parasayar.feature_accounts.domain.model.Account
import kotlinx.coroutines.flow.Flow

interface AccountsRepository {

    suspend fun insertAccount(account: Account)

    suspend fun deleteAccount(account: Account)

    suspend fun updateAccount(account: Account)

    fun getAccounts(): Flow<List<Account>>

}