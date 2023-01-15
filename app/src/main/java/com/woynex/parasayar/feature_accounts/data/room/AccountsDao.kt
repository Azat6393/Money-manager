package com.woynex.parasayar.feature_accounts.data.room

import androidx.room.*
import com.woynex.parasayar.core.utils.TransTypes
import com.woynex.parasayar.feature_accounts.domain.model.AccountDto
import com.woynex.parasayar.feature_accounts.domain.model.AccountGroup
import com.woynex.parasayar.feature_accounts.domain.model.AccountWithTrans
import com.woynex.parasayar.feature_trans.domain.model.Trans
import kotlinx.coroutines.flow.Flow

@Dao
interface AccountsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAccount(account: AccountDto)

    @Delete
    suspend fun deleteAccount(account: AccountDto)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateAccount(account: AccountDto)

    @Query("SELECT * FROM account")
    fun getAccounts(): Flow<List<AccountDto>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAccountGroup(accountGroup: AccountGroup)

    @Delete
    suspend fun deleteAccountGroup(accountGroup: AccountGroup)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateAccountGroup(accountGroup: AccountGroup)

    @Query("SELECT * FROM account_group")
    fun getAccountGroups(): Flow<List<AccountGroup>>

    @Query("SELECT * FROM trans WHERE type=:income OR type=:expense")
    suspend fun getAllTrans(
        income: String = TransTypes.INCOME,
        expense: String = TransTypes.EXPENSE
    ): List<Trans>

    @Transaction
    @Query("SELECT * FROM account")
    fun getAccountWithTrans(): Flow<List<AccountWithTrans>>

    @Query("SELECT * FROM trans WHERE to_account_id=:toAccountId AND type=:type")
    suspend fun getIncomeTransfers(
        toAccountId: Int,
        type: String = TransTypes.TRANSFER
    ): List<Trans>

    @Query("SELECT * FROM trans WHERE account_id=:accountId AND type=:type")
    suspend fun getExpenceTransfers(
        accountId: Int,
        type: String = TransTypes.TRANSFER
    ): List<Trans>


}