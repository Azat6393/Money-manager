package com.woynex.parasayar.feature_trans.domain.use_case.trans

import com.woynex.parasayar.feature_trans.domain.model.BarStatisticsItem
import com.woynex.parasayar.feature_trans.domain.repository.TransRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetBarStatistics @Inject constructor(
    private val repo: TransRepository
) {
    operator fun invoke(
        isCategory: Boolean,
        id: Int,
        year: Int,
        currency: String
    ): Flow<List<BarStatisticsItem>> = flow {
        val transList =
            if (isCategory) repo.getTransByCategoryYearly(
                category_id = id, currency = currency, year = year
            ).first()
            else repo.getTransBySubcategoryYearly(
                subcategory_id = id, currency = currency, year = year
            ).first()
        val list = arrayListOf<BarStatisticsItem>()
        for (index in 1 until 13) {
            val amount = transList.filter { it.month == index}.sumOf { it.amount }
            list.add(
                BarStatisticsItem(
                    month = index,
                    amount = amount
                )
            )
        }
        emit(list)
    }
}