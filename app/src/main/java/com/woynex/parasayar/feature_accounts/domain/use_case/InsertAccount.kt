package com.woynex.parasayar.feature_accounts.domain.use_case

import com.woynex.parasayar.feature_accounts.data.room.toAccountDto
import com.woynex.parasayar.feature_accounts.domain.model.Account
import com.woynex.parasayar.feature_accounts.domain.repository.AccountsRepository
import javax.inject.Inject

class InsertAccount @Inject constructor(
    private val repo: AccountsRepository
) {
    suspend operator fun invoke(account: Account) = repo.insertAccount(account.toAccountDto())
}