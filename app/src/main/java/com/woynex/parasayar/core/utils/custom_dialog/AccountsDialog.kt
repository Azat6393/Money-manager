package com.woynex.parasayar.core.utils.custom_dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.woynex.parasayar.R
import com.woynex.parasayar.core.utils.OnItemClickListener
import com.woynex.parasayar.core.utils.adapter.AccountAdapter
import com.woynex.parasayar.databinding.DialogAccountsBinding
import com.woynex.parasayar.feature_accounts.domain.model.Account
import kotlinx.coroutines.flow.StateFlow

class AccountsDialog(
    private val accounts: StateFlow<List<Account>>,
    private val selectedAccount: (Account) -> Unit
) : DialogFragment(), OnItemClickListener<Account> {

    private lateinit var _binding: DialogAccountsBinding
    private val mAdapter: AccountAdapter by lazy {
        AccountAdapter(this)
    }

    private var selected = false
    private var accountList = listOf<Account>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_accounts, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = DialogAccountsBinding.bind(view)

        _binding.recyclerView.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
        observe()
    }

    private fun observe() {
        lifecycleScope.launchWhenStarted {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                accounts.collect { result ->
                    mAdapter.submitList(result)
                    accountList = result
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
    }

    override fun onClick(item: Account) {
        selected = true
        selectedAccount(item)
        this.dismiss()
    }
}