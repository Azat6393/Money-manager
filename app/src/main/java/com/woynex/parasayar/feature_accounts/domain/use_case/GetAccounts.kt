package com.woynex.parasayar.feature_accounts.domain.use_case

import com.woynex.parasayar.feature_accounts.domain.model.Account
import com.woynex.parasayar.feature_accounts.domain.repository.AccountsRepository
import javax.inject.Inject

class GetAccounts @Inject constructor(
    private val repo: AccountsRepository
) {
    operator fun invoke() = repo.getAccounts()
}