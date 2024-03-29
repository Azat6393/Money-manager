package com.woynex.parasayar.feature_statistics.data.repository

import com.woynex.parasayar.feature_statistics.data.room.StatisticsDao
import com.woynex.parasayar.feature_statistics.domain.repository.StatisticsRepository
import com.woynex.parasayar.feature_trans.domain.model.Trans
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class StatisticsRepositoryImpl @Inject constructor(
    private val dao: StatisticsDao
) : StatisticsRepository {

    override fun getCategoryWithTransMonthly(
        month: Int,
        year: Int,
        type: String,
        currency: String
    ): Flow<List<Trans>> {
        return dao.getCategoryWithTransMonthly(month, year, type, currency)
    }

    override fun getCategoryWithTransYear(
        year: Int,
        type: String,
        currency: String
    ): Flow<List<Trans>> {
        return dao.getCategoryWithTransYear(year, type, currency)
    }

    override fun getCategoryWithTransByPeriod(
        startDate: Long,
        endDate: Long,
        currency: String,
        type: String
    ): Flow<List<Trans>> {
        return dao.getCategoryWithTransPeriod(startDate, endDate, currency, type)
    }
}