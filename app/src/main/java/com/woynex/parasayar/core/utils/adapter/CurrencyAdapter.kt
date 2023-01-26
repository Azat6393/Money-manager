package com.woynex.parasayar.core.utils.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.woynex.parasayar.core.domain.model.Currency
import com.woynex.parasayar.databinding.ItemCurrencyDetailsBinding

class CurrencyAdapter(private val listener: CurrencyItemListener, private val deleteMode: Boolean) :
    ListAdapter<Currency, CurrencyAdapter.CurrencyViewHolder>(DIFF_CALL_BACK) {

    var defaultCurrency: Currency? = null

    fun updateDefaultCurrency(currency: Currency) {
        defaultCurrency = currency
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyViewHolder {
        return CurrencyViewHolder(
            ItemCurrencyDetailsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: CurrencyViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bind(item)
        }
    }

    inner class CurrencyViewHolder(private val _binding: ItemCurrencyDetailsBinding) :
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

        @SuppressLint("SetTextI18n")
        fun bind(item: Currency) {
            _binding.apply {
                deleteBtn.isVisible = deleteMode
                currencyDetails.text = "${item.cc} - ${item.name} (${item.symbol})"
                dotIcon.isVisible = item.symbol == defaultCurrency?.symbol
            }
        }
    }

    companion object {
        private val DIFF_CALL_BACK = object : DiffUtil.ItemCallback<Currency>() {
            override fun areItemsTheSame(oldItem: Currency, newItem: Currency): Boolean {
                return oldItem.cc == newItem.cc
            }

            override fun areContentsTheSame(oldItem: Currency, newItem: Currency): Boolean {
                return oldItem == newItem
            }
        }
    }

    interface CurrencyItemListener {
        fun onClick(item: Currency)
        fun onDelete(item: Currency)
    }
}