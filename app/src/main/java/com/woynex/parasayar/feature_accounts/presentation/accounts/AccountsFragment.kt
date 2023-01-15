package com.woynex.parasayar.feature_accounts.presentation.accounts

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
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
import com.woynex.parasayar.core.utils.OnItemClickListener
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAccountsBinding.bind(view)

        initPowerMenu()
        _binding.moreBtn.setOnClickListener {
            powerMenu.showAsDropDown(_binding.moreBtn)
        }
        initRecyclerView()
        observe()
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
                        assets.text = allDeposit.toString()
                        liabilities.text = allWithdraws.toString()
                        total.text = (allDeposit - allWithdraws).toString()
                        total.setTextColor(
                            if ((allDeposit - allWithdraws) >= 0) Color.parseColor("#3a86ff")
                            else Color.parseColor("#e63946")
                        )
                    }
                }
            }
        }
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
                        AccountsFragmentDirections.actionAccountsFragmentToAccountSettingFragment(false)
                    findNavController().navigate(action)
                    powerMenu.dismiss()
                }
            }
        }

    override fun onClick(item: Account) {

    }
}