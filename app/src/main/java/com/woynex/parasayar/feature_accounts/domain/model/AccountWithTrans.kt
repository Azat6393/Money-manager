package com.woynex.parasayar.feature_accounts.domain.model

import androidx.room.Embedded
import androidx.room.Relation
import com.woynex.parasayar.feature_trans.domain.model.Trans

data class AccountWithTrans(
    @Embedded val accountDto: AccountDto,
    @Relation(
        parentColumn = "id",
        entityColumn = "account_id"
    )
    val trans_list: List<Trans>
)