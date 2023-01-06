package com.woynex.parasayar.core.utils.custom_dialog

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.woynex.parasayar.R
import com.woynex.parasayar.core.utils.OnItemClickListener
import com.woynex.parasayar.core.utils.adapter.CategoryAdapter
import com.woynex.parasayar.core.utils.adapter.SubCategoryAdapter
import com.woynex.parasayar.databinding.DialogCategoriesBinding
import com.woynex.parasayar.feature_accounts.domain.model.Account
import com.woynex.parasayar.feature_settings.domain.model.CategoryWithSubCategories
import com.woynex.parasayar.feature_settings.domain.model.SubCategory
import kotlinx.coroutines.flow.StateFlow

class CategoriesDialog(
    private val categoryWithSubcategories: StateFlow<List<CategoryWithSubCategories>>,
    private val selectedCategory: (String, String?) -> Unit
) : DialogFragment(), OnItemClickListener<SubCategory>,
    CategoryAdapter.CategoryOnItemClickListener {

    private lateinit var _binding: DialogCategoriesBinding
    private val categoryAdapter: CategoryAdapter by lazy {
        CategoryAdapter(this)
    }
    private val subcategoryAdapter: SubCategoryAdapter by lazy {
        SubCategoryAdapter(this)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_categories, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = DialogCategoriesBinding.bind(view)

        _binding.categoryRecyclerView.apply {
            adapter = categoryAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
        _binding.subcategoryRecyclerView.apply {
            adapter = subcategoryAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
        observe()
    }

    private fun observe() {
        lifecycleScope.launchWhenStarted {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                categoryWithSubcategories.collect { result ->
                    categoryAdapter.submitList(result)
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
    }

    override fun onClick(item: SubCategory) {
        selectedCategory(item.category_name,item.name)
        this.dismiss()
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onClickCategory(subCategories: List<SubCategory>) {
        subcategoryAdapter.submitList(emptyList())
        subcategoryAdapter.notifyDataSetChanged()
        subcategoryAdapter.submitList(subCategories)
    }

    override fun onSelect(category: String) {
        selectedCategory(category, null)
        this.dismiss()
    }
}