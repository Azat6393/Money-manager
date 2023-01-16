package com.woynex.parasayar.feature_accounts.presentation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.woynex.parasayar.R
import com.woynex.parasayar.databinding.ItemAccountGroupSettingBinding
import com.woynex.parasayar.databinding.ItemAccountSettingsBinding
import com.woynex.parasayar.feature_accounts.domain.model.AccountGroup

class AccountGroupSettingAdapter(private val listener: AccountGroupSettingItemListener) :
    ListAdapter<AccountGroup, AccountGroupSettingAdapter.AccountGroupListViewHolder>(
        DIFF_CALL_BACK
    ) {

    private var context: Context? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountGroupListViewHolder {
        context = parent.context
        return AccountGroupListViewHolder(
            ItemAccountGroupSettingBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: AccountGroupListViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bind(item, position)
        }
    }

    inner class AccountGroupListViewHolder(private val _binding: ItemAccountGroupSettingBinding) :
        RecyclerView.ViewHolder(_binding.root) {

        init {
            _binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val item = getItem(position)
                    if (item != null) {
                        listener.onClick(item)
                    }
                }
            }
            _binding.cancelBtn.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val item = getItem(position)
                    if (item != null) {
                        listener.onDelete(item)
                    }
                }
            }
        }

        fun bind(accountGroup: AccountGroup, position: Int) {
            _binding.coinBtn.isVisible = position == 0 || position == 1 || position == 2
            _binding.cancelBtn.isVisible = position > 2
            _binding.nameTv.text = accountGroup.name
        }
    }

    companion object {
        private val DIFF_CALL_BACK = object : DiffUtil.ItemCallback<AccountGroup>() {
            override fun areItemsTheSame(oldItem: AccountGroup, newItem: AccountGroup): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: AccountGroup, newItem: AccountGroup): Boolean {
                return oldItem == newItem
            }
        }
    }

    interface AccountGroupSettingItemListener {
        fun onClick(accountGroup: AccountGroup)
        fun onDelete(accountGroup: AccountGroup)
    }
}