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
import com.woynex.parasayar.databinding.ItemBudgetSettingParentBinding
import com.woynex.parasayar.feature_trans.domain.model.CategoryWithSubcategoryBudget
import com.woynex.parasayar.feature_trans.domain.model.SubcategoryBudget

class BudgetParentSettingAdapter(private val listener: OnItemClickListener) :
    ListAdapter<CategoryWithSubcategoryBudget, BudgetParentSettingAdapter.BudgetParentSettingViewHolder>(
        DIFF_CALL_BACK
    ), OnItemClickListener<SubcategoryBudget> {

    private var childViewState = false

    private var context: Context? = null

    @SuppressLint("NotifyDataSetChanged")
    fun updateChildViewState(state: Boolean) {
        childViewState = state
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BudgetParentSettingViewHolder {
        context = parent.context
        return BudgetParentSettingViewHolder(
            ItemBudgetSettingParentBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: BudgetParentSettingViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bind(item)
        }
    }

    inner class BudgetParentSettingViewHolder(private val _binding: ItemBudgetSettingParentBinding) :
        RecyclerView.ViewHolder(_binding.root) {

        init {
            _binding.closeBtn.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val item = getItem(position)
                    if (item != null) {
                        listener.onDeleteCategory(item)
                    }
                }
            }
            _binding.root.setOnClickListener {
                _binding.recyclerView.isVisible = !_binding.recyclerView.isVisible
                if (context != null) {
                    _binding.arrowIcon.setImageDrawable(
                        if (_binding.recyclerView.isVisible) ContextCompat.getDrawable(
                            context!!,
                            R.drawable.ic_baseline_arrow_drop_up_24
                        )
                        else ContextCompat.getDrawable(
                            context!!,
                            R.drawable.ic_baseline_arrow_drop_down_24
                        )
                    )
                }
            }
        }

        @SuppressLint("SetTextI18n")
        fun bind(item: CategoryWithSubcategoryBudget) {
            _binding.apply {
                arrowIcon.setImageDrawable(
                    if (childViewState) ContextCompat.getDrawable(
                        context!!,
                        R.drawable.ic_baseline_arrow_drop_up_24
                    )
                    else ContextCompat.getDrawable(
                        context!!,
                        R.drawable.ic_baseline_arrow_drop_down_24
                    )
                )
                arrowIcon.isVisible = item.subcategoryBudgetList.isNotEmpty()
                _binding.recyclerView.isVisible = childViewState
                categoryNameTv.text = item.categoryBudget.category_name
                amount.text = "${item.categoryBudget.currency} ${item.categoryBudget.amount}"
                val mAdapter = BudgetChildSettingAdapter(this@BudgetParentSettingAdapter)
                _binding.recyclerView.apply {
                    adapter = mAdapter
                    layoutManager = LinearLayoutManager(context)
                    setHasFixedSize(false)
                }
                mAdapter.submitList(item.subcategoryBudgetList)
            }
        }
    }

    companion object {
        private val DIFF_CALL_BACK =
            object : DiffUtil.ItemCallback<CategoryWithSubcategoryBudget>() {
                override fun areItemsTheSame(
                    oldItem: CategoryWithSubcategoryBudget,
                    newItem: CategoryWithSubcategoryBudget
                ): Boolean {
                    return oldItem.categoryBudget.id == newItem.categoryBudget.id
                }

                override fun areContentsTheSame(
                    oldItem: CategoryWithSubcategoryBudget,
                    newItem: CategoryWithSubcategoryBudget
                ): Boolean {
                    return oldItem == newItem
                }
            }
    }

    interface OnItemClickListener {
        fun onDeleteCategory(categoryWithSubcategoryBudget: CategoryWithSubcategoryBudget)
        fun onDeleteSubcategory(subcategoryBudget: SubcategoryBudget)
    }

    override fun onClick(item: SubcategoryBudget) {
        listener.onDeleteSubcategory(item)
    }
}