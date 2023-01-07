package com.woynex.parasayar.feature_trans

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.woynex.parasayar.core.utils.convertToYearInfo
import com.woynex.parasayar.feature_trans.domain.model.YearInfo
import com.woynex.parasayar.feature_trans.domain.use_case.DailyUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class TransCoreViewModel @Inject constructor(
    private val transUseCases: DailyUseCases
) : ViewModel() {

    private val _selectedDate = MutableStateFlow<LocalDate>(LocalDate.now())
    val selectedDate = _selectedDate.asStateFlow()

    private val _yearInfo = MutableStateFlow<YearInfo?>(null)
    val yearInfo = _yearInfo.asStateFlow()

    fun updateDate(date: LocalDate) {
        _selectedDate.value = date
    }

    fun getTransByYear() {
        transUseCases.getTransByYear(_selectedDate.value.year).onEach { result ->
            _yearInfo.value = result.convertToYearInfo()
        }.launchIn(viewModelScope)
    }
}