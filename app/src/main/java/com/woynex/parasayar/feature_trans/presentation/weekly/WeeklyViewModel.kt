package com.woynex.parasayar.feature_trans.presentation.weekly

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.woynex.parasayar.core.utils.Resource
import com.woynex.parasayar.core.utils.convertToDailyTransList
import com.woynex.parasayar.core.utils.convertToWeekTrans
import com.woynex.parasayar.feature_trans.domain.model.DailyTrans
import com.woynex.parasayar.feature_trans.domain.model.Trans
import com.woynex.parasayar.feature_trans.domain.model.WeekTrans
import com.woynex.parasayar.feature_trans.domain.use_case.DailyUseCases
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

    fun getTransByMonth(date: LocalDate) {
        dailyUseCases.getWeeklyTrans(date.monthValue, date.year).onEach { result ->
            _transList.value = result.convertToWeekTrans(date)
        }.launchIn(viewModelScope)
    }
}