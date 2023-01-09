package com.woynex.parasayar.feature_trans.presentation.trans_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.woynex.parasayar.core.utils.Resource
import com.woynex.parasayar.core.utils.TransTypes
import com.woynex.parasayar.feature_accounts.domain.model.Account
import com.woynex.parasayar.feature_accounts.domain.use_case.AccountsUseCases
import com.woynex.parasayar.feature_settings.domain.model.CategoryWithSubCategories
import com.woynex.parasayar.feature_settings.domain.use_case.CategoryUseCases
import com.woynex.parasayar.feature_trans.domain.model.Trans
import com.woynex.parasayar.feature_trans.domain.use_case.TransUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransDetailsViewModel @Inject constructor(
    private val accountsUseCases: AccountsUseCases,
    private val transUseCases: TransUseCases,
    private val categoriesUseCases: CategoryUseCases
) : ViewModel() {

    private val _accounts = MutableStateFlow<List<Account>>(emptyList())
    val accounts = _accounts.asStateFlow()

    private val _categoryWithSubCategories =
        MutableStateFlow<List<CategoryWithSubCategories>>(emptyList())
    val categoryWithSubCategories = _categoryWithSubCategories.asStateFlow()

    private val _saveStatus = MutableStateFlow<Resource<String>>(Resource.Empty())
    val saveStatus = _saveStatus.asStateFlow()

    init {
        getAccounts()
    }

    fun insertTrans(trans: Trans, account: Account) = viewModelScope.launch {
        transUseCases.insertTrans(trans)
        when (trans.type) {
            TransTypes.EXPENSE -> {
                accountsUseCases.updateAccount(
                    account.copy(
                        withdrawal = account.withdrawal + trans.amount
                    )
                )
                _saveStatus.value = Resource.Success<String>("Success")
            }
            TransTypes.INCOME -> {
                accountsUseCases.updateAccount(
                    account.copy(
                        deposit = account.deposit + trans.amount
                    )
                )
                _saveStatus.value = Resource.Success<String>("Success")
            }
        }
    }

    fun insertTransferTrans(trans: Trans, to: Account, from: Account) = viewModelScope.launch {
        transUseCases.insertTrans(trans = trans)
        accountsUseCases.updateAccount(to.copy(deposit = to.deposit + trans.amount))
        accountsUseCases.updateAccount(from.copy(withdrawal = from.withdrawal + trans.amount))
        _saveStatus.value = Resource.Success<String>("Success")
    }

    fun insertTransferTrans(trans: Trans, to: Account, from: Account, feeTrans: Trans) =
        viewModelScope.launch {
            transUseCases.insertTrans(trans = trans)
            transUseCases.insertTrans(trans = feeTrans)
            accountsUseCases.updateAccount(to.copy(deposit = to.deposit + trans.amount))
            accountsUseCases.updateAccount(from.copy(withdrawal = from.withdrawal + trans.amount + trans.fee_amount!!))
            _saveStatus.value = Resource.Success<String>("Success")
        }

    fun getExpenseCategories() {
        categoriesUseCases.getExpenseCategoryWithSubCategories().onEach {
            _categoryWithSubCategories.value = it
        }.launchIn(viewModelScope)
    }

    fun getIncomeCategories() {
        categoriesUseCases.getIncomeCategoryWithSubCategories().onEach {
            _categoryWithSubCategories.value = it
        }.launchIn(viewModelScope)
    }

    private fun getAccounts() {
        accountsUseCases.getAccounts().onEach {
            _accounts.value = it
        }.launchIn(viewModelScope)
    }
}