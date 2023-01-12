package com.woynex.parasayar.feature_trans.domain.use_case

data class TransUseCases(
    val insertTrans: InsertTrans,
    val deleteTrans: DeleteTrans,
    val updateTrans: UpdateTrans,
    val getTransById: GetTransById,
    val updateFeeAmount: UpdateFeeAmount,
    val deleteFeeTrans: DeleteFeeTrans
)