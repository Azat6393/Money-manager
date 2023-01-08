package com.woynex.parasayar.feature_trans.presentation.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.woynex.parasayar.core.utils.parseMonth
import com.woynex.parasayar.databinding.ItemWeeksMonthsTransBinding
import com.woynex.parasayar.feature_trans.domain.model.YearTrans

class YearTransAdapter :
    ListAdapter<YearTrans, YearTransAdapter.YearTransViewHolder>(DIFF_CALL_BACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): YearTransViewHolder {
        return YearTransViewHolder(
            ItemWeeksMonthsTransBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: YearTransViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null){
            holder.bind(item)
        }
    }

    inner class YearTransViewHolder(private val _binding: ItemWeeksMonthsTransBinding) :
        RecyclerView.ViewHolder(_binding.root) {

            @SuppressLint("SetTextI18n")
            fun bind(item: YearTrans){
                _binding.apply {
                    titleTv.text = parseMonth(item.date)
                    incomeTv.text = "\$ ${item.income}"
                    expenceTv.text ="\$ ${item.expence}"
                    totalTv.text = "\$ ${item.total}"
                }
            }
    }

    companion object {
        val DIFF_CALL_BACK = object : DiffUtil.ItemCallback<YearTrans>() {
            override fun areItemsTheSame(oldItem: YearTrans, newItem: YearTrans): Boolean {
                return oldItem.date == newItem.date
            }

            override fun areContentsTheSame(oldItem: YearTrans, newItem: YearTrans): Boolean {
                return oldItem == newItem
            }
        }
    }
}