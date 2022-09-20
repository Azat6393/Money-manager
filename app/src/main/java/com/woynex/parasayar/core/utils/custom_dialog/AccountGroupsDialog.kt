package com.woynex.parasayar.core.utils.custom_dialog

import android.content.DialogInterface
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
import com.woynex.parasayar.core.utils.adapter.AccountGroupAdapter
import com.woynex.parasayar.databinding.DialogAccountGroupsBinding
import com.woynex.parasayar.feature_accounts.domain.model.AccountGroup
import kotlinx.coroutines.flow.StateFlow

class AccountGroupsDialog(
    private val accountGroups: StateFlow<List<AccountGroup>>,
    private val selectedCountry: (AccountGroup) -> Unit
) : DialogFragment(),
    AccountGroupAdapter.OnItemClickListener {

    private lateinit var _binding: DialogAccountGroupsBinding
    private val mAdapter: AccountGroupAdapter by lazy {
        AccountGroupAdapter(this)
    }

    private var selected = false
    private var groupList = listOf<AccountGroup>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_account_groups, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = DialogAccountGroupsBinding.bind(view)

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
                accountGroups.collect { result ->
                    mAdapter.submitList(result)
                    groupList = result
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

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        if (groupList.isNotEmpty()) {
            if (!selected) {
                selectedCountry(groupList[0])
            }
        }
    }

    override fun onClick(accountGroup: AccountGroup) {
        selected = true
        selectedCountry(accountGroup)
        this.dismiss()
    }
}