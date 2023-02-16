package com.woynex.parasayar.feature_statistics.presentation.income

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.utils.MPPointF
import com.woynex.parasayar.R
import com.woynex.parasayar.core.domain.model.Currency
import com.woynex.parasayar.core.utils.OnItemClickListener
import com.woynex.parasayar.core.utils.SharedPreferencesHelper
import com.woynex.parasayar.core.utils.getColorFromAttr
import com.woynex.parasayar.databinding.FragmentStatisticsIncomeBinding
import com.woynex.parasayar.feature_statistics.domain.model.CategoryStatistics
import com.woynex.parasayar.feature_statistics.presentation.adapter.CategoryStatisticsAdapter
import com.woynex.parasayar.feature_statistics.presentation.stats.StatisticsCoreViewModel
import com.woynex.parasayar.feature_statistics.presentation.stats.StatisticsFilter
import com.woynex.parasayar.feature_statistics.presentation.stats.StatisticsFragment
import com.woynex.parasayar.feature_statistics.presentation.stats.StatisticsFragmentDirections
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.temporal.WeekFields
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList


@AndroidEntryPoint
class StatisticsIncomeFragment : Fragment(R.layout.fragment_statistics_income),
    OnItemClickListener<CategoryStatistics>,
    OnChartValueSelectedListener {

    private lateinit var _binding: FragmentStatisticsIncomeBinding
    private val coreViewModel: StatisticsCoreViewModel by activityViewModels()
    private val viewModel: StatisticsIncomeViewModel by viewModels()
    private val mAdapter: CategoryStatisticsAdapter by lazy { CategoryStatisticsAdapter(this) }

    private var selectedCurrency: Currency? = null
    private var selectedDate: LocalDate = LocalDate.now()
    private var selectedStartDate: LocalDate = LocalDate.now().minusMonths(1)
    private var selectedEndDate: LocalDate = LocalDate.now()
    private var filter: StatisticsFilter = StatisticsFilter.Monthly

    @Inject
    lateinit var preferencesHelper: SharedPreferencesHelper

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentStatisticsIncomeBinding.bind(view)

        initView()
        observe()
        viewModel.getMonthlyTrans(
            date = selectedDate,
            currency = selectedCurrency?.symbol
                ?: preferencesHelper.getDefaultCurrency().symbol
        )
    }

    private fun initView() {
        _binding.recyclerView.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun observe() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                coreViewModel.selectedCurrency.collect { result ->
                    selectedCurrency = result
                    updateData()
                }
            }
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                coreViewModel.selectedDate.collect { result ->
                    selectedDate = result
                    updateData()
                }
            }
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                coreViewModel.filter.collect { result ->
                    filter = result
                    updateData()
                }
            }
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                coreViewModel.selectedEndDate.collect { result ->
                    selectedEndDate = result
                    updateData()
                }
            }
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                coreViewModel.selectedStartDate.collect { result ->
                    selectedStartDate = result
                    updateData()
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.categoryStatistics.collect { result ->
                    mAdapter.submitList(result)
                    initChart(result)
                }
            }
        }
    }

    private fun initChart(items: List<CategoryStatistics>) {
        _binding.chart1.apply {
            setUsePercentValues(true)
            description.isEnabled = false
            setExtraOffsets(5f, 10f, 5f, 5f)
            dragDecelerationFrictionCoef = 0.95f
            isDrawHoleEnabled = true
            setHoleColor(requireContext().getColorFromAttr(R.attr.background_color))
            setTransparentCircleColor(requireContext().getColorFromAttr(R.attr.background_color))
            setTransparentCircleAlpha(110)
            holeRadius = 58f
            transparentCircleRadius = 61f
            setDrawCenterText(true)
            rotationAngle = 0f
            isRotationEnabled = true
            isHighlightPerTapEnabled = true
            setOnChartValueSelectedListener(this@StatisticsIncomeFragment)
            animateY(1400, Easing.EaseInOutQuad)
            val l: Legend = legend
            l.isEnabled = false
            setEntryLabelColor(Color.BLACK)
            setEntryLabelTextSize(12f)

            val dataItem = mutableListOf<PieEntry>()
            items.forEach {
                dataItem.add(
                    PieEntry(
                        it.percentage.toFloat(), it.category_name
                    )
                )
            }

            val dataSet = PieDataSet(dataItem, "")

            dataSet.setDrawIcons(false)

            dataSet.sliceSpace = 3f
            dataSet.iconsOffset = MPPointF(0f, 40f)
            dataSet.selectionShift = 5f

            val colors: ArrayList<Int> = ArrayList()
            items.forEach {
                colors.add(it.color)
            }

            dataSet.colors = colors

            val data = PieData(dataSet)
            data.setValueFormatter(PercentFormatter())
            data.setValueTextSize(11f)
            data.setValueTextColor(Color.BLACK)
            setData(data)
        }
    }

    private fun updateData() {
        when (filter) {
            StatisticsFilter.Annually -> {
                viewModel.getAnnuallyTrans(
                    date = selectedDate,
                    currency = selectedCurrency?.symbol
                        ?: preferencesHelper.getDefaultCurrency().symbol
                )
            }
            StatisticsFilter.Monthly -> {
                viewModel.getMonthlyTrans(
                    date = selectedDate,
                    currency = selectedCurrency?.symbol
                        ?: preferencesHelper.getDefaultCurrency().symbol
                )
            }
            StatisticsFilter.Period -> {
                viewModel.getPeriodTrans(
                    startDate = selectedStartDate,
                    endDate = selectedEndDate,
                    currency = selectedCurrency?.symbol
                        ?: preferencesHelper.getDefaultCurrency().symbol
                )
            }
            StatisticsFilter.Weekly -> {
                val start = selectedDate.with(WeekFields.of(Locale.US).dayOfWeek(), 1L)
                val end = selectedDate.with(WeekFields.of(Locale.US).dayOfWeek(), 7L)
                viewModel.getPeriodTrans(
                    startDate = start,
                    endDate = end,
                    currency = selectedCurrency?.symbol
                        ?: preferencesHelper.getDefaultCurrency().symbol
                )
            }
        }
    }

    override fun onValueSelected(e: Entry?, h: Highlight?) {

    }

    override fun onNothingSelected() {

    }

    override fun onClick(item: CategoryStatistics) {
        val action = StatisticsFragmentDirections.actionStatisticsFragmentToBudgetDetailsFragment(
            isCategory = true,
            id = item.category_id!!,
            name = item.category_name,
            isBudget = false,
            currency = item.currency
        )
        findNavController().navigate(action)
    }
}