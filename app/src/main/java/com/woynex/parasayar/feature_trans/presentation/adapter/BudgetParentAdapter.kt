package com.woynex.parasayar.feature_trans.presentation.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.woynex.parasayar.R
import com.woynex.parasayar.core.utils.OnItemClickListener
import com.woynex.parasayar.core.utils.maskCurrency
import com.woynex.parasayar.databinding.ItemBudgetParentBinding
import com.woynex.parasayar.feature_trans.domain.model.Budget
import com.woynex.parasayar.feature_trans.domain.model.BudgetItem

class BudgetParentAdapter(private val listener: OnItemClickListener) :
    ListAdapter<Budget, BudgetParentAdapter.BudgetParentViewHolder>(
        DIFF_CALL_BACK
    ), OnItemClickListener<BudgetItem> {

    private var childViewState = false

    private var context: Context? = null


    @SuppressLint("NotifyDataSetChanged")
    fun updateChildViewState(state: Boolean) {
        childViewState = state
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BudgetParentViewHolder {
        context = parent.context
        return BudgetParentViewHolder(
            ItemBudgetParentBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: BudgetParentViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bind(item)
        }
    }

    inner class BudgetParentViewHolder(private val _binding: ItemBudgetParentBinding) :
        RecyclerView.ViewHolder(_binding.root) {

        init {
            _binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val item = getItem(position)
                    if (item != null) {
                        listener.selectCategory(item)
                    }
                }
            }
            _binding.categoryContainer.setOnClickListener {
                _binding.childRv.isVisible = !_binding.childRv.isVisible
                if (context != null){
                    _binding.arrowIcon.setImageDrawable(
                        if (_binding.childRv.isVisible) ContextCompat.getDrawable(context!!, R.drawable.ic_baseline_arrow_drop_up_24)
                        else ContextCompat.getDrawable(context!!, R.drawable.ic_baseline_arrow_drop_down_24)
                    )
                }
            }
        }

        @SuppressLint("SetTextI18n")
        fun bind(item: Budget) {
            _binding.apply {
                arrowIcon.setImageDrawable(
                    if (childViewState) ContextCompat.getDrawable(context!!, R.drawable.ic_baseline_arrow_drop_up_24)
                    else ContextCompat.getDrawable(context!!, R.drawable.ic_baseline_arrow_drop_down_24)
                )
                arrowIcon.isVisible = item.subcategoryBudget.isNotEmpty()
                childRv.isVisible = childViewState
                categoryNameTv.text = item.categoryBudget.name
                totalAmount.text =
                    "${item.categoryBudget.currency} ${item.categoryBudget.budgetAmount.maskCurrency()}"
                expensesTv.text =
                    "${item.categoryBudget.currency} ${item.categoryBudget.expenses.maskCurrency()}"
                percentageTv.text = "${item.categoryBudget.percentage}%"
                progressBar.progress = item.categoryBudget.percentage
                if (item.categoryBudget.percentage >= 95) {
                    if (context != null) {
                        percentageTv.setTextColor(context!!.getColor(R.color.white))
                    }
                }
                if (item.categoryBudget.expenses > item.categoryBudget.budgetAmount) {
                    if (context != null) {
                        expensesTv.setTextColor(context!!.getColor(R.color.red))
                        totalTv.text =
                            "${context!!.getText(R.string.excess)} ${item.categoryBudget.currency} ${(item.categoryBudget.budgetAmount - item.categoryBudget.expenses).maskCurrency()}"
                    }
                } else {
                    totalTv.text =
                        "${item.categoryBudget.currency} ${(item.categoryBudget.budgetAmount - item.categoryBudget.expenses).maskCurrency()}"
                }
                val mAdapter = BudgetChildAdapter(this@BudgetParentAdapter)
                childRv.apply {
                    adapter = mAdapter
                    layoutManager = LinearLayoutManager(context)
                    setHasFixedSize(true)
                }
                mAdapter.submitList(item.subcategoryBudget)
            }
        }
    }

    companion object {
        private val DIFF_CALL_BACK = object : DiffUtil.ItemCallback<Budget>() {
            override fun areItemsTheSame(oldItem: Budget, newItem: Budget): Boolean {
                return oldItem.categoryBudget.name == newItem.categoryBudget.name
            }

            override fun areContentsTheSame(oldItem: Budget, newItem: Budget): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onClick(item: BudgetItem) {
        listener.selectSubcategory(item)
    }

    interface OnItemClickListener {
        fun selectCategory(budget: Budget)
        fun selectSubcategory(budgetItem: BudgetItem)
    }
}