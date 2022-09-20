package com.woynex.parasayar.feature_accounts.di

import com.woynex.parasayar.core.data.room.ParaSayarDatabase
import com.woynex.parasayar.feature_accounts.data.repository.AccountsRepositoryImpl
import com.woynex.parasayar.feature_accounts.data.room.AccountsDao
import com.woynex.parasayar.feature_accounts.domain.repository.AccountsRepository
import com.woynex.parasayar.feature_accounts.domain.use_case.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AccountsModule {

    @Provides
    @Singleton
    fun provideAccountsDao(database: ParaSayarDatabase): AccountsDao {
        return database.accountsDao
    }

    @Provides
    @Singleton
    fun provideAccountsRepository(dao: AccountsDao): AccountsRepository {
        return AccountsRepositoryImpl(dao)
    }

    @Provides
    @Singleton
    fun provideAccountsUseCases(repo: AccountsRepository): AccountsUseCases {
        return AccountsUseCases(
            insertAccount = InsertAccount(repo),
            deleteAccount = DeleteAccount(repo),
            updateAccount = UpdateAccount(repo),
            getAccounts = GetAccounts(repo)
        )
    }
}