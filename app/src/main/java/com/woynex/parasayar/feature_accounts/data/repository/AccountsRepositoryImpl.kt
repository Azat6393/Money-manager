package com.woynex.parasayar.feature_accounts.data.repository

import com.woynex.parasayar.feature_accounts.data.room.AccountsDao
import com.woynex.parasayar.feature_accounts.domain.model.Account
import com.woynex.parasayar.feature_accounts.domain.repository.AccountsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AccountsRepositoryImpl @Inject constructor(
    private val dao: AccountsDao
) : AccountsRepository {
    override suspend fun insertAccount(account: Account) {
        dao.insertAccount(account)
    }

    override suspend fun deleteAccount(account: Account) {
        dao.deleteAccount(account)
    }

    override suspend fun updateAccount(account: Account) {
        dao.updateAccount(account)
    }

    override fun getAccounts(): Flow<List<Account>> {
        return dao.getAccounts()
    }
}