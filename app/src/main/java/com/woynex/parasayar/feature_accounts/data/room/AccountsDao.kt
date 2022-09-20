package com.woynex.parasayar.feature_accounts.data.room

import androidx.room.*
import com.woynex.parasayar.feature_accounts.domain.model.Account
import com.woynex.parasayar.feature_accounts.domain.model.AccountGroup
import kotlinx.coroutines.flow.Flow

@Dao
interface AccountsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAccount(account: Account)

    @Delete
    suspend fun deleteAccount(account: Account)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateAccount(account: Account)

    @Query("SELECT * FROM account")
    fun getAccounts(): Flow<List<Account>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAccountGroup(accountGroup: AccountGroup)

    @Delete
    suspend fun deleteAccountGroup(accountGroup: AccountGroup)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateAccountGroup(accountGroup: AccountGroup)

    @Query("SELECT * FROM account_group")
    fun getAccountGroups(): Flow<List<AccountGroup>>
}