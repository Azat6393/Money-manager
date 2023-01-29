package com.woynex.parasayar.feature_trans.domain.use_case.trans

import com.woynex.parasayar.feature_trans.domain.repository.TransRepository
import javax.inject.Inject

class GetTransByMonth @Inject constructor(
    private val repo: TransRepository
) {
    operator fun invoke(month: Int, year: Int, currency: String) =
        repo.getTransByMonth(month, year, currency)
}