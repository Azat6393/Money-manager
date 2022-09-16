package com.woynex.parasayar.feature_accounts.presentation.accounts

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.woynex.parasayar.R
import com.woynex.parasayar.databinding.FragmentAccountsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AccountsFragment : Fragment(R.layout.fragment_accounts) {

    private lateinit var _binding: FragmentAccountsBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAccountsBinding.bind(view)
    }

}