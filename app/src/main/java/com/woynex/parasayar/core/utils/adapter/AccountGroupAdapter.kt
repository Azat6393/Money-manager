package com.woynex.parasayar.core.utils.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.woynex.parasayar.databinding.ItemAccoutGroupBinding
import com.woynex.parasayar.feature_accounts.domain.model.AccountGroup


class AccountGroupAdapter(private val listener: OnItemClickListener) :
    ListAdapter<AccountGroup, AccountGroupAdapter.AccountGroupViewHolder>(DiffCallBack) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountGroupViewHolder {
        return AccountGroupViewHolder(
            ItemAccoutGroupBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: AccountGroupViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bind(item)
        }
    }

    inner class AccountGroupViewHolder(private val _binding: ItemAccoutGroupBinding) :
        RecyclerView.ViewHolder(_binding.root) {

        init {
            _binding.root.setOnClickListener {
                val position = adapterPosition
                val item = getItem(position)
                if (item != null) {
                    listener.onClick(item)
                }
            }
        }

        fun bind(item: AccountGroup) {
            _binding.apply {
                _binding.name.text = item.name
            }
        }
    }

    companion object {
        private val DiffCallBack = object : DiffUtil.ItemCallback<AccountGroup>() {
            override fun areItemsTheSame(oldItem: AccountGroup, newItem: AccountGroup): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: AccountGroup, newItem: AccountGroup): Boolean {
                return oldItem == newItem
            }
        }
    }

    interface OnItemClickListener {
        fun onClick(countryInfo: AccountGroup)
    }
}