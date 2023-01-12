package com.woynex.parasayar.feature_trans.domain.use_case

import com.woynex.parasayar.feature_trans.domain.repository.TransRepository
import javax.inject.Inject

class DeleteFeeTrans @Inject constructor(
    private val repo: TransRepository
) {
    suspend operator fun invoke(transId: String) =
        repo.deleteFeeTrans(transId)
}