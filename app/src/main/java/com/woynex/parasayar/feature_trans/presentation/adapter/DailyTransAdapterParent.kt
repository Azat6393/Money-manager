package com.woynex.parasayar.feature_trans.presentation.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.woynex.parasayar.R
import com.woynex.parasayar.core.utils.OnItemClickListener
import com.woynex.parasayar.core.utils.maskCurrency
import com.woynex.parasayar.core.utils.millisecondToLocalDate
import com.woynex.parasayar.databinding.ItemDailyTransParentBinding
import com.woynex.parasayar.feature_trans.domain.model.DailyTrans
import com.woynex.parasayar.feature_trans.domain.model.Trans
import java.time.LocalDate

class DailyTransAdapterParent(private val listener: OnItemClickListener<Trans>) :
    ListAdapter<DailyTrans, DailyTransAdapterParent.DailyTransParentViewHolder>(
        DIFF_CALL_BACK
    ), OnItemClickListener<Trans> {

    private var context: Context? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyTransParentViewHolder {
        context = parent.context
        return DailyTransParentViewHolder(
            ItemDailyTransParentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: DailyTransParentViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bind(item)
        }
    }

    inner class DailyTransParentViewHolder(private val _binding: ItemDailyTransParentBinding) :
        RecyclerView.ViewHolder(_binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(item: DailyTrans) {
            _binding.apply {
                dayTv.text = item.day
                dayOfWeekTv.text = item.dayOfWeek
                dateTv.text = item.date
                incomeTv.text = "${item.arrayList[0].currency} ${item.income.maskCurrency()}"
                expenseTv.text = "${item.arrayList[0].currency} ${item.expenses.maskCurrency()}"
                val transAdapter = DailyTransAdapterChild(this@DailyTransAdapterParent)
                transRv.apply {
                    adapter = transAdapter
                    layoutManager = LinearLayoutManager(context)
                    setHasFixedSize(true)
                }
                transAdapter.submitList(item.arrayList)
                if (item.arrayList.isNotEmpty()){
                    if (millisecondToLocalDate(item.arrayList[0].date_in_millis) == LocalDate.now()) {
                        dayOfWeekTv.setBackgroundResource(R.drawable.radio_button_selected)
                        dayOfWeekTv.setTextColor(Color.WHITE)
                    }
                }
            }
        }
    }

    companion object {
        val DIFF_CALL_BACK = object : DiffUtil.ItemCallback<DailyTrans>() {
            override fun areItemsTheSame(oldItem: DailyTrans, newItem: DailyTrans): Boolean {
                return oldItem.day == newItem.day
            }

            override fun areContentsTheSame(oldItem: DailyTrans, newItem: DailyTrans): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onClick(item: Trans) {
        listener.onClick(item)
    }
}