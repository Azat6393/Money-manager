package com.woynex.parasayar.feature_settings.presentation.subcategory

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.woynex.parasayar.R
import com.woynex.parasayar.core.utils.custom_dialog.CustomEditDialog
import com.woynex.parasayar.core.utils.showAlertDialog
import com.woynex.parasayar.databinding.FragmentSubcategorySettingBinding
import com.woynex.parasayar.feature_settings.domain.model.CategoryWithSubCategories
import com.woynex.parasayar.feature_settings.domain.model.SubCategory
import com.woynex.parasayar.feature_settings.presentation.adapter.SubcategoryListAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SubcategorySettingFragment : Fragment(R.layout.fragment_subcategory_setting),
    SubcategoryListAdapter.SubcategoryListItemListener {

    private lateinit var _binding: FragmentSubcategorySettingBinding
    private val args: SubcategorySettingFragmentArgs by navArgs()
    private val viewModel: SubcategoryViewModel by viewModels()
    private val mAdapter: SubcategoryListAdapter by lazy { SubcategoryListAdapter(this) }

    private lateinit var categoryWithSubCategories: CategoryWithSubCategories

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSubcategorySettingBinding.bind(view)

        initView()
        observe()
        viewModel.getCategories(args.categoryId)
    }

    private fun observe() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.subcategories.collect { result ->
                    if (result != null){
                        categoryWithSubCategories = result
                        _binding.titleTv.text = categoryWithSubCategories.category.name
                    }
                    mAdapter.submitList(result?.subCategoryList)
                }
            }
        }
    }

    private fun initView() {
        _binding.apply {
            recyclerView.apply {
                adapter = mAdapter
                layoutManager = LinearLayoutManager(requireContext())
                setHasFixedSize(true)
            }
            addBtn.setOnClickListener {
                val action =
                    SubcategorySettingFragmentDirections.actionSubcategorySettingFragmentToAddSubcategoryFragment(
                        categoryWithSubCategories.category, null, categoryWithSubCategories.category.type
                    )
                findNavController().navigate(action)
            }
            editBtn.setOnClickListener {
                CustomEditDialog(
                    titleText = getString(R.string.change_category),
                    isEditMode = true,
                    editText = categoryWithSubCategories.category.name,
                    save = {},
                    update = { name ->
                        titleTv.text = name
                        viewModel.updateCategory(
                            categoryWithSubCategories.category.copy(
                                name = name
                            )
                        )
                    }
                ).show(childFragmentManager, "CustomEditDialog")
            }
            backBtn.setOnClickListener { findNavController().popBackStack() }
        }
    }

    override fun onClick(item: SubCategory) {
        val action =
            SubcategorySettingFragmentDirections.actionSubcategorySettingFragmentToAddSubcategoryFragment(
                categoryWithSubCategories.category, item, categoryWithSubCategories.category.type
            )
        findNavController().navigate(action)
    }

    override fun onDelete(item: SubCategory) {
        requireContext().showAlertDialog(
            title = getString(R.string.delete_subcategory_title),
            message = getString(R.string.delete_subcategory_message)
        ) {
            viewModel.deleteSubcategory(item)
        }
    }

    override fun onStart() {
        super.onStart()
        requireActivity()
            .findViewById<BottomNavigationView>(R.id.bottom_navigation)
            .visibility = View.GONE
    }

    override fun onStop() {
        super.onStop()
        requireActivity()
            .findViewById<BottomNavigationView>(R.id.bottom_navigation)
            .visibility = View.VISIBLE
    }
}