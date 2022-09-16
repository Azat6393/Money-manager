package com.woynex.parasayar.feature_trans.presentation.trans

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.woynex.parasayar.R
import com.woynex.parasayar.databinding.FragmentTransBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TransFragment : Fragment(R.layout.fragment_trans) {

    private lateinit var _binding: FragmentTransBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentTransBinding.bind(view)
    }

}