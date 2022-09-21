package com.woynex.parasayar.core.utils.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.woynex.parasayar.core.utils.OnItemClickListener
import com.woynex.parasayar.databinding.ItemSubcategoryBinding
import com.woynex.parasayar.feature_settings.domain.model.SubCategory

class SubCategoryAdapter(private val listener: OnItemClickListener<SubCategory>) :
    ListAdapter<SubCategory, SubCategoryAdapter.SubCategoryViewHolder>(DiffCallBack) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubCategoryViewHolder {
        return SubCategoryViewHolder(
            ItemSubcategoryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: SubCategoryViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bind(item)
        }
    }

    inner class SubCategoryViewHolder(private val _binding: ItemSubcategoryBinding) :
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

        fun bind(item: SubCategory) {
            _binding.apply {
                name.text = item.name
            }
        }
    }

    companion object {
        private val DiffCallBack = object : DiffUtil.ItemCallback<SubCategory>() {
            override fun areItemsTheSame(oldItem: SubCategory, newItem: SubCategory): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: SubCategory, newItem: SubCategory): Boolean {
                return oldItem == newItem
            }
        }
    }
}