package com.woynex.parasayar.feature_trans.presentation.calendar

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.woynex.parasayar.R
import com.woynex.parasayar.core.domain.model.Currency
import com.woynex.parasayar.core.utils.CalendarUtils
import com.woynex.parasayar.core.utils.CalendarUtils.Companion.daysInMonthList
import com.woynex.parasayar.core.utils.OnItemClickListener
import com.woynex.parasayar.core.utils.Resource
import com.woynex.parasayar.core.utils.SharedPreferencesHelper
import com.woynex.parasayar.databinding.FragmentCalendarBinding
import com.woynex.parasayar.feature_trans.TransCoreViewModel
import com.woynex.parasayar.feature_trans.domain.model.CalendarDay
import com.woynex.parasayar.feature_trans.domain.model.Trans
import com.woynex.parasayar.feature_trans.presentation.adapter.CalendarAdapter
import com.woynex.parasayar.feature_trans.presentation.trans.TransFragmentDirections
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@AndroidEntryPoint
class CalendarFragment : Fragment(R.layout.fragment_calendar), OnItemClickListener<CalendarDay> {

    private lateinit var _binding: FragmentCalendarBinding
    private val coreViewModel: TransCoreViewModel by activityViewModels()
    private val viewModel: CalendarViewModel by viewModels()
    private val mAdapter: CalendarAdapter by lazy { CalendarAdapter(this) }

    private var selectedDate: LocalDate? = null
    private var selectedCurrency: Currency? = null

    @Inject
    lateinit var preferencesHelper: SharedPreferencesHelper

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentCalendarBinding.bind(view)

        initRecyclerView()
        observe()
    }

    private fun initRecyclerView() {
        _binding.calendarRv.apply {
            adapter = mAdapter
            layoutManager = GridLayoutManager(requireContext(), 7)
            setHasFixedSize(true)
        }
    }

    private fun observe() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                coreViewModel.selectedDate.collect { date ->
                    selectedDate = date
                    viewModel.getTransByMonth(
                        month = date.monthValue,
                        year = date.year,
                        selectedCurrency?.symbol ?: preferencesHelper.getDefaultCurrency().symbol
                    )
                }
            }
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                coreViewModel.selectedCurrency.collect { result ->
                    selectedCurrency = result
                    viewModel.getTransByMonth(
                        month = selectedDate?.monthValue ?: LocalDate.now().monthValue,
                        year = selectedDate?.year ?: LocalDate.now().year,
                        selectedCurrency?.symbol ?: preferencesHelper.getDefaultCurrency().symbol
                    )
                }
            }
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.transList.collect { result ->
                    when (result) {
                        is Resource.Empty -> {
                            selectedDate?.let {
                                setTransToMonth(date = it, transList = emptyList())
                            }
                        }
                        is Resource.Error -> Unit
                        is Resource.Loading -> Unit
                        is Resource.Success -> {
                            selectedDate?.let {
                                setTransToMonth(date = it, transList = result.data ?: emptyList())
                            }
                        }
                    }
                }
            }
        }
    }

    private fun setTransToMonth(date: LocalDate, transList: List<Trans>) {
        mAdapter.submitList(null)
        val daysOfMonth = daysInMonthList(date, date, transList)
        mAdapter.submitList(daysOfMonth)
    }


    override fun onClick(item: CalendarDay) {
        if (item.income != null && item.expense != null) {
            DailyTransBottomSheet(
                item.date,
                viewModel,
                selectedCurrency ?: preferencesHelper.getDefaultCurrency()
            ) { trans ->
                val action = TransFragmentDirections.actionTransFragmentToTransEditFragment(trans)
                findNavController().navigate(action)
            }.show(childFragmentManager, "Calendar Bottom Sheet")
        }
    }
}