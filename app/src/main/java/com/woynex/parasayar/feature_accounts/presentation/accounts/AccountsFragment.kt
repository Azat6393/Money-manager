package com.woynex.parasayar.feature_accounts.presentation.accounts

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.skydoves.powermenu.MenuAnimation
import com.skydoves.powermenu.OnMenuItemClickListener
import com.skydoves.powermenu.PowerMenu
import com.skydoves.powermenu.PowerMenuItem
import com.woynex.parasayar.R
import com.woynex.parasayar.core.domain.model.Currency
import com.woynex.parasayar.core.utils.OnItemClickListener
import com.woynex.parasayar.core.utils.maskCurrency
import com.woynex.parasayar.databinding.FragmentAccountsBinding
import com.woynex.parasayar.feature_accounts.domain.model.Account
import com.woynex.parasayar.feature_accounts.presentation.adapter.AccountAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class AccountsFragment : Fragment(R.layout.fragment_accounts), OnItemClickListener<Account> {

    private lateinit var _binding: FragmentAccountsBinding
    private val viewModel: AccountsViewModel by viewModels()
    private lateinit var powerMenu: PowerMenu
    private val mAdapter: AccountAdapter by lazy { AccountAdapter(this) }

    private var currencyList: List<Currency> = emptyList()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAccountsBinding.bind(view)

        initPowerMenu()
        _binding.moreBtn.setOnClickListener {
            powerMenu.showAsDropDown(_binding.moreBtn)
        }
        _binding.currencyFilter.editText?.doAfterTextChanged { text ->
            val currency = currencyList.find { it.symbol == text.toString() }
            currency?.let {
                viewModel.updateCurrency(it)
                viewModel.getAccounts()
            }
        }
        initRecyclerView()
        observe()
        viewModel.getCurrencies()
    }

    private fun initRecyclerView() {
        _binding.recyclerView.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
    }

    private fun observe() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.accounts.collectLatest { result ->
                    val sortedList = result.sortedWith(compareBy({ it.group_name }, { it.name }))
                    mAdapter.submitList(sortedList)
                    var allDeposit = 0.0
                    var allWithdraws = 0.0
                    result.forEach {
                        allDeposit += it.deposit
                        allWithdraws += it.withdrawal
                    }
                    _binding.apply {
                        assets.text = allDeposit.maskCurrency()
                        liabilities.text = allWithdraws.maskCurrency()
                        total.text = (allDeposit - allWithdraws).maskCurrency()
                        total.setTextColor(
                            if ((allDeposit - allWithdraws) >= 0) Color.parseColor("#3a86ff")
                            else Color.parseColor("#e63946")
                        )
                    }
                }
            }
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.currencies.collect { result ->
                    currencyList = result
                    initCurrencySelector(result)
                }
            }
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.selectedCurrency.collect { result ->
                    _binding.currencyFilter.editText?.setText(result?.symbol)
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

    private fun initPowerMenu() {
        powerMenu = PowerMenu.Builder(requireContext())
            .addItem(PowerMenuItem(getString(R.string.add), false))
            .addItem(PowerMenuItem(getString(R.string.delete), false))
            .setAnimation(MenuAnimation.SHOWUP_TOP_RIGHT)
            .setMenuRadius(10f)
            .setMenuShadow(10f)
            .setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.black
                )
            )
            .setTextGravity(Gravity.START)
            .setTextTypeface(Typeface.create("sans-serif-medium", Typeface.NORMAL))
            .setSelectedTextColor(Color.WHITE)
            .setMenuColor(Color.WHITE)
            .setSelectedMenuColor(ContextCompat.getColor(requireContext(), R.color.primary))
            .setOnMenuItemClickListener(onMenuItemClickListener)
            .build()
    }

    private val onMenuItemClickListener: OnMenuItemClickListener<PowerMenuItem> =
        OnMenuItemClickListener<PowerMenuItem> { position, item ->
            when (position) {
                0 -> {
                    powerMenu.dismiss()
                    val action =
                        AccountsFragmentDirections.actionAccountsFragmentToAddAccountFragment()
                    findNavController().navigate(action)
                }
                1 -> {
                    val action =
                        AccountsFragmentDirections.actionAccountsFragmentToAccountSettingFragment(
                            true
                        )
                    findNavController().navigate(action)
                    powerMenu.dismiss()
                }
            }
        }

    override fun onClick(item: Account) {
        val action =
            AccountsFragmentDirections.actionAccountsFragmentToAccountsTransFragment(account = item)
        findNavController().navigate(action)
    }
}