package com.woynex.parasayar.feature_statistics.domain.use_case

data class StatisticsUseCases(
    val getIncomeTransByMonth: GetIncomeTransByMonth,
    val getExpensesTransByMonth: GetExpensesTransByMonth
)
