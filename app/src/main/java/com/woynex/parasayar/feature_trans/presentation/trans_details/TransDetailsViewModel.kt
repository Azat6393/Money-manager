package com.woynex.parasayar.feature_trans.presentation.trans_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.woynex.parasayar.feature_accounts.domain.model.Account
import com.woynex.parasayar.feature_accounts.domain.use_case.AccountsUseCases
import com.woynex.parasayar.feature_settings.domain.model.CategoryWithSubCategories
import com.woynex.parasayar.feature_settings.domain.use_case.CategoryUseCases
import com.woynex.parasayar.feature_trans.domain.use_case.TransUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
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

    init {
        getAccounts()
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