package com.woynex.parasayar.feature_trans.domain.model

import java.time.LocalDate


data class WeekTrans(
    val startWeek: LocalDate,
    val endWeek: LocalDate,
    val income: Double? = null,
    val expense: Double? = null,
    val total: Double? = null
)