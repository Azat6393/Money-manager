package com.woynex.parasayar.core.utils.custom_dialog

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.woynex.parasayar.R
import com.woynex.parasayar.core.utils.OnItemClickListener
import com.woynex.parasayar.core.utils.adapter.AccountAdapter
import com.woynex.parasayar.databinding.DialogAccountsBinding
import com.woynex.parasayar.feature_accounts.domain.model.Account
import kotlinx.coroutines.flow.StateFlow

class AccountsDialog(
    private val context: Context,
    private val _binding: DialogAccountsBinding,
    private val accounts: List<Account>,
    private val onClose: () -> Unit,
    private val onEdit: () -> Unit,
    private val selectedAccount: (Account) -> Unit
) : OnItemClickListener<Account> {

    private val mAdapter: AccountAdapter by lazy {
        AccountAdapter(this)
    }

    private var selected = false

    init {
        initView()
    }

    fun initView() {
        _binding.recyclerView.apply {
            adapter = mAdapter
            val mLayoutManager = GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false)
            layoutManager = mLayoutManager
            addItemDecoration(
                DividerItemDecoration(
                    context,
                    DividerItemDecoration.HORIZONTAL
                )
            )
            addItemDecoration(
                DividerItemDecoration(
                    context,
                    DividerItemDecoration.VERTICAL
                )
            )
            setHasFixedSize(true)
        }
        _binding.closeBtn.setOnClickListener { invisible() }
        _binding.editBtn.setOnClickListener { onEdit() }
        observe()
    }

    private fun observe() {
        mAdapter.submitList(accounts)
    }

    fun visible() {
        _binding.root.isVisible = true
    }

    fun invisible() {
        _binding.root.isVisible = false
        onClose()
    }

    override fun onClick(item: Account) {
        selected = true
        selectedAccount(item)
        invisible()
    }
}