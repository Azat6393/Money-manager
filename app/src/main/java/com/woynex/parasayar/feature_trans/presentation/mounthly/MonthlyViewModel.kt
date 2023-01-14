package com.woynex.parasayar.feature_trans.presentation.mounthly

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.woynex.parasayar.core.utils.convertToYearTrans
import com.woynex.parasayar.feature_trans.domain.model.YearTrans
import com.woynex.parasayar.feature_trans.domain.use_case.DailyUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class MonthlyViewModel @Inject constructor(
    private val dailyTransUseCases: DailyUseCases
): ViewModel() {

    private val _yearTrans = MutableStateFlow<List<YearTrans>>(emptyList())
    val yearTrans = _yearTrans.asStateFlow()

    fun getYearTrans(date: LocalDate, currency: String){
        dailyTransUseCases.getTransByYear(date.year, currency).onEach { result ->
            _yearTrans.value = result.convertToYearTrans(date, currency)
        }.launchIn(viewModelScope)
    }
}