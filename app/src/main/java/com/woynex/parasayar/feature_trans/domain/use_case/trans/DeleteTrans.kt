package com.woynex.parasayar.feature_trans.domain.use_case.trans

import com.woynex.parasayar.feature_trans.domain.model.Trans
import com.woynex.parasayar.feature_trans.domain.repository.TransRepository
import javax.inject.Inject

class DeleteTrans @Inject constructor(
    private val repo: TransRepository
) {
    suspend operator fun invoke(trans: Trans) = repo.deleteTrans(trans)
}