package com.woynex.parasayar.feature_trans.presentation.weekly

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.woynex.parasayar.core.utils.convertToWeekTrans
import com.woynex.parasayar.feature_trans.domain.model.WeekTrans
import com.woynex.parasayar.feature_trans.domain.use_case.trans.DailyUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class WeeklyViewModel @Inject constructor(
    private val dailyUseCases: DailyUseCases
) : ViewModel() {

    private val _transList = MutableStateFlow<List<WeekTrans>>(emptyList())
    val transList = _transList.asStateFlow()

    fun getTransByMonth(date: LocalDate, currency: String) {
        dailyUseCases.getWeeklyTrans(date.monthValue, date.year, currency).onEach { result ->
            _transList.value = result.convertToWeekTrans(date, currency)
        }.launchIn(viewModelScope)
    }
}