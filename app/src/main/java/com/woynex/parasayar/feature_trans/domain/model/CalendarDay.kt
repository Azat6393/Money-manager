package com.woynex.parasayar.feature_trans.domain.model

import java.time.LocalDate

data class CalendarDay(
    val date: LocalDate,
    val expense: Double? = null,
    val income: Double? = null,
    val total: Double? = null
)
