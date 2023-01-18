package com.woynex.parasayar.feature_statistics.di

import com.woynex.parasayar.core.data.room.ParaSayarDatabase
import com.woynex.parasayar.feature_statistics.data.repository.StatisticsRepositoryImpl
import com.woynex.parasayar.feature_statistics.data.room.StatisticsDao
import com.woynex.parasayar.feature_statistics.domain.repository.StatisticsRepository
import com.woynex.parasayar.feature_statistics.domain.use_case.GetExpensesTransByMonth
import com.woynex.parasayar.feature_statistics.domain.use_case.GetIncomeTransByMonth
import com.woynex.parasayar.feature_statistics.domain.use_case.StatisticsUseCases
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object StatisticsModule {

    @Singleton
    @Provides
    fun provideStatisticsDao(database: ParaSayarDatabase): StatisticsDao {
        return database.statistics
    }

    @Provides
    @Singleton
    fun provideStatisticsRepository(dao: StatisticsDao): StatisticsRepository {
        return StatisticsRepositoryImpl(dao)
    }

    @Provides
    @Singleton
    fun provideStatisticsUseCases(repo: StatisticsRepository): StatisticsUseCases {
        return StatisticsUseCases(
            getIncomeTransByMonth = GetIncomeTransByMonth(repo),
            getExpensesTransByMonth = GetExpensesTransByMonth(repo)
        )
    }
}