package com.woynex.parasayar.feature_trans.presentation.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.woynex.parasayar.core.utils.OnItemClickListener
import com.woynex.parasayar.core.utils.maskCurrency
import com.woynex.parasayar.databinding.ItemBudgetSettingChildBinding
import com.woynex.parasayar.feature_trans.domain.model.SubcategoryBudget

class BudgetChildSettingAdapter(private val listener: OnItemClickListener<SubcategoryBudget>) :
    ListAdapter<SubcategoryBudget, BudgetChildSettingAdapter.BudgetChildSettingViewHolder>(
        DIFF_CALL_BACK
    ) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BudgetChildSettingViewHolder {
        return BudgetChildSettingViewHolder(
            ItemBudgetSettingChildBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: BudgetChildSettingViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bind(item)
        }
    }

    inner class BudgetChildSettingViewHolder(private val _binding: ItemBudgetSettingChildBinding) :
        RecyclerView.ViewHolder(_binding.root) {

        init {
            _binding.closeBtn.setOnClickListener {
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
        fun bind(item: SubcategoryBudget) {
            _binding.apply {
                categoryNameTv.text = item.subcategory_name
                amount.text = "${item.currency} ${item.amount.maskCurrency()}"
            }
        }
    }

    companion object {
        private val DIFF_CALL_BACK = object : DiffUtil.ItemCallback<SubcategoryBudget>() {
            override fun areItemsTheSame(
                oldItem: SubcategoryBudget,
                newItem: SubcategoryBudget
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: SubcategoryBudget,
                newItem: SubcategoryBudget
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}