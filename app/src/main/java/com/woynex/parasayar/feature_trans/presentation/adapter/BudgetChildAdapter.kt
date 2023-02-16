package com.woynex.parasayar.feature_trans.presentation.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.woynex.parasayar.R
import com.woynex.parasayar.core.utils.OnItemClickListener
import com.woynex.parasayar.core.utils.maskCurrency
import com.woynex.parasayar.databinding.ItemBudgetChildBinding
import com.woynex.parasayar.feature_trans.domain.model.BudgetItem

class BudgetChildAdapter(private val listener: OnItemClickListener<BudgetItem>) :
    ListAdapter<BudgetItem, BudgetChildAdapter.BudgetChildViewHolder>(
        DIFF_CALL_BACK
    ) {

    private var context: Context? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BudgetChildViewHolder {
        context = parent.context
        return BudgetChildViewHolder(
            ItemBudgetChildBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: BudgetChildViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bind(item)
        }
    }

    inner class BudgetChildViewHolder(private val _binding: ItemBudgetChildBinding) :
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

        @SuppressLint("SetTextI18n")
        fun bind(item: BudgetItem) {
            _binding.apply {
                subcategoryNameTv.text = item.name
                totalAmount.text = "${item.currency} ${item.budgetAmount.maskCurrency()}"
                expensesTv.text = "${item.currency} ${item.expenses.maskCurrency()}"
                percentageTv.text = "${item.percentage}%"
                progressBar.progress = item.percentage
                if (item.percentage >= 95) {
                    if (context != null) {
                        percentageTv.setTextColor(context!!.getColor(R.color.white))
                    }
                }
                if (item.expenses > item.budgetAmount) {
                    if (context != null) {
                        expensesTv.setTextColor(context!!.getColor(R.color.red))
                        totalTv.text =
                            "${context!!.getText(R.string.excess)} ${item.currency} ${(item.budgetAmount - item.expenses).maskCurrency()}"
                    }
                } else {
                    totalTv.text = "${item.currency} ${(item.budgetAmount - item.expenses).maskCurrency()}"
                }
            }
        }
    }

    companion object {
        private val DIFF_CALL_BACK = object : DiffUtil.ItemCallback<BudgetItem>() {
            override fun areItemsTheSame(oldItem: BudgetItem, newItem: BudgetItem): Boolean {
                return oldItem.name == newItem.name
            }

            override fun areContentsTheSame(oldItem: BudgetItem, newItem: BudgetItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}