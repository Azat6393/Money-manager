package com.woynex.parasayar.feature_statistics.domain.use_case

import com.woynex.parasayar.core.utils.convertToCategoryStatisticsList
import com.woynex.parasayar.feature_statistics.domain.model.CategoryStatistics
import com.woynex.parasayar.feature_statistics.domain.repository.StatisticsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetStatisticsTransByPeriod @Inject constructor(
    private val repo: StatisticsRepository
) {
    operator fun invoke(
        startDate: Long,
        endDate: Long,
        currency: String,
        type: String
    ): Flow<List<CategoryStatistics>> =
        flow {
            repo.getCategoryWithTransByPeriod(startDate, endDate, currency, type)
                .collect { trans_list ->
                    val groupByCategory = trans_list.groupBy { it.category }
                    emit(groupByCategory.convertToCategoryStatisticsList(currency))
                }
        }
}