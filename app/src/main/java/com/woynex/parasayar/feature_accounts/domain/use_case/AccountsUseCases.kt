package com.woynex.parasayar.feature_accounts.domain.use_case

data class AccountsUseCases(
    val insertAccount: InsertAccount,
    val deleteAccount: DeleteAccount,
    val updateAccount: UpdateAccount,
    val getAccounts: GetAccounts
)
