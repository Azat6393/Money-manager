package com.woynex.parasayar.feature_accounts.presentation.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.woynex.parasayar.core.utils.OnItemClickListener
import com.woynex.parasayar.databinding.ItemAccountBinding
import com.woynex.parasayar.feature_accounts.domain.model.Account

class AccountAdapter(private val listener: OnItemClickListener<Account>) :
    ListAdapter<Account, AccountAdapter.AccountViewHolder>(DiffCallBack) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountViewHolder {
        return AccountViewHolder(
            ItemAccountBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: AccountViewHolder, position: Int) {
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
                        holder.bind(item, hasHeader = true, total)
                    }
                    currentAccount == previousAccount -> {
                        holder.bind(item, hasHeader = false, 0.0)
                    }
                    else -> {
                        var total = 0.0
                        currentList.forEach {
                            if (it.group_name == item.group_name) {
                                total += it.deposit
                                total -= +it.withdrawal
                            }
                        }
                        holder.bind(item, hasHeader = true, total)
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
                holder.bind(item, hasHeader = true, total)
            }
        }
    }

    inner class AccountViewHolder(private val _binding: ItemAccountBinding) :
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
        }

        fun bind(account: Account, hasHeader: Boolean, total: Double) {
            _binding.apply {
                header.isVisible = hasHeader
                accountGroupName.text = account.group_name
                accountName.text = account.name
                val amount = account.deposit - account.withdrawal
                accountAmount.text = amount.toString()
                accountAmount.setTextColor(
                    if (amount >= 0) Color.parseColor("#3a86ff")
                    else Color.parseColor("#e63946")
                )
                totalAmount.setTextColor(
                    if (total >= 0) Color.parseColor("#3a86ff")
                    else Color.parseColor("#e63946")
                )
                totalAmount.text = total.toString()
            }
        }
    }

    companion object {
        private val DiffCallBack = object : DiffUtil.ItemCallback<Account>() {
            override fun areItemsTheSame(oldItem: Account, newItem: Account): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Account, newItem: Account): Boolean {
                return oldItem == newItem
            }
        }
    }

}