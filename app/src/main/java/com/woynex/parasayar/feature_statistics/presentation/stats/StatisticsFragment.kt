package com.woynex.parasayar.feature_statistics.presentation.stats

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.woynex.parasayar.R
import com.woynex.parasayar.databinding.FragmentStatisticsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StatisticsFragment: Fragment(R.layout.fragment_statistics) {

    private lateinit var _binding: FragmentStatisticsBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentStatisticsBinding.bind(view)
    }

}