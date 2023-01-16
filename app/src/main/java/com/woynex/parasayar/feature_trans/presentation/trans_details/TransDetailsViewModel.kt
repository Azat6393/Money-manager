package com.woynex.parasayar.feature_trans.presentation.trans_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.woynex.parasayar.core.utils.Resource
import com.woynex.parasayar.core.utils.TransTypes
import com.woynex.parasayar.feature_accounts.data.room.toAccount
import com.woynex.parasayar.feature_accounts.domain.model.Account
import com.woynex.parasayar.feature_accounts.domain.repository.AccountsRepository
import com.woynex.parasayar.feature_accounts.domain.use_case.AccountsUseCases
import com.woynex.parasayar.feature_settings.domain.model.CategoryWithSubCategories
import com.woynex.parasayar.feature_settings.domain.use_case.CategoryUseCases
import com.woynex.parasayar.feature_trans.domain.model.Trans
import com.woynex.parasayar.feature_trans.domain.use_case.TransUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
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
    private val categoriesUseCases: CategoryUseCases,
    private val repo: AccountsRepository
) : ViewModel() {

    private val _accounts = MutableStateFlow<List<Account>>(emptyList())
    val accounts = _accounts.asStateFlow()

    private val _categoryWithSubCategories =
        MutableStateFlow<List<CategoryWithSubCategories>>(emptyList())
    val categoryWithSubCategories = _categoryWithSubCategories.asStateFlow()

    private val _saveStatus = MutableStateFlow<Resource<String>>(Resource.Empty())
    val saveStatus = _saveStatus.asStateFlow()

    private val _updateStatus = MutableStateFlow<Resource<String>>(Resource.Empty())
    val updateStatus = _updateStatus.asStateFlow()

    init {
        getAccounts()
    }

    fun insertTrans(trans: Trans) = viewModelScope.launch {
        transUseCases.insertTrans(trans)
        _saveStatus.value = Resource.Success<String>("Success")
    }

    fun insertFromTrans(trans: Trans) = viewModelScope.launch {
        transUseCases.insertTrans(trans)
        _updateStatus.value = Resource.Success<String>("Success")
    }

    fun updateTrans(trans: Trans) = viewModelScope.launch {
        transUseCases.updateTrans(trans)
        _updateStatus.value = Resource.Success<String>("Success")
    }

    fun insertTransferTrans(trans: Trans) = viewModelScope.launch {
        transUseCases.insertTrans(trans = trans)
        _saveStatus.value = Resource.Success<String>("Success")
    }

    fun insertTransferTrans(trans: Trans, feeTrans: Trans) =
        viewModelScope.launch {
            transUseCases.insertTrans(trans = trans)
            transUseCases.insertTrans(trans = feeTrans)
            _saveStatus.value = Resource.Success<String>("Success")
        }

    fun updateFeeAmount(amount: Double, transId: String) = viewModelScope.launch {
        transUseCases.updateFeeAmount(amount, transId)
    }

    fun deleteFeeTrans(transId: String) = viewModelScope.launch {
        transUseCases.deleteFeeTrans(transId)
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
        repo.getAccounts().onEach { list ->
            _accounts.value = list.map { it.toAccount() }
        }.launchIn(viewModelScope)
    }
}