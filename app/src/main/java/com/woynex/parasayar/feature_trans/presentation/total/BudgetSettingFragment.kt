package com.woynex.parasayar.feature_trans.presentation.total

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.woynex.parasayar.R
import com.woynex.parasayar.core.domain.model.Currency
import com.woynex.parasayar.core.utils.SharedPreferencesHelper
import com.woynex.parasayar.core.utils.showAlertDialog
import com.woynex.parasayar.databinding.FragmentBudgetSettingBinding
import com.woynex.parasayar.feature_trans.TransCoreViewModel
import com.woynex.parasayar.feature_trans.domain.model.CategoryWithSubcategoryBudget
import com.woynex.parasayar.feature_trans.domain.model.SubcategoryBudget
import com.woynex.parasayar.feature_trans.presentation.adapter.BudgetParentSettingAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class BudgetSettingFragment : Fragment(R.layout.fragment_budget_setting),
    BudgetParentSettingAdapter.OnItemClickListener {

    private lateinit var _binding: FragmentBudgetSettingBinding
    private val viewModel: BudgetViewModel by viewModels()
    private val coreViewModel: TransCoreViewModel by viewModels()
    private val mAdapter: BudgetParentSettingAdapter by lazy { BudgetParentSettingAdapter(this) }

    private var childViewState = false
    private var currencyList: List<Currency> = emptyList()

    private var selectedCurrency = ""

    @Inject
    lateinit var sharedPreferencesHelper: SharedPreferencesHelper

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentBudgetSettingBinding.bind(view)

        selectedCurrency = sharedPreferencesHelper.getDefaultCurrency().symbol
        _binding.currencyFilter.editText?.setText(selectedCurrency)

        initView()
        observe()
        coreViewModel.getCurrencies()
        viewModel.getCategoryBudgets(sharedPreferencesHelper.getDefaultCurrency().symbol)
    }

    private fun observe() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                coreViewModel.currencies.collect { result ->
                    currencyList = result
                    updateCurrencies(result)
                }
            }
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.categoryBudgets.collect { result ->
                    mAdapter.submitList(result)
                }
            }
        }
    }


    private fun updateCurrencies(currencyList: List<Currency>) {
        val adapter = ArrayAdapter(
            requireContext(), R.layout.item_select_currency,
            currencyList.map { it.symbol }
        )
        (_binding.currencyFilter.editText as? AutoCompleteTextView)?.setAdapter(adapter)
    }

    private fun initView() {
        _binding.backBtn.setOnClickListener { findNavController().popBackStack() }
        _binding.addBtn.setOnClickListener {
            val action =
                BudgetSettingFragmentDirections.actionBudgetSettingFragmentToAddBudgetFragment()
            findNavController().navigate(action)
        }
        _binding.recyclerView.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(false)
        }
        _binding.currencyFilter.editText?.doAfterTextChanged { text ->
            val currency = currencyList.find { it.symbol == text.toString() }
            currency?.let {
                selectedCurrency = it.symbol
                viewModel.getCategoryBudgets(currency = it.symbol)
            }
        }
        _binding.budgetContainer.setOnClickListener {
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
    }

    override fun onDeleteCategory(categoryWithSubcategoryBudget: CategoryWithSubcategoryBudget) {
        requireActivity().showAlertDialog(
            title = getString(R.string.delete_budget_title),
            message = getString(R.string.delete_budget_message)
        ) {
            viewModel.deleteCategoryBudgets(categoryWithSubcategoryBudget)
            lifecycleScope.launch {
                delay(2000L)
                viewModel.getCategoryBudgets(selectedCurrency)
            }
        }
    }

    override fun onDeleteSubcategory(subcategoryBudget: SubcategoryBudget) {
        requireActivity().showAlertDialog(
            title = getString(R.string.delete_budget_title),
            message = getString(R.string.delete_budget_message)
        ) {
            viewModel.deleteSubcategoryBudget(subcategoryBudget)
            viewModel.getCategoryBudgets(selectedCurrency)
        }
    }

    override fun onStart() {
        super.onStart()
        requireActivity()
            .findViewById<BottomNavigationView>(R.id.bottom_navigation)
            .visibility = View.GONE
    }

    override fun onStop() {
        super.onStop()
        requireActivity()
            .findViewById<BottomNavigationView>(R.id.bottom_navigation)
            .visibility = View.VISIBLE
    }
}