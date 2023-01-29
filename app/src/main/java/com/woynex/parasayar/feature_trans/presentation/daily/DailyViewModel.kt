package com.woynex.parasayar.feature_trans.presentation.daily

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.woynex.parasayar.core.utils.convertToDailyTransList
import com.woynex.parasayar.feature_trans.domain.model.DailyTrans
import com.woynex.parasayar.feature_trans.domain.use_case.trans.DailyUseCases
import com.woynex.parasayar.feature_trans.domain.use_case.trans.TransUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DailyViewModel @Inject constructor(
    private val transUseCases: TransUseCases,
    private val dailyTransUseCases: DailyUseCases
) : ViewModel() {

    private val _dailyTrans = MutableStateFlow<List<DailyTrans>>(emptyList())
    val dailyTrans = _dailyTrans.asStateFlow()

    fun getMonthlyTrans(month: Int, year: Int, currency: String) = viewModelScope.launch {
        dailyTransUseCases.getTransByMonth(month, year, currency).onEach { result ->
            _dailyTrans.value = result.convertToDailyTransList()
        }.launchIn(viewModelScope)
    }
}