package com.woynex.parasayar.feature_trans.domain.use_case

import com.woynex.parasayar.feature_trans.domain.repository.TransRepository
import javax.inject.Inject

class GetWeeklyTrans @Inject constructor(
    private val repo: TransRepository
) {
    operator fun invoke(month: Int, year: Int, currency: String) = repo.getTransByWeek(month, year, currency)
}