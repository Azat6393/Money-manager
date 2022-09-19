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
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import com.skydoves.powermenu.MenuAnimation
import com.skydoves.powermenu.OnMenuItemClickListener
import com.skydoves.powermenu.PowerMenu
import com.skydoves.powermenu.PowerMenuItem
import com.woynex.parasayar.R
import com.woynex.parasayar.databinding.FragmentAccountsBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AccountsFragment : Fragment(R.layout.fragment_accounts) {

    private lateinit var _binding: FragmentAccountsBinding

    private lateinit var powerMenu: PowerMenu

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAccountsBinding.bind(view)

        initPowerMenu()
        _binding.moreBtn.setOnClickListener {
            powerMenu.showAsDropDown(_binding.moreBtn)
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
            when(position){
                0 -> {
                    powerMenu.dismiss()
                    val action = AccountsFragmentDirections.actionAccountsFragmentToAddAccountFragment()
                    findNavController().navigate(action)
                }
                1 -> {
                    Toast.makeText(requireContext(),"Delete", Toast.LENGTH_SHORT).show()
                    powerMenu.dismiss()
                }
            }
        }
}