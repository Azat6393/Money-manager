package com.woynex.parasayar.feature_accounts.presentation.account_setting

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.woynex.parasayar.R
import com.woynex.parasayar.core.utils.showAlertDialog
import com.woynex.parasayar.core.utils.showToastMessage
import com.woynex.parasayar.databinding.FragmentAccountsSettingBinding
import com.woynex.parasayar.feature_accounts.domain.model.Account
import com.woynex.parasayar.feature_accounts.presentation.accounts.AccountsFragmentDirections
import com.woynex.parasayar.feature_accounts.presentation.adapter.AccountListAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AccountSettingFragment : Fragment(R.layout.fragment_accounts_setting),
    AccountListAdapter.AccountSettingItemListener {

    private lateinit var _binding: FragmentAccountsSettingBinding
    private val mAdapter: AccountListAdapter by lazy { AccountListAdapter(this) }
    private val args: AccountSettingFragmentArgs by navArgs()
    private val viewModel: AccountSettingViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAccountsSettingBinding.bind(view)
        initView()
        observe()
        viewModel.getAccounts()
    }

    private fun observe() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.accounts.collect { result ->
                    mAdapter.submitList(result)
                }
            }
        }
    }

    private fun initView() {
        _binding.recyclerView.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
        _binding.deleteBtn.setOnClickListener { deleteMode() }
        _binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }
        _binding.deleteBackBtn.setOnClickListener {
            if (args.isDeleteMode)
                findNavController().popBackStack()
            else settingMode()
        }
        if (args.isDeleteMode) deleteMode() else settingMode()
        _binding.addBtn.setOnClickListener {
            val action =
                AccountSettingFragmentDirections.actionAccountSettingFragmentToAddAccountFragment()
            findNavController().navigate(action)
        }
    }

    private fun deleteMode() {
        _binding.accountDeleteContainer.isVisible = true
        _binding.accountSettingsContainer.isVisible = false
        mAdapter.isDeleteMode(true)
    }

    private fun settingMode() {
        _binding.accountDeleteContainer.isVisible = false
        _binding.accountSettingsContainer.isVisible = true
        mAdapter.isDeleteMode(false)
    }

    override fun onClick(item: Account) {
        val action =
            AccountSettingFragmentDirections.actionAccountSettingFragmentToAddAccountFragment(item)
        findNavController().navigate(action)
    }

    override fun onDelete(item: Account) {
        requireContext().showAlertDialog(
            title = getString(R.string.delete_account_title),
            message = getString(R.string.delete_account_message)
        ) {
            viewModel.deleteAccount(account = item)
        }
    }

    override fun onStart() {
        super.onStart()
        requireActivity()
            .findViewById<BottomNavigationView>(R.id.bottom_navigation)
            .visibility = View.GONE
    }

    override fun onStop() {
        super.onStop()
        requireActivity()
            .findViewById<BottomNavigationView>(R.id.bottom_navigation)
            .visibility = View.VISIBLE
    }
}