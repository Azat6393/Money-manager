package com.woynex.parasayar.feature_accounts.data.repository

import com.woynex.parasayar.feature_accounts.data.room.AccountsDao
import com.woynex.parasayar.feature_accounts.domain.model.Account
import com.woynex.parasayar.feature_accounts.domain.model.AccountDto
import com.woynex.parasayar.feature_accounts.domain.model.AccountGroup
import com.woynex.parasayar.feature_accounts.domain.model.AccountWithTrans
import com.woynex.parasayar.feature_accounts.domain.repository.AccountsRepository
import com.woynex.parasayar.feature_trans.domain.model.Trans
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AccountsRepositoryImpl @Inject constructor(
    private val dao: AccountsDao
) : AccountsRepository {
    override suspend fun insertAccount(account: AccountDto) {
        dao.insertAccount(account)
    }

    override suspend fun deleteAccount(account: AccountDto) {
        dao.deleteAccount(account)
    }

    override suspend fun updateAccount(account: AccountDto) {
        dao.updateAccount(account)
    }

    override fun getAccounts(): Flow<List<AccountDto>> {
        return dao.getAccounts()
    }

    override suspend fun insertAccountGroup(accountGroup: AccountGroup) {
        dao.insertAccountGroup(accountGroup)
    }

    override suspend fun deleteAccountGroup(accountGroup: AccountGroup) {
        dao.deleteAccountGroup(accountGroup)
    }

    override suspend fun updateAccountGroup(accountGroup: AccountGroup) {
        dao.updateAccountGroup(accountGroup)
    }

    override fun getAccountGroups(): Flow<List<AccountGroup>> {
        return dao.getAccountGroups()
    }

    override suspend fun getAccountWithTrans(): Flow<List<AccountWithTrans>> {
        return dao.getAccountWithTrans()
    }

    override suspend fun getIncomeTransfers(toAccountId: Int): List<Trans> {
        return dao.getIncomeTransfers(toAccountId)
    }

    override suspend fun getExpenceTransfers(accountId: Int): List<Trans> {
        return dao.getExpenceTransfers(accountId)
    }
}