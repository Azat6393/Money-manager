package com.woynex.parasayar.feature_accounts.domain.use_case

data class AccountGroupUseCases(
    val insertAccountGroup: InsertAccountGroup,
    val updateAccountGroup: UpdateAccountGroup,
    val deleteAccountGroup: DeleteAccountGroup,
    val getAccountGroups: GetAccountGroups
)
