package com.woynex.parasayar.feature_accounts.presentation.accounts_trans

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.woynex.parasayar.R
import com.woynex.parasayar.core.domain.model.Currency
import com.woynex.parasayar.core.utils.OnItemClickListener
import com.woynex.parasayar.core.utils.SharedPreferencesHelper
import com.woynex.parasayar.core.utils.custom_dialog.SelectMonthDialog
import com.woynex.parasayar.core.utils.parseDateText
import com.woynex.parasayar.core.utils.parseStartAndEndDateOfMonth
import com.woynex.parasayar.databinding.FragmentAccountsTransBinding
import com.woynex.parasayar.feature_trans.domain.model.Trans
import com.woynex.parasayar.feature_trans.presentation.adapter.DailyTransAdapterParent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@AndroidEntryPoint
class AccountsTransFragment : Fragment(R.layout.fragment_accounts_trans),
    OnItemClickListener<Trans> {

    private lateinit var _binding: FragmentAccountsTransBinding
    private val viewModel: AccountsTransViewModel by viewModels()
    private val mAdapter: DailyTransAdapterParent by lazy { DailyTransAdapterParent(this) }
    private val args: AccountsTransFragmentArgs by navArgs()

    private lateinit var date: LocalDate

    private var selectedDate: LocalDate? = null
    private var selectedCurrency: Currency? = null
    private var currencyList: List<Currency> = emptyList()

    @Inject
    lateinit var preferencesHelper: SharedPreferencesHelper

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAccountsTransBinding.bind(view)

        date = LocalDate.now()
        updateDate()
        parseWithMonth()
        initView()
        initRecyclerView()
        observe()
        viewModel.getCurrencies()
    }

    private fun initView() {
        _binding.currencyFilter.editText?.doAfterTextChanged { text ->
            val currency = currencyList.find { it.symbol == text.toString() }
            currency?.let {
                viewModel.updateCurrency(it)
                viewModel.getTrans(args.account.id!!)
            }
        }
        _binding.titleTv.text = args.account.name
        _binding.backBtn.setOnClickListener { findNavController().popBackStack() }

    }

    private fun initRecyclerView() {
        _binding.recyclerView.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
    }

    @SuppressLint("ResourceType")
    private fun observe() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.yearInfo.collect { result ->
                    if (result != null){
                        _binding.income.text = result.income.toString()
                        _binding.expenses.text = result.expence.toString()
                        _binding.total.text = result.total.toString()
                        _binding.balance.text = result.total.toString()
                        _binding.balance.setTextColor(
                            if (result.total >= 0) Color.parseColor("#3a86ff")
                            else Color.parseColor("#e63946")
                        )
                    }
                }
            }
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.transList.collect { result ->
                    mAdapter.submitList(result)
                }
            }
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.selectedDate.collect { date ->
                    selectedDate = date
                    _binding.statementTv.text = selectedDate?.let { parseStartAndEndDateOfMonth(it) }
                    viewModel.getTrans(
                        args.account.id!!
                    )
                }
            }
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.selectedCurrency.collect { result ->
                    selectedCurrency = result
                    _binding.currencyFilter.editText?.setText(result?.symbol)
                    viewModel.getTrans(
                        args.account.id!!
                    )
                }
            }
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.currencies.collect { result ->
                    currencyList = result
                    initCurrencySelector(result)
                }
            }
        }
    }

    private fun initCurrencySelector(currencyList: List<Currency>) {
        val adapter = ArrayAdapter(requireContext(), R.layout.item_select_currency,
            currencyList.map { it.symbol }
        )
        (_binding.currencyFilter.editText as? AutoCompleteTextView)?.setAdapter(adapter)
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

    private fun updateDate() {
        viewModel.updateDate(date)
        viewModel.getTrans(args.account.id!!)
        _binding.statementTv.text = selectedDate?.let { parseStartAndEndDateOfMonth(it) }
    }

    override fun onClick(item: Trans) {
        val action =
            AccountsTransFragmentDirections.actionAccountsTransFragmentToTransEditFragment(item)
        findNavController().navigate(action)
    }
}