package com.woynex.parasayar.feature_settings.presentation.settings

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.woynex.parasayar.R
import com.woynex.parasayar.core.utils.CategoryTypes
import com.woynex.parasayar.databinding.FragmentSettingsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : Fragment(R.layout.fragment_settings) {

    private lateinit var _binding: FragmentSettingsBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSettingsBinding.bind(view)

        _binding.apply {
            setting.setOnClickListener {
                val action = SettingsFragmentDirections.actionSettingsFragmentToConfigurationFragment()
                findNavController().navigate(action)
            }
            webSite.setOnClickListener { navigateToWebView("http://woynapp.com") }
            privacyPolicy.setOnClickListener { navigateToWebView("http://woynapp.com/gizlilik-politikasi/") }
            termOfService.setOnClickListener { navigateToWebView("http://woynapp.com/terms-of-service/") }
        }
    }

    private fun navigateToWebView(url: String) {
        val action = SettingsFragmentDirections.actionSettingsFragmentToWebViewFragment(url)
        findNavController().navigate(action)
    }
}