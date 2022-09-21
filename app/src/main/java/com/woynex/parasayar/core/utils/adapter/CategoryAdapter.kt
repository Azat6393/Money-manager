package com.woynex.parasayar.core.utils.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.woynex.parasayar.databinding.ItemCategoryBinding
import com.woynex.parasayar.feature_settings.domain.model.CategoryWithSubCategories
import com.woynex.parasayar.feature_settings.domain.model.SubCategory

class CategoryAdapter(private val listener: CategoryOnItemClickListener) :
    ListAdapter<CategoryWithSubCategories, CategoryAdapter.CategoryViewHolder>(DiffCallBack) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        return CategoryViewHolder(
            ItemCategoryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bind(item)
        }
    }

    inner class CategoryViewHolder(private val _binding: ItemCategoryBinding) :
        RecyclerView.ViewHolder(_binding.root) {

        init {
            _binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val item = getItem(position)
                    if (item != null) {
                        if (item.subCategoryList.isEmpty()) {
                            listener.onSelect(item.category.name)
                        } else {
                            listener.onClickCategory(item.subCategoryList)
                        }
                    }
                }
            }
        }

        fun bind(item: CategoryWithSubCategories) {
            _binding.apply {
                subcategoryBtn.isVisible = item.subCategoryList.isNotEmpty()
                name.text = item.category.name
            }
        }
    }

    companion object {
        private val DiffCallBack = object : DiffUtil.ItemCallback<CategoryWithSubCategories>() {
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

    interface CategoryOnItemClickListener {
        fun onClickCategory(subCategories: List<SubCategory>)
        fun onSelect(category: String)
    }
}