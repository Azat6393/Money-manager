package com.woynex.parasayar.feature_trans.di

import com.woynex.parasayar.core.data.room.ParaSayarDatabase
import com.woynex.parasayar.feature_trans.data.repository.TransRepositoryImpl
import com.woynex.parasayar.feature_trans.data.room.TransDao
import com.woynex.parasayar.feature_trans.domain.repository.TransRepository
import com.woynex.parasayar.feature_trans.domain.use_case.*
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
    fun provideTransRepo(dao: TransDao): TransRepository {
        return TransRepositoryImpl(dao)
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