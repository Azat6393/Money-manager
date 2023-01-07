package com.woynex.parasayar.feature_trans.presentation.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.woynex.parasayar.core.utils.Resource
import com.woynex.parasayar.feature_trans.domain.model.Trans
import com.woynex.parasayar.feature_trans.domain.use_case.DailyUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val dailyUseCases: DailyUseCases
) : ViewModel() {

    private val _transList = MutableStateFlow<Resource<List<Trans>>>(Resource.Empty())
    val transList = _transList.asStateFlow()

    fun getTransByMonth(month: Int, year: Int) {
        dailyUseCases.getTransByMonth(month, year).onEach { result ->
            if (result.isEmpty()) {
                _transList.value = Resource.Empty<List<Trans>>()
            } else {
                _transList.value = Resource.Success<List<Trans>>(result)
            }
        }.launchIn(viewModelScope)
    }
}