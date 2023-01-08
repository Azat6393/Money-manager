package com.woynex.parasayar.feature_trans.domain.model

import java.time.LocalDate

data class YearTrans(
    val date: LocalDate,
    val income: Double? = null,
    val expence: Double? = null,
    val total: Double? = null,
)
