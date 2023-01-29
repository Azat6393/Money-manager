package com.woynex.parasayar.feature_trans.domain.use_case.trans

import com.woynex.parasayar.feature_trans.domain.repository.TransRepository
import javax.inject.Inject

class UpdateFeeAmount @Inject constructor(
    private val repo: TransRepository
) {
    suspend operator fun invoke(amount: Double, transId: String) =
        repo.updateFeeAmount(amount, transId)
}