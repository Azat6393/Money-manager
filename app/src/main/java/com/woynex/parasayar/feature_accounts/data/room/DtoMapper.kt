package com.woynex.parasayar.feature_accounts.data.room

import com.woynex.parasayar.feature_accounts.domain.model.Account
import com.woynex.parasayar.feature_accounts.domain.model.AccountDto

fun Account.toAccountDto(): AccountDto{
    return AccountDto(
        id = this.id,
        name = this.name,
        group_name = this.group_name,
        group_id = this.group_id
    )
}

fun AccountDto.toAccount(): Account{
    return Account(
        id = this.id,
        name = this.name,
        group_name = this.group_name,
        group_id = this.group_id,
        deposit = 0.00,
        withdrawal = 0.00
    )
}