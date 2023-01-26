package com.woynex.parasayar.feature_statistics.presentation.stats

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.tabs.TabLayoutMediator
import com.woynex.parasayar.R
import com.woynex.parasayar.core.domain.model.Currency
import com.woynex.parasayar.core.utils.*
import com.woynex.parasayar.core.utils.custom_dialog.SelectMonthDialog
import com.woynex.parasayar.databinding.FragmentStatisticsBinding
import com.woynex.parasayar.feature_statistics.presentation.adapter.StatisticsViewPagerAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.time.LocalDate

@AndroidEntryPoint
class StatisticsFragment : Fragment(R.layout.fragment_statistics) {

    private lateinit var _binding: FragmentStatisticsBinding
    private val coreViewModel: StatisticsCoreViewModel by activityViewModels()

    private lateinit var date: LocalDate
    private var startDate: LocalDate = LocalDate.now().minusMonths(1)
    private var endDate: LocalDate = LocalDate.now()
    private var currencyList: List<Currency> = emptyList()
    private var selectedFilter: StatisticsFilter = StatisticsFilter.Monthly

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentStatisticsBinding.bind(view)

        date = LocalDate.now()
        updateDate()

        _binding.currencyTextField.editText?.doAfterTextChanged { text ->
            val currency = currencyList.find { it.symbol == text.toString() }
            currency?.let {
                coreViewModel.updateCurrency(it)
            }
        }

        parseWithMonth()
        initViewPager()
        observe()
        coreViewModel.getCurrencies()
    }

    private fun observe() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                coreViewModel.currencies.collect { result ->
                    currencyList = result
                    initCurrencySelector(result)
                }
            }
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                coreViewModel.selectedCurrency.collect { result ->
                    _binding.currencyTextField.editText?.setText(result?.symbol)
                }
            }
        }
    }

    private fun initCurrencySelector(currencyList: List<Currency>) {
        val adapter = ArrayAdapter(requireContext(), R.layout.item_select_currency,
            currencyList.map { it.symbol }
        )
        (_binding.currencyTextField.editText as? AutoCompleteTextView)?.setAdapter(adapter)
    }

    private fun parseOnlyYear() {
        _binding.apply {
            dateTv.text = parseYear(date)
            dateLeftBtn.setOnClickListener {
                date = date.minusYears(1)
                dateTv.text = parseYear(date)
                updateDate()
            }
            dateRightBtn.setOnClickListener {
                date = date.plusYears(1)
                dateTv.text = parseYear(date)
                updateDate()
            }
            dateTv.isEnabled = false
        }
    }

    private fun parseWeekly() {
        _binding.apply {
            dateTv.text = parseStartAndEndOfWeek(date)
            dateLeftBtn.setOnClickListener {
                date = date.minusWeeks(1)
                dateTv.text = parseStartAndEndOfWeek(date)
                updateDate()
            }
            dateRightBtn.setOnClickListener {
                date = date.plusWeeks(1)
                dateTv.text = parseStartAndEndOfWeek(date)
                updateDate()
            }
            dateTv.setOnClickListener {
                SelectMonthDialog(date) { newDate ->
                    date = newDate
                    dateTv.text = parseStartAndEndOfWeek(date)
                    updateDate()
                }.show(childFragmentManager, "SelectMonthDialog")
            }
            dateTv.isEnabled = true
        }
    }

    private fun parseWithMonth() {
        _binding.apply {
            dateTv.text = parseDateText(date)
            dateLeftBtn.setOnClickListener {
                date = date.minusMonths(1)
                dateTv.text = parseDateText(date)
                updateDate()
            }
            dateRightBtn.setOnClickListener {
                date = date.plusMonths(1)
                dateTv.text = parseDateText(date)
                updateDate()
            }
            dateTv.setOnClickListener {
                SelectMonthDialog(date) { newDate ->
                    date = newDate
                    dateTv.text = parseDateText(date)
                    updateDate()
                }.show(childFragmentManager, "SelectMonthDialog")
            }
            dateTv.isEnabled = true
        }
    }

    private fun parsePeriod(){
        _binding.apply {
            startTv.text = parseFullDate(startDate)
            endTv.text = parseFullDate(endDate)
            startCalendarIv.setOnClickListener {
                val picker = MaterialDatePicker.Builder.datePicker()
                    .setTitleText("Select dates")
                    .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                    .build()
                picker.addOnPositiveButtonClickListener {
                    startDate = millisecondToLocalDate(it)
                    startTv.text = parseFullDate(startDate)
                    coreViewModel.updateStartDate(startDate)
                }
                picker.show(childFragmentManager, "Date Picker")
            }
            endCalendarIv.setOnClickListener {
                val picker = MaterialDatePicker.Builder.datePicker()
                    .setTitleText("Select dates")
                    .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                    .build()
                picker.addOnPositiveButtonClickListener {
                    endDate = millisecondToLocalDate(it)
                    endTv.text = parseFullDate(endDate)
                    coreViewModel.updateEndDate(endDate)
                }
                picker.show(childFragmentManager, "Date Picker")
            }
        }
    }

    private fun updateDate() {
        coreViewModel.updateDate(date)
    }

    private fun initViewPager() {
        _binding.apply {
            viewPager.adapter = StatisticsViewPagerAdapter(this@StatisticsFragment)
            viewPager.offscreenPageLimit = 1
            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                when (position) {
                    0 -> tab.text = getString(R.string.income)
                    1 -> tab.text = getString(R.string.expenses)
                }
            }.attach()
            viewPager.isUserInputEnabled = false
            radioGroup.check(R.id.monthly)
            radioGroup.setOnCheckedChangeListener { group, checkedId ->
                when (checkedId) {
                    R.id.weekly -> onSelectWeekly()
                    R.id.monthly -> onSelectMonthly()
                    R.id.annually -> onSelectAnnually()
                    R.id.period -> onSelectPeriod()
                }
            }
        }
    }

    private fun onSelectWeekly() {
        coreViewModel.updateFilter(StatisticsFilter.Weekly)
        selectedFilter = StatisticsFilter.Weekly
        dateContainerVisibility(true)
        parseWeekly()

    }

    private fun onSelectMonthly() {
        coreViewModel.updateFilter(StatisticsFilter.Monthly)
        selectedFilter = StatisticsFilter.Monthly
        dateContainerVisibility(true)
        parseWithMonth()

    }

    private fun onSelectAnnually() {
        coreViewModel.updateFilter(StatisticsFilter.Annually)
        selectedFilter = StatisticsFilter.Annually
        dateContainerVisibility(true)
        parseOnlyYear()

    }

    private fun onSelectPeriod() {
        coreViewModel.updateFilter(StatisticsFilter.Period)
        selectedFilter = StatisticsFilter.Period
        dateContainerVisibility(false)
        parsePeriod()
    }

    private fun dateContainerVisibility(state: Boolean) {
        _binding.apply {
            dateContainer.isVisible = state
            periodContainer.isVisible = !state
        }
    }
}