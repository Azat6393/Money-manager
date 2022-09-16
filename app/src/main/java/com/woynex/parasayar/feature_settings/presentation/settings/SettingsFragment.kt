package com.woynex.parasayar.feature_settings.presentation.settings

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.woynex.parasayar.R
import com.woynex.parasayar.databinding.FragmentSettingsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment: Fragment(R.layout.fragment_settings) {

    private lateinit var _binding: FragmentSettingsBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSettingsBinding.bind(view)
    }

}