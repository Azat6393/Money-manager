package com.woynex.parasayar.feature_accounts.domain.repository

import com.woynex.parasayar.feature_accounts.domain.model.AccountDto
import com.woynex.parasayar.feature_accounts.domain.model.AccountGroup
import com.woynex.parasayar.feature_accounts.domain.model.AccountWithTrans
import com.woynex.parasayar.feature_trans.domain.model.Trans
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface AccountsRepository {

    suspend fun insertAccount(account: AccountDto)

    suspend fun deleteAccount(account: AccountDto)

    suspend fun updateAccount(account: AccountDto)

    fun getAccounts(): Flow<List<AccountDto>>

    suspend fun insertAccountGroup(accountGroup: AccountGroup)

    suspend fun deleteAccountGroup(accountGroup: AccountGroup)

    suspend fun updateAccountGroup(accountGroup: AccountGroup)

    fun getAccountGroups(): Flow<List<AccountGroup>>


    suspend fun getAccountWithTrans(): Flow<List<AccountWithTrans>>

    suspend fun getIncomeTransfers(
        toAccountId: Int
    ): List<Trans>

    suspend fun getExpenceTransfers(
        accountId: Int
    ): List<Trans>

    fun getAllAccountsDto(): Flow<List<AccountDto>>

    fun getAccountsTrans(month: Int, year: Int, accountId: Int, currency: String): Flow<List<Trans>>

}