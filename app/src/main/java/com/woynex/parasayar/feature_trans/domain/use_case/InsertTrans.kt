package com.woynex.parasayar.feature_trans.domain.use_case

import com.woynex.parasayar.feature_trans.domain.model.Trans
import com.woynex.parasayar.feature_trans.domain.repository.TransRepository
import javax.inject.Inject

class InsertTrans @Inject constructor(
    private val repo: TransRepository
) {
    suspend operator fun invoke(trans: Trans) = repo.insertTrans(trans)
}