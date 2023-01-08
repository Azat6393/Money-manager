package com.woynex.parasayar.feature_trans.domain.use_case

data class DailyUseCases(
    val getTransByMonth: GetTransByMonth,
    val getTransByYear: GetTransByYear,
    val getTransByDay: GetTransByDay,
    val getWeeklyTrans: GetWeeklyTrans
)
