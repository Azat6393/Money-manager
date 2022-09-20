package com.woynex.parasayar.feature_accounts.domain.use_case

import com.woynex.parasayar.feature_accounts.domain.model.Account
import com.woynex.parasayar.feature_accounts.domain.model.AccountGroup
import com.woynex.parasayar.feature_accounts.domain.repository.AccountsRepository
import javax.inject.Inject

class DeleteAccountGroup @Inject constructor(
    private val repo: AccountsRepository
) {
    suspend operator fun invoke(accountGroup: AccountGroup) = repo.deleteAccountGroup(accountGroup)
}