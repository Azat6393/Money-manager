package com.woynex.parasayar.core.utils.custom_dialog

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.woynex.parasayar.R
import com.woynex.parasayar.core.utils.OnItemClickListener
import com.woynex.parasayar.core.utils.adapter.CategoryAdapter
import com.woynex.parasayar.core.utils.adapter.SubCategoryAdapter
import com.woynex.parasayar.databinding.DialogCategoriesBinding
import com.woynex.parasayar.feature_settings.domain.model.Category
import com.woynex.parasayar.feature_settings.domain.model.CategoryWithSubCategories
import com.woynex.parasayar.feature_settings.domain.model.SubCategory
import kotlinx.android.synthetic.main.fragment_accounts.view.*
import kotlinx.coroutines.flow.StateFlow


class CategoriesDialog(
    private val _binding: DialogCategoriesBinding,
    private val context: Context,
    private val categoryWithSubcategories: List<CategoryWithSubCategories>,
    private val onClose: () -> Unit,
    private val onEdit: () -> Unit,
    private val selectedCategory: (Category, SubCategory?) -> Unit
) : OnItemClickListener<SubCategory>,
    CategoryAdapter.CategoryOnItemClickListener {

    private val categoryAdapter: CategoryAdapter by lazy {
        CategoryAdapter(this)
    }
    private val subcategoryAdapter: SubCategoryAdapter by lazy {
        SubCategoryAdapter(this)
    }

    private var category: Category? = null

    init {
        initView()
    }

    fun initView() {

        _binding.categoryRecyclerView.apply {
            adapter = categoryAdapter
            layoutManager = GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false)
            setHasFixedSize(true)
        }
        _binding.subcategoryRecyclerView.apply {
            adapter = subcategoryAdapter
            layoutManager = GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false)
            setHasFixedSize(true)
        }
        _binding.closeBtn.setOnClickListener { invisible() }
        _binding.editBtn.setOnClickListener { onEdit() }
        _binding.addBtn.setOnClickListener { onEdit() }
        _binding.backCategoryBtn.setOnClickListener {
            if (_binding.subcategoryRecyclerView.isVisible){
                _binding.subcategoryRecyclerView.isVisible = false
                _binding.categoryRecyclerView.isVisible = true
                _binding.title.text = context.getString(R.string.category)
                subcategoryAdapter.submitList(null)
            }else {
                invisible()
            }
        }
        observe()
    }


    private fun observe() {
        categoryAdapter.submitList(categoryWithSubcategories)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onClickCategory(category: Category, subCategories: List<SubCategory>) {
        this.category = category
        subcategoryAdapter.submitList(emptyList())
        subcategoryAdapter.notifyDataSetChanged()
        subcategoryAdapter.submitList(subCategories)
        _binding.categoryRecyclerView.isVisible = false
        _binding.subcategoryRecyclerView.isVisible = true
        _binding.title.text = "${context.getString(R.string.category)} > ${category.name}"
    }

    override fun onSelect(category: Category) {
        selectedCategory(category, null)
        invisible()
    }

    fun visible() {
        _binding.root.isVisible = true
    }

    fun invisible() {
        _binding.subcategoryRecyclerView.isVisible = false
        _binding.categoryRecyclerView.isVisible = true
        _binding.title.text = context.getString(R.string.category)
        subcategoryAdapter.submitList(null)
        _binding.root.isVisible = false
        onClose()
    }

    override fun onClick(item: SubCategory) {
        selectedCategory(category!!, item)
        invisible()
    }
}