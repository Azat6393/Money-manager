package com.woynex.parasayar.feature_trans.domain.model

data class DailyTrans(
    val day: String,
    val dayOfWeek: String,
    val date: String,
    val income: Double,
    val expenses: Double,
    var arrayList: List<Trans>
)