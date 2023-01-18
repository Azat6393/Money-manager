package com.woynex.parasayar.feature_statistics.domain.use_case

import com.woynex.parasayar.core.utils.TransTypes
import com.woynex.parasayar.feature_statistics.domain.repository.StatisticsRepository
import com.woynex.parasayar.feature_trans.domain.model.Trans
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetExpensesTransByMonth @Inject constructor(
    private val repo: StatisticsRepository
) {
    operator fun invoke(month: Int, year: Int): Flow<Map<String?, List<Trans>>> = flow {
        repo.getCategoryWithTransMonthly(month, year, TransTypes.EXPENSE).collect { trans_list ->
            val groupByCategory = trans_list.groupBy { it.category }
            emit(groupByCategory)
        }
    }
}