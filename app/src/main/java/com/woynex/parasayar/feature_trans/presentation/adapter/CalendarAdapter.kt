package com.woynex.parasayar.feature_trans.presentation.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.woynex.parasayar.core.utils.OnItemClickListener
import com.woynex.parasayar.core.utils.maskCurrency
import com.woynex.parasayar.databinding.ItemCalendarDayBinding
import com.woynex.parasayar.feature_trans.domain.model.CalendarDay
import java.time.LocalDate

class CalendarAdapter(private val listener: OnItemClickListener<CalendarDay>) :
    ListAdapter<CalendarDay, CalendarAdapter.CalendarViewHolder>(DiffCallBack) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarViewHolder {
        val binding = CalendarViewHolder(
            ItemCalendarDayBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
          val layoutParams = binding.itemView.layoutParams
          if (currentList.size > 15) {
              layoutParams.height = (parent.height * 0.166666666).toInt()
          } else {
              layoutParams.height = parent.height
          }
        return binding
    }

    override fun onBindViewHolder(holder: CalendarViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bind(item)
        } else {
            holder.emptyDate()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    inner class CalendarViewHolder(private val _binding: ItemCalendarDayBinding) :
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

        fun bind(item: CalendarDay) {
            _binding.dayTv.text = item.date.dayOfMonth.toString()
            if (item.date.dayOfMonth == LocalDate.now().dayOfMonth
                && item.date.year == LocalDate.now().year
                && item.date.monthValue == LocalDate.now().monthValue
            ) {
                _binding.dayTv.setBackgroundColor(Color.parseColor("#FF6750A4"))
                _binding.dayTv.setTextColor(Color.WHITE)
            }
            if (item.income != null && item.expense != null && item.total != null) {
                _binding.incomeTv.isVisible = true
                _binding.expenceTv.isVisible = true
                _binding.totalTv.isVisible = true
                _binding.incomeTv.text = item.income.maskCurrency()
                _binding.expenceTv.text = item.expense.maskCurrency()
                _binding.totalTv.text = item.total.maskCurrency()
            }
        }

        fun emptyDate() {
            _binding.dayTv.text = ""
        }
    }

    companion object {
        private val DiffCallBack = object : DiffUtil.ItemCallback<CalendarDay>() {
            override fun areItemsTheSame(oldItem: CalendarDay, newItem: CalendarDay): Boolean {
                return oldItem.date == newItem.date
            }

            override fun areContentsTheSame(oldItem: CalendarDay, newItem: CalendarDay): Boolean {
                return oldItem == newItem
            }
        }
    }
}