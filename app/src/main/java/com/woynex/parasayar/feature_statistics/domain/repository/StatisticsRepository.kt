package com.woynex.parasayar.feature_statistics.domain.repository

import com.woynex.parasayar.feature_trans.domain.model.Trans
import kotlinx.coroutines.flow.Flow

interface StatisticsRepository {

    fun getCategoryWithTransMonthly(
        month: Int,
        year: Int,
        type: String
    ): Flow<List<Trans>>

    fun getCategoryWithTransYear(
        year: Int,
        type: String
    ): Flow<List<Trans>>
}