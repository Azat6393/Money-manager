package com.woynex.parasayar.feature_statistics.domain.use_case

data class StatisticsUseCases(
    val getStatisticsTransByMonth: GetStatisticsTransByMonth,
    val getStatisticsTransByYear: GetStatisticsTransByYear,
    val getStatisticsTransByPeriod: GetStatisticsTransByPeriod
)
