package com.woynex.parasayar.feature_accounts.presentation.add_account

import android.os.Bundle
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.text.set
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.woynex.parasayar.R
import com.woynex.parasayar.core.utils.custom_dialog.AccountGroupsDialog
import com.woynex.parasayar.core.utils.showSnackBar
import com.woynex.parasayar.databinding.FragmentAddAccountBinding
import com.woynex.parasayar.feature_accounts.domain.model.Account
import com.woynex.parasayar.feature_accounts.domain.model.AccountDto
import com.woynex.parasayar.feature_accounts.domain.model.AccountGroup
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddAccountFragment : Fragment(R.layout.fragment_add_account) {

    private lateinit var _binding: FragmentAddAccountBinding
    private val viewModel: AddAccountViewModel by viewModels()

    private var selectedAccountGroup: AccountGroup? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAddAccountBinding.bind(view)

        _binding.apply {
            backBtn.setOnClickListener {
                findNavController().popBackStack()
            }
            saveBtn.setOnClickListener {
                if (isValid()) {
                    viewModel.addAccount(
                        Account(
                            name = _binding.name.text.toString(),
                            group_name = selectedAccountGroup?.name!!,
                            group_id = selectedAccountGroup?.id!!,
                            deposit = 0.00,
                            withdrawal = 0.00
                        )
                    )
                    findNavController().popBackStack()
                }
            }
            group.setOnClickListener {
                showGroupList()
            }
        }
        showGroupList()
    }

    private fun showGroupList() {
        AccountGroupsDialog(viewModel.accountGroup) {
            _binding.group.setText(it.name)
            selectedAccountGroup = it
        }.show(childFragmentManager, "AccountGroup")
    }

    private fun isValid(): Boolean {
        return when {
            _binding.group.text.toString().isBlank() -> {
                requireView().showSnackBar("There is no group name")
                false
            }
            _binding.name.text.toString().isBlank() -> {
                requireView().showSnackBar("There is no name")
                false
            }
            else -> true
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