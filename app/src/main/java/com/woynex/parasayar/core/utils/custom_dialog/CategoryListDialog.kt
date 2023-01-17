package com.woynex.parasayar.core.utils.custom_dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.woynex.parasayar.R
import com.woynex.parasayar.core.utils.adapter.AllCategoryAdapter
import com.woynex.parasayar.core.utils.adapter.CategoryAdapter
import com.woynex.parasayar.databinding.DialogCategoriesBinding
import com.woynex.parasayar.databinding.DialogCategoryListBinding
import com.woynex.parasayar.feature_settings.domain.model.Category
import com.woynex.parasayar.feature_settings.domain.model.CategoryWithSubCategories
import com.woynex.parasayar.feature_settings.domain.model.SubCategory

class CategoryListDialog(
    private val categories: List<Category>,
    private val selectedCategory: (Category) -> Unit
) : DialogFragment(), AllCategoryAdapter.AllCategoryOnItemClickListener {

    private lateinit var _binding: DialogCategoryListBinding
    private val categoryAdapter: AllCategoryAdapter by lazy {
        AllCategoryAdapter(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_category_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = DialogCategoryListBinding.bind(view)

        _binding.categoryRecyclerView.apply {
            adapter = categoryAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
        categoryAdapter.submitList(categories)
    }


    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
    }

    override fun onSelect(category: Category) {
        selectedCategory(category)
        this.dismiss()
    }
}