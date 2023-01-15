package com.woynex.parasayar.feature_accounts.domain.use_case

import com.woynex.parasayar.core.utils.TransTypes
import com.woynex.parasayar.feature_accounts.domain.model.Account
import com.woynex.parasayar.feature_accounts.domain.repository.AccountsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class GetAccounts @Inject constructor(
    private val repo: AccountsRepository
) {
    operator fun invoke(): Flow<List<Account>> = flow {
        repo.getAccountWithTrans().collect { accountWIthTrans ->
            val accountList = mutableListOf<Account>()
            accountWIthTrans.forEach { accountWithTrans ->
                var income = accountWithTrans.trans_list.filter { it.type == TransTypes.INCOME }
                    .sumOf { it.amount }
                var expence = accountWithTrans.trans_list.filter { it.type == TransTypes.EXPENSE }
                    .sumOf { it.amount }
                income += repo.getIncomeTransfers(accountWithTrans.accountDto.id!!).sumOf { it.amount }
                expence += repo.getExpenceTransfers(accountWithTrans.accountDto.id).sumOf { it.amount }
                accountList.add(
                    Account(
                        id = accountWithTrans.accountDto.id,
                        name = accountWithTrans.accountDto.name,
                        deposit = income,
                        withdrawal = expence,
                        group_id = accountWithTrans.accountDto.group_id,
                        group_name = accountWithTrans.accountDto.group_name
                    )
                )
            }
            emit(accountList)
        }
    }
}