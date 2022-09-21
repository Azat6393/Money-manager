package com.woynex.parasayar.core.utils.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.woynex.parasayar.core.utils.OnItemClickListener
import com.woynex.parasayar.databinding.ItemAccoutBinding
import com.woynex.parasayar.feature_accounts.domain.model.Account


class AccountAdapter(private val listener: OnItemClickListener<Account>) :
    ListAdapter<Account, AccountAdapter.AccountViewHolder>(DiffCallBack) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountViewHolder {
        return AccountViewHolder(
            ItemAccoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: AccountViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bind(item)
        }
    }

    inner class AccountViewHolder(private val _binding: ItemAccoutBinding) :
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

        fun bind(item: Account) {
            _binding.apply {
                _binding.name.text = item.name
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