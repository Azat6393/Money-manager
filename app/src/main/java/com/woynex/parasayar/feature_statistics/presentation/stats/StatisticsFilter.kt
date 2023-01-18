package com.woynex.parasayar.feature_statistics.presentation.stats

sealed class StatisticsFilter {
    object Weekly: StatisticsFilter()
    object Monthly: StatisticsFilter()
    object Annually: StatisticsFilter()
    object Period: StatisticsFilter()
}