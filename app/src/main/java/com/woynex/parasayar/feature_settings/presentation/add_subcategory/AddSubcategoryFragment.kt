package com.woynex.parasayar.feature_settings.presentation.add_subcategory

import android.os.Bundle
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.woynex.parasayar.R
import com.woynex.parasayar.core.utils.custom_dialog.CategoriesDialog
import com.woynex.parasayar.core.utils.custom_dialog.CategoryListDialog
import com.woynex.parasayar.databinding.FragmentAddSubcategoryBinding
import com.woynex.parasayar.feature_settings.domain.model.Category
import com.woynex.parasayar.feature_settings.domain.model.SubCategory
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddSubcategoryFragment : Fragment(R.layout.fragment_add_subcategory) {

    private lateinit var _binding: FragmentAddSubcategoryBinding
    private val args: AddSubcategoryFragmentArgs by navArgs()
    private val viewModel: AddSubcategoryViewModel by viewModels()

    private var selectedCategory: Category? = null
    private var isEditMode = false
    private var categoryList: List<Category> = emptyList()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAddSubcategoryBinding.bind(view)


        selectedCategory = args.category
        if (args.subcategory != null) isEditMode = true

        initView()
        observe()
        viewModel.getCategories(args.type)
    }

    private fun observe() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.categoryList.collect { result ->
                    categoryList = result
                }
            }
        }
    }

    private fun initView() {
        _binding.apply {
            if (args.subcategory == null)
                titleTv.text = getString(R.string.category)
            else titleTv.text = getString(R.string.modify_subcategory)
            category.setText(args.category.name)
            args.subcategory?.let {
                subcategory.setText(it.name)
            }
            category.setOnClickListener {
                CategoryListDialog(categoryList) {
                    category.setText(it.name)
                    selectedCategory = it
                }.show(childFragmentManager, "CategoryListDialog")
            }
            saveBtn.setOnClickListener {
                val name = _binding.subcategory.text.toString()
                if (name.isNotBlank() && selectedCategory != null) {
                    if (isEditMode) {
                        args.subcategory?.let { subCategory ->
                            viewModel.updateSubcategory(
                                subCategory.copy(
                                    name = name,
                                    category_name = selectedCategory?.name!!,
                                    category_id = selectedCategory?.id!!
                                )
                            )
                        }
                    } else {
                        viewModel.addSubcategory(
                            SubCategory(
                                name = name,
                                category_name = selectedCategory?.name!!,
                                category_id = selectedCategory?.id!!
                            )
                        )
                    }
                    findNavController().popBackStack()
                }
            }
            backBtn.setOnClickListener { findNavController().popBackStack() }
        }
    }


    override fun onStart() {
        super.onStart()
        requireActivity()
            .findViewById<CoordinatorLayout>(R.id.coordinatorLayout)
            .visibility = View.GONE
    }

    override fun onStop() {
        super.onStop()
        requireActivity()
            .findViewById<CoordinatorLayout>(R.id.coordinatorLayout)
            .visibility = View.VISIBLE
    }
}