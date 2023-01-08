package com.woynex.parasayar.feature_trans.domain.use_case

import com.woynex.parasayar.feature_trans.domain.repository.TransRepository
import javax.inject.Inject

class GetTransByDay @Inject constructor(
    private val repo: TransRepository
) {
    operator fun invoke(day: Int, month: Int, year: Int) = repo.getTransByDay(day, month, year)
}