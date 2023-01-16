package com.woynex.parasayar.feature_settings.presentation.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.woynex.parasayar.databinding.ItemCategorySettingBinding
import com.woynex.parasayar.feature_settings.domain.model.CategoryWithSubCategories

class CategoryListAdapter(private val listener: CategoryListItemListener) :
    ListAdapter<CategoryWithSubCategories, CategoryListAdapter.CategoryListViewHolder>(
        DIFF_CALL_BACK
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryListViewHolder {
        return CategoryListViewHolder(
            ItemCategorySettingBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: CategoryListViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bind(item)
        }
    }

    inner class CategoryListViewHolder(private val _binding: ItemCategorySettingBinding) :
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

        @SuppressLint("SetTextI18n")
        fun bind(item: CategoryWithSubCategories) {
            if (item.subCategoryList.isEmpty())
                _binding.categoryNameTv.text = item.category.name
            else _binding.categoryNameTv.text =
                "${item.category.name} (${item.subCategoryList.size})"
            _binding.subcategoryNameTv.isVisible = item.subCategoryList.isNotEmpty()
            _binding.subcategoryNameTv.text = item.subCategoryList.joinToString { it.name }
        }
    }

    companion object {
        private val DIFF_CALL_BACK = object : DiffUtil.ItemCallback<CategoryWithSubCategories>() {
            override fun areItemsTheSame(
                oldItem: CategoryWithSubCategories,
                newItem: CategoryWithSubCategories
            ): Boolean {
                return oldItem.category.id == newItem.category.id
            }

            override fun areContentsTheSame(
                oldItem: CategoryWithSubCategories,
                newItem: CategoryWithSubCategories
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    interface CategoryListItemListener {
        fun onClick(categoryWithSubCategories: CategoryWithSubCategories)
        fun onDelete(categoryWithSubCategories: CategoryWithSubCategories)
    }
}