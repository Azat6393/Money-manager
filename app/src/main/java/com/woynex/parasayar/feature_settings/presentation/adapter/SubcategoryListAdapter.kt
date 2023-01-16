package com.woynex.parasayar.feature_settings.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.woynex.parasayar.databinding.ItemSubcategorySettingBinding
import com.woynex.parasayar.feature_settings.domain.model.SubCategory

class SubcategoryListAdapter(private val listener: SubcategoryListItemListener) :
    ListAdapter<SubCategory, SubcategoryListAdapter.SubcategoryListViewHolder>(
        DIFF_CALL_BACK
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubcategoryListViewHolder {
        return SubcategoryListViewHolder(
            ItemSubcategorySettingBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: SubcategoryListViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bind(item)
        }
    }

    inner class SubcategoryListViewHolder(private val _binding: ItemSubcategorySettingBinding) :
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

        fun bind(item: SubCategory) {
            _binding.subcategoryNameTv.text = item.name
        }
    }

    companion object {
        private val DIFF_CALL_BACK = object : DiffUtil.ItemCallback<SubCategory>() {
            override fun areItemsTheSame(
                oldItem: SubCategory,
                newItem: SubCategory
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: SubCategory,
                newItem: SubCategory
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    interface SubcategoryListItemListener {
        fun onClick(item: SubCategory)
        fun onDelete(item: SubCategory)
    }
}