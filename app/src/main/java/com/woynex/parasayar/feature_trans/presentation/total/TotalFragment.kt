package com.woynex.parasayar.feature_trans.presentation.total

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.woynex.parasayar.R
import com.woynex.parasayar.core.domain.model.Currency
import com.woynex.parasayar.core.utils.SharedPreferencesHelper
import com.woynex.parasayar.databinding.FragmentTotalBinding
import com.woynex.parasayar.feature_trans.TransCoreViewModel
import com.woynex.parasayar.feature_trans.domain.model.Budget
import com.woynex.parasayar.feature_trans.domain.model.BudgetItem
import com.woynex.parasayar.feature_trans.presentation.adapter.BudgetParentAdapter
import com.woynex.parasayar.feature_trans.presentation.trans.TransFragmentDirections
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@AndroidEntryPoint
class TotalFragment : Fragment(R.layout.fragment_total), BudgetParentAdapter.OnItemClickListener {

    private lateinit var _binding: FragmentTotalBinding
    private val viewModel: TotalViewModel by viewModels()
    private val coreViewModel: TransCoreViewModel by activityViewModels()
    private val mAdapter: BudgetParentAdapter by lazy { BudgetParentAdapter(this) }

    private var selectedDate: LocalDate? = null
    private var selectedCurrency: Currency? = null

    private var childViewState = false

    @Inject
    lateinit var preferencesHelper: SharedPreferencesHelper

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentTotalBinding.bind(view)

        initView()
        observe()
    }

    private fun initView() {
        _binding.recyclerView.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(false)
        }
        _binding.totalContainer.setOnClickListener {
            childViewState = !childViewState
            mAdapter.updateChildViewState(childViewState)
            _binding.arrowIcon.setImageDrawable(
                if (!childViewState) ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.ic_baseline_arrow_drop_up_24
                )
                else ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.ic_baseline_arrow_drop_down_24
                )
            )
        }
        _binding.budgetSetting.setOnClickListener {
            val action = TransFragmentDirections.actionTransFragmentToBudgetSettingFragment()
            findNavController().navigate(action)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun fillInformation(list: List<Budget>) {
        _binding.apply {
            val totalBudget = list.sumOf { it.categoryBudget.budgetAmount }
            val totalExpenses = list.sumOf { it.categoryBudget.expenses }
            val percentage = ((totalExpenses / totalExpenses) * 100).toInt()
            totalAmount.text =
                "${selectedCurrency!!.symbol} $totalBudget"
            expensesTv.text =
                "${selectedCurrency!!.symbol} $totalExpenses"
            percentageTv.text = "$percentage%"
            progressBar.progress = percentage
            if (percentage >= 95) {
                if (context != null) {
                    percentageTv.setTextColor(requireContext().getColor(R.color.white))
                }
            }
            if (totalExpenses > totalBudget) {
                if (context != null) {
                    expensesTv.setTextColor(requireContext().getColor(R.color.red))
                    totalTv.text =
                        "${requireContext().getText(R.string.excess)} ${selectedCurrency!!.symbol} ${totalBudget - totalExpenses}"
                }
            } else {
                totalTv.text =
                    "${selectedCurrency!!.symbol} ${totalBudget - totalExpenses}"
            }
        }
    }

    private fun observe() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.budgets.collect { result ->
                    _binding.budgetContainer.isVisible = result.isNotEmpty()
                    if (result.isNotEmpty()) {
                        fillInformation(result)
                    }
                    mAdapter.submitList(result)
                }
            }
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                coreViewModel.selectedDate.collect { date ->
                    selectedDate = date
                    viewModel.getBudgets(
                        localDate = date,
                        selectedCurrency?.symbol ?: preferencesHelper.getDefaultCurrency().symbol
                    )
                }
            }
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                coreViewModel.selectedCurrency.collect { result ->
                    selectedCurrency = result
                    viewModel.getBudgets(
                        localDate = selectedDate ?: LocalDate.now(),
                        selectedCurrency?.symbol ?: preferencesHelper.getDefaultCurrency().symbol
                    )
                }
            }
        }
    }

    override fun selectCategory(budget: Budget) {

    }

    override fun selectSubcategory(budgetItem: BudgetItem) {

    }
}