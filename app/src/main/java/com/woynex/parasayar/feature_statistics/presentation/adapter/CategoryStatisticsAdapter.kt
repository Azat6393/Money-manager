package com.woynex.parasayar.feature_statistics.presentation.adapter

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
import com.woynex.parasayar.databinding.ItemCategoryStatisticsBinding
import com.woynex.parasayar.feature_statistics.domain.model.CategoryStatistics

class CategoryStatisticsAdapter(private val listener: OnItemClickListener<CategoryStatistics>) :
    ListAdapter<CategoryStatistics, CategoryStatisticsAdapter.CategoryStatisticsViewHolder>(
        DIFF_CALL_BACK
    ) {

    private var context: Context? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CategoryStatisticsViewHolder {
        context = parent.context
        return CategoryStatisticsViewHolder(
            ItemCategoryStatisticsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CategoryStatisticsViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bind(item)
        }
    }

    inner class CategoryStatisticsViewHolder(private val _binding: ItemCategoryStatisticsBinding) :
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
        fun bind(item: CategoryStatistics) {
            _binding.apply {
                percentageTv.text = "% ${item.percentage}"
                categoryNameTv.text = item.category_name
                amountTv.text = "${item.currency} ${item.total_amount.maskCurrency()}"
                percentageCd.background.level = item.percentage
                percentageCd.setCardBackgroundColor(item.color)
                if(item.percentage <= 10){
                    context?.getColor(R.color.red)?.let {
                        percentageTv.setTextColor(
                            it
                        )
                    }
                }
            }
        }
    }

    companion object {
        private val DIFF_CALL_BACK = object : DiffUtil.ItemCallback<CategoryStatistics>() {
            override fun areItemsTheSame(
                oldItem: CategoryStatistics,
                newItem: CategoryStatistics
            ): Boolean {
                return oldItem.category_id == newItem.category_id
            }

            override fun areContentsTheSame(
                oldItem: CategoryStatistics,
                newItem: CategoryStatistics
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}