package com.woynex.parasayar.feature_trans.domain.use_case.trans

import com.woynex.parasayar.feature_trans.domain.repository.TransRepository
import javax.inject.Inject

class GetTransById @Inject constructor(
    private val repo: TransRepository
) {
    suspend operator fun invoke(id: Int) = repo.getTransById(id)
}