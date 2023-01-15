package com.woynex.parasayar.feature_accounts.presentation.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.woynex.parasayar.databinding.ItemAccountSettingsBinding
import com.woynex.parasayar.feature_accounts.domain.model.Account

class AccountListAdapter(private val listener: AccountSettingItemListener) :
    ListAdapter<Account, AccountListAdapter.AccountListViewHolder>(
        DIFF_CALL_BACK
    ) {

    private var deleteMode = false

    @SuppressLint("NotifyDataSetChanged")
    fun isDeleteMode(mode: Boolean) {
        deleteMode = mode
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountListViewHolder {
        return AccountListViewHolder(
            ItemAccountSettingsBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: AccountListViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            if (position > 0) {
                val currentAccount = item.group_name
                val previousAccount = getItem(position - 1).group_name
                when {
                    previousAccount.isBlank() -> {
                        var total = 0.0
                        currentList.forEach {
                            if (it.group_name == item.group_name) {
                                total += it.deposit
                                total -= +it.withdrawal
                            }
                        }
                        holder.bind(item, hasHeader = true)
                    }
                    currentAccount == previousAccount -> {
                        holder.bind(item, hasHeader = false)
                    }
                    else -> {
                        var total = 0.0
                        currentList.forEach {
                            if (it.group_name == item.group_name) {
                                total += it.deposit
                                total -= +it.withdrawal
                            }
                        }
                        holder.bind(item, hasHeader = true)
                    }
                }
            } else {
                var total = 0.0
                currentList.forEach {
                    if (it.group_name == item.group_name) {
                        total += it.deposit
                        total -= +it.withdrawal
                    }
                }
                holder.bind(item, hasHeader = true)
            }
        }
    }

    inner class AccountListViewHolder(private val _binding: ItemAccountSettingsBinding) :
        RecyclerView.ViewHolder(_binding.root) {

        init {
            _binding.root.setOnClickListener {
                if (!deleteMode) {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val item = getItem(position)
                        if (item != null) {
                            listener.onClick(item)
                        }
                    }
                }
            }
            _binding.deleteBtn.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val item = getItem(position)
                    if (item != null) {
                        listener.onDelete(item)
                    }
                }
            }
        }

        fun bind(account: Account, hasHeader: Boolean) {
            _binding.apply {
                accountName.text = account.name
                deleteBtn.visibility = if (deleteMode) View.VISIBLE else View.GONE
                rightBtn.isVisible = !deleteMode
                header.isVisible = hasHeader
                accountGroupName.text = account.group_name
                accountName.text = account.name
            }
        }
    }

    companion object {
        val DIFF_CALL_BACK = object : DiffUtil.ItemCallback<Account>() {
            override fun areItemsTheSame(oldItem: Account, newItem: Account): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Account, newItem: Account): Boolean {
                return oldItem == newItem
            }
        }
    }

    interface AccountSettingItemListener {
        fun onClick(item: Account)
        fun onDelete(item: Account)
    }
}