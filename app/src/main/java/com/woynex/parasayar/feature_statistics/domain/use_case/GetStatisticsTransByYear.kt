package com.woynex.parasayar.feature_statistics.domain.use_case

import com.woynex.parasayar.core.utils.convertToCategoryStatisticsList
import com.woynex.parasayar.feature_statistics.domain.model.CategoryStatistics
import com.woynex.parasayar.feature_statistics.domain.repository.StatisticsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetStatisticsTransByYear @Inject constructor(
    private val repo: StatisticsRepository
) {
    operator fun invoke(year: Int, type: String, currency: String): Flow<List<CategoryStatistics>> =
        flow {
            repo.getCategoryWithTransYear(year, type, currency)
                .collect { trans_list ->
                    val groupByCategory = trans_list.groupBy { it.category }
                    emit(groupByCategory.convertToCategoryStatisticsList(currency))
                }
        }
}