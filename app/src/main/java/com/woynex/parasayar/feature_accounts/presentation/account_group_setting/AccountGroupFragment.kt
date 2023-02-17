package com.woynex.parasayar.feature_accounts.presentation.account_group_setting

import android.os.Bundle
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.woynex.parasayar.R
import com.woynex.parasayar.core.utils.custom_dialog.CustomEditDialog
import com.woynex.parasayar.core.utils.showAlertDialog
import com.woynex.parasayar.databinding.FragmentAccountGroupBinding
import com.woynex.parasayar.feature_accounts.domain.model.AccountGroup
import com.woynex.parasayar.feature_accounts.presentation.adapter.AccountGroupSettingAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.item_accout.*
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AccountGroupFragment : Fragment(R.layout.fragment_account_group),
    AccountGroupSettingAdapter.AccountGroupSettingItemListener {

    private lateinit var _binding: FragmentAccountGroupBinding
    private val mAdapter: AccountGroupSettingAdapter by lazy { AccountGroupSettingAdapter(this) }
    private val viewModel: AccountGroupViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAccountGroupBinding.bind(view)

        initView()
        observe()
    }

    private fun observe() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.accountGroup.collect { result ->
                    mAdapter.submitList(result)
                }
            }
        }
    }

    private fun initView() {
        _binding.apply {
            addBtn.setOnClickListener {
                CustomEditDialog(
                    titleText = getString(R.string.add_account_group),
                    isEditMode = false,
                    save = {
                        viewModel.insertAccountGroup(AccountGroup(name = it))
                    },
                    update = {}
                ).show(childFragmentManager, "EditAccountGroupDialog")
            }
            recyclerView.apply {
                adapter = mAdapter
                layoutManager = LinearLayoutManager(requireContext())
                setHasFixedSize(true)
            }
            backBtn.setOnClickListener { findNavController().popBackStack() }
        }
    }

    override fun onClick(accountGroup: AccountGroup) {
        CustomEditDialog(
            titleText = getString(R.string.add_account_group),
            isEditMode = true,
            save = {},
            editText = accountGroup.name,
            update = {
                viewModel.updateAccountGroup(accountGroup.copy(name = it))
            }
        ).show(childFragmentManager, "EditAccountGroupDialog")
    }

    override fun onDelete(accountGroup: AccountGroup) {
        requireContext().showAlertDialog(
            getString(R.string.delete_account_group_title),
            getString(R.string.delete_account_group_message)
        ) {
            viewModel.deleteAccountGroup(accountGroup)
        }
    }

    override fun onStart() {
        super.onStart()
        requireActivity()
            .findViewById<CoordinatorLayout>(R.id.coordinatorLayout)
            .visibility = View.GONE
    }

    override fun onStop() {
        super.onStop()
        requireActivity()
            .findViewById<CoordinatorLayout>(R.id.coordinatorLayout)
            .visibility = View.VISIBLE
    }
}