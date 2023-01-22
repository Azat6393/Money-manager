package com.woynex.parasayar.feature_settings.presentation.configuration

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.woynex.parasayar.R
import com.woynex.parasayar.core.utils.CategoryTypes
import com.woynex.parasayar.databinding.FragmentConfigurationBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ConfigurationFragment : Fragment(R.layout.fragment_configuration) {

    private lateinit var _binding: FragmentConfigurationBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentConfigurationBinding.bind(view)

        _binding.apply {
            accountGroupSetting.setOnClickListener {
                val action =
                    ConfigurationFragmentDirections.actionConfigurationFragmentToAccountGroupFragment()
                findNavController().navigate(action)
            }
            accountsSetting.setOnClickListener {
                val action =
                    ConfigurationFragmentDirections.actionConfigurationFragmentToAccountSettingFragment(
                        isDeleteMode = false
                    )
                findNavController().navigate(action)
            }
            incomeSetting.setOnClickListener {
                val action =
                    ConfigurationFragmentDirections.actionConfigurationFragmentToCategorySettingFragment(
                        type = CategoryTypes.INCOME
                    )
                findNavController().navigate(action)
            }
            expensesSetting.setOnClickListener {
                val action =
                    ConfigurationFragmentDirections.actionConfigurationFragmentToCategorySettingFragment(
                        type = CategoryTypes.EXPENSE
                    )
                findNavController().navigate(action)
            }
            currencySetting.setOnClickListener {
                val action =
                    ConfigurationFragmentDirections.actionConfigurationFragmentToCurrenciesFragment()
                findNavController().navigate(action)
            }
            _binding.backBtn.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }
}