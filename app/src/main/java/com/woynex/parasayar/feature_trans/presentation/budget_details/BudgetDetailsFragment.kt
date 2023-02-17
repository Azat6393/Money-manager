package com.woynex.parasayar.feature_trans.presentation.budget_details

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mikephil.charting.charts.CombinedChart.DrawOrder
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.XAxis.XAxisPosition
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.ValueFormatter
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.woynex.parasayar.R
import com.woynex.parasayar.core.utils.OnItemClickListener
import com.woynex.parasayar.core.utils.custom_dialog.SelectMonthDialog
import com.woynex.parasayar.core.utils.parseDateText
import com.woynex.parasayar.databinding.FragmentBudgetDetailsBinding
import com.woynex.parasayar.feature_trans.domain.model.BarStatisticsItem
import com.woynex.parasayar.feature_trans.domain.model.BudgetItem
import com.woynex.parasayar.feature_trans.domain.model.Trans
import com.woynex.parasayar.feature_trans.presentation.adapter.DailyTransAdapterParent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.time.LocalDate


@AndroidEntryPoint
class BudgetDetailsFragment : Fragment(R.layout.fragment_budget_details),
    OnItemClickListener<Trans> {

    private lateinit var _binding: FragmentBudgetDetailsBinding
    private val args: BudgetDetailsFragmentArgs by navArgs()
    private val mAdapter: DailyTransAdapterParent by lazy { DailyTransAdapterParent(this) }
    private val viewModel: BudgetDetailsViewModel by viewModels()

    private lateinit var date: LocalDate

    private var budgetItem: BudgetItem? = null
    private var barStatistics: List<BarStatisticsItem> = emptyList()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentBudgetDetailsBinding.bind(view)

        date = LocalDate.now()

        initView()
        observe()
        updateData()
    }

    @SuppressLint("SetTextI18n")
    private fun observe() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.dailyTrans.collect { result ->
                    mAdapter.submitList(result)
                }
            }
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.budgetInfo.collect { result ->
                    if (result != null) {
                        budgetItem = result
                        _binding.apply {
                            budget.text = "${result.currency} ${result.budgetAmount}"
                            usedAmount.text = "${result.currency} ${result.expenses}"
                            remaining.text =
                                "${result.currency} ${(result.budgetAmount - result.expenses)}"
                            initChart(barStatistics)
                        }
                    }
                }
            }
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.barStatistics.collect { result ->
                    barStatistics = result
                    initChart(result)
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initView() {
        _binding.apply {
            titleTv.text = args.name
            backBtn.setOnClickListener { findNavController().popBackStack() }
            budgetContainer.isVisible = args.isBudget
            parseWithMonth()
            recyclerView.apply {
                adapter = mAdapter
                layoutManager = LinearLayoutManager(requireContext())
            }
        }
    }

    private fun parseWithMonth() {
        _binding.apply {
            dateTv.text = parseDateText(date)
            dateLeftBtn.setOnClickListener {
                date = date.minusMonths(1)
                dateTv.text = parseDateText(date)
                updateData()
            }
            dateRightBtn.setOnClickListener {
                date = date.plusMonths(1)
                dateTv.text = parseDateText(date)
                updateData()
            }
            dateTv.setOnClickListener {
                SelectMonthDialog(date) { newDate ->
                    date = newDate
                    dateTv.text = parseDateText(date)
                    updateData()
                }.show(childFragmentManager, "SelectMonthDialog")
            }
            dateTv.isEnabled = true
        }
    }

    private fun updateData() {
        if (args.isCategory) {
            viewModel.getTransByCategory(
                category_id = args.id,
                currency = args.currency,
                date = date
            )
            viewModel.getCategoryBudgetInfo(
                category_id = args.id,
                date = date,
                currency = args.currency
            )
        } else {
            viewModel.getTransBySubcategory(
                subcategory_id = args.id,
                currency = args.currency,
                date = date
            )
            viewModel.getSubcategoryBudgetInfo(
                subcategory_id = args.id,
                date = date,
                currency = args.currency
            )
        }
        viewModel.getBarStatistics(args.isCategory, date.year, args.id, args.currency)
    }

    private fun initChart(list: List<BarStatisticsItem>) {
        _binding.combinedChart.apply {
            description.isEnabled = false
            setBackgroundColor(Color.WHITE)
            setDrawGridBackground(false)
            setDrawBarShadow(false)
            isHighlightFullBarEnabled = false

            drawOrder = arrayOf(
                DrawOrder.BAR, DrawOrder.LINE
            )

            val rightAxis: YAxis = axisRight
            rightAxis.setDrawGridLines(false)
            rightAxis.setDrawLabels(false)
            rightAxis.axisMinimum = 0f // this replaces setStartAtZero(true)

            val leftAxis: YAxis = axisLeft
            leftAxis.setDrawGridLines(false)
            leftAxis.axisMinimum = 0f // this replaces setStartAtZero(true)

            val xAxis: XAxis = xAxis
            xAxis.position = XAxisPosition.BOTTOM
            xAxis.axisMinimum = 0f
            xAxis.granularity = 1f

            with(xAxis) {
                valueFormatter = xAxisFormatter
                labelCount = 14
            }

            val data = CombinedData()
            if (args.isBudget && budgetItem != null) {
                data.setData(generateLineData(budgetItem!!))
            }
            data.setData(
                generateBarData(
                    list
                )
            )

            //xAxis.axisMaximum = data.xMax + 0.25f

            setData(data)
            invalidate()
        }
    }

    private val xAxisFormatter = object : ValueFormatter() {
        override fun getFormattedValue(value: Float): String {
            return when (value) {
                1.0f -> getString(R.string.jan) //Jan
                2.0f -> getString(R.string.feb) // Feb
                3.0f -> getString(R.string.mar) //"Mar"
                4.0f -> getString(R.string.apr) //"Apr"
                5.0f -> getString(R.string.may) //"May"
                6.0f -> getString(R.string.jun) //"Jun"
                7.0f -> getString(R.string.jul) //"Jul"
                8.0f -> getString(R.string.aug) //"Aug"
                9.0f -> getString(R.string.sep) //"Sep"
                10.0f -> getString(R.string.oct) //"Oct"
                11.0f -> getString(R.string.nov) //"Nov"
                12.0f -> getString(R.string.dec) //"Dec"
                else -> ""
            }
        }
    }

    private fun generateLineData(item: BudgetItem): LineData {
        val d = LineData()
        val entries: ArrayList<Entry> = ArrayList()
        for (index in 0 until 13) entries.add(
            Entry(
                index.toFloat(), item.budgetAmount.toFloat()
            )
        )
        val set = LineDataSet(entries, "")
        set.color = Color.rgb(240, 238, 70)
        set.lineWidth = 2.5f
        set.setCircleColor(Color.rgb(240, 238, 70))
        set.circleRadius = 5f
        set.fillColor = Color.rgb(240, 238, 70)
        set.mode = LineDataSet.Mode.CUBIC_BEZIER
        set.setDrawValues(false)
        set.valueTextSize = 10f
        set.valueTextColor = Color.rgb(240, 238, 70)
        set.axisDependency = YAxis.AxisDependency.LEFT
        d.addDataSet(set)
        return d
    }

    private fun generateBarData(itemList: List<BarStatisticsItem>): BarData {
        val entries1: ArrayList<BarEntry> = ArrayList()
        itemList.forEach {
            entries1.add(BarEntry(it.month.toFloat(), it.amount.toFloat()))
        }

        val set1 = BarDataSet(entries1, "")
        set1.color = requireContext().getColor(R.color.blue)
        set1.valueTextColor = requireContext().getColor(R.color.black)
        set1.valueTextSize = 10f
        set1.axisDependency = YAxis.AxisDependency.LEFT

        val groupSpace = 0.06f
        val barSpace = 0.02f
        val barWidth = 0.45f

        // (0.45 + 0.02) * 2 + 0.06 = 1.00 -> interval per "group"
        val d = BarData(set1, set1)
        d.barWidth = barWidth

        // make this BarData object grouped
        d.groupBars(0f, groupSpace, barSpace) // start at x = 0
        return d
    }


    override fun onClick(item: Trans) {
        val action =
            BudgetDetailsFragmentDirections.actionBudgetDetailsFragmentToTransEditFragment(item)
        findNavController().navigate(action)
    }

    override fun onStart() {
        super.onStart()
        requireActivity()
            .findViewById<CoordinatorLayout>(R.id.coordinatorLayout)
            .visibility = View.GONE
    }

    override fun onStop() {
        super.onStop()
        requireActivity()
            .findViewById<CoordinatorLayout>(R.id.coordinatorLayout)
            .visibility = View.VISIBLE
    }
}