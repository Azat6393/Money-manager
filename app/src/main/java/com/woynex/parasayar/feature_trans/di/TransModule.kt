package com.woynex.parasayar.feature_trans.di

import com.woynex.parasayar.core.data.room.CurrencyDao
import com.woynex.parasayar.core.data.room.ParaSayarDatabase
import com.woynex.parasayar.feature_trans.data.repository.BudgetRepositoryImpl
import com.woynex.parasayar.feature_trans.data.repository.TransRepositoryImpl
import com.woynex.parasayar.feature_trans.data.room.BudgetDao
import com.woynex.parasayar.feature_trans.data.room.TransDao
import com.woynex.parasayar.feature_trans.domain.repository.BudgetRepository
import com.woynex.parasayar.feature_trans.domain.repository.TransRepository
import com.woynex.parasayar.feature_trans.domain.use_case.budget.*
import com.woynex.parasayar.feature_trans.domain.use_case.trans.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TransModule {

    @Provides
    @Singleton
    fun provideTransDao(database: ParaSayarDatabase): TransDao {
        return database.transDao
    }

    @Provides
    @Singleton
    fun provideBudgetDao(database: ParaSayarDatabase): BudgetDao {
        return database.budgetDao
    }

    @Provides
    @Singleton
    fun provideTransRepo(dao: TransDao, currencyDao: CurrencyDao): TransRepository {
        return TransRepositoryImpl(dao, currencyDao)
    }

    @Provides
    @Singleton
    fun provideBudgetRepository(dao: BudgetDao): BudgetRepository {
        return BudgetRepositoryImpl(dao)
    }

    @Provides
    @Singleton
    fun provideBudgetUseCases(repo: BudgetRepository): BudgetUseCases {
        return BudgetUseCases(
            getAllBudget = GetAllBudget(repo),
            getAllCategoryWithSubcategoryBudget = GetAllCategoryWithSubcategoryBudget(repo),
            upsetCategoryBudget = UpsetCategoryBudget(repo),
            upsetSubcategoryBudget = UpsetSubcategoryBudget(repo)
        )
    }

    @Provides
    @Singleton
    fun provideDailyUseCases(repo: TransRepository): DailyUseCases {
        return DailyUseCases(
            getTransByMonth = GetTransByMonth(repo),
            getTransByYear = GetTransByYear(repo),
            getTransByDay = GetTransByDay(repo),
            getWeeklyTrans = GetWeeklyTrans(repo)
        )
    }

    @Provides
    @Singleton
    fun provideTransUseCases(repo: TransRepository): TransUseCases {
        return TransUseCases(
            insertTrans = InsertTrans(repo),
            deleteTrans = DeleteTrans(repo),
            updateTrans = UpdateTrans(repo),
            getTransById = GetTransById(repo),
            updateFeeAmount = UpdateFeeAmount(repo),
            deleteFeeTrans = DeleteFeeTrans(repo)
        )
    }
}