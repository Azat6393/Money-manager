package com.woynex.parasayar.feature_trans.presentation.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.woynex.parasayar.core.utils.parseMonth
import com.woynex.parasayar.core.utils.parseWeek
import com.woynex.parasayar.databinding.ItemWeeksMonthsTransBinding
import com.woynex.parasayar.feature_trans.domain.model.WeekTrans
import com.woynex.parasayar.feature_trans.domain.model.YearTrans

class WeekTransAdapter :
    ListAdapter<WeekTrans, WeekTransAdapter.YearTransViewHolder>(DIFF_CALL_BACK) {

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
            fun bind(item: WeekTrans){
                _binding.apply {
                    titleTv.text = "${parseWeek(item.startWeek)} ~ ${parseWeek(item.endWeek)}"
                    incomeTv.text = "\$ ${item.income}"
                    expenceTv.text ="\$ ${item.expense}"
                    totalTv.text = "\$ ${item.total}"
                }
            }
    }

    companion object {
        val DIFF_CALL_BACK = object : DiffUtil.ItemCallback<WeekTrans>() {
            override fun areItemsTheSame(oldItem: WeekTrans, newItem: WeekTrans): Boolean {
                return oldItem.startWeek == newItem.startWeek
            }

            override fun areContentsTheSame(oldItem: WeekTrans, newItem: WeekTrans): Boolean {
                return oldItem == newItem
            }
        }
    }
}