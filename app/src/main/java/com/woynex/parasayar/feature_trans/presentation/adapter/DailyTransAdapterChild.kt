package com.woynex.parasayar.feature_trans.presentation.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.woynex.parasayar.R
import com.woynex.parasayar.core.utils.OnItemClickListener
import com.woynex.parasayar.core.utils.TransTypes
import com.woynex.parasayar.databinding.ItemDailyTransChildBinding
import com.woynex.parasayar.databinding.ItemDailyTransParentBinding
import com.woynex.parasayar.feature_trans.domain.model.DailyTrans
import com.woynex.parasayar.feature_trans.domain.model.Trans

class DailyTransAdapterChild(private val listener: OnItemClickListener<Trans>) :
    ListAdapter<Trans, DailyTransAdapterChild.DailyTransChildViewHolder>(
        DIFF_CALL_BACK
    ) {

    private var context: Context? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyTransChildViewHolder {
        context = parent.context
        return DailyTransChildViewHolder(
            ItemDailyTransChildBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: DailyTransChildViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bind(item)
        }
    }

    inner class DailyTransChildViewHolder(private val _binding: ItemDailyTransChildBinding) :
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
        fun bind(item: Trans) {
            _binding.apply {
                categoryNameTv.text = item.category
                if (!item.subcategory.isNullOrBlank()) {
                    subcategoryNameTv.isVisible = true
                    subcategoryNameTv.text = item.subcategory
                }
                amountNameTv.text = "${item.currency} ${item.amount}"
                noteTv.text = item.note
                noteTv.isVisible = item.note.isNotBlank()
                hasImageIv.isVisible = item.photo != null
                when (item.type) {
                    TransTypes.INCOME -> {
                        amountNameTv.setTextColor(ContextCompat.getColor(context!!, R.color.blue))
                        transferContainer.isVisible = false
                        accountNameTv.isVisible = true
                        accountNameTv.text = item.account_name
                    }
                    TransTypes.EXPENSE -> {
                        amountNameTv.setTextColor(ContextCompat.getColor(context!!, R.color.red))
                        transferContainer.isVisible = false
                        accountNameTv.isVisible = true
                        accountNameTv.text = item.account_name
                    }
                    TransTypes.TRANSFER -> {
                        categoryNameTv.text = context!!.getString(R.string.transfer)
                        amountNameTv.setTextColor(ContextCompat.getColor(context!!, R.color.black))
                        transferContainer.isVisible = true
                        accountNameTv.isVisible = false
                        fromAccountTv.text = item.account_name
                        toAccountTv.text = item.to_account_name
                    }
                }
            }
        }
    }

    companion object {
        val DIFF_CALL_BACK = object : DiffUtil.ItemCallback<Trans>() {
            override fun areItemsTheSame(oldItem: Trans, newItem: Trans): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Trans, newItem: Trans): Boolean {
                return oldItem == newItem
            }
        }
    }
}