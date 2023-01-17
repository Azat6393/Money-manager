package com.woynex.parasayar.core.utils.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.woynex.parasayar.databinding.ItemCategoryBinding
import com.woynex.parasayar.feature_settings.domain.model.Category

class AllCategoryAdapter(private val listener: AllCategoryOnItemClickListener) :
    ListAdapter<Category, AllCategoryAdapter.CategoryViewHolder>(DiffCallBack) {

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
                        listener.onSelect(item)
                    }
                }
            }
        }

        fun bind(item: Category) {
            _binding.apply {
                name.text = item.name
            }
        }
    }

    companion object {
        private val DiffCallBack = object : DiffUtil.ItemCallback<Category>() {
            override fun areItemsTheSame(
                oldItem: Category,
                newItem: Category
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: Category,
                newItem: Category
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    interface AllCategoryOnItemClickListener {
        fun onSelect(category: Category)
    }
}