package com.woynex.parasayar.feature_settings.presentation.category

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
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.woynex.parasayar.R
import com.woynex.parasayar.core.utils.CategoryTypes
import com.woynex.parasayar.core.utils.custom_dialog.CustomEditDialog
import com.woynex.parasayar.core.utils.showAlertDialog
import com.woynex.parasayar.databinding.FragmentCategorySettingBinding
import com.woynex.parasayar.feature_settings.domain.model.Category
import com.woynex.parasayar.feature_settings.domain.model.CategoryWithSubCategories
import com.woynex.parasayar.feature_settings.presentation.adapter.CategoryListAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CategorySettingFragment : Fragment(R.layout.fragment_category_setting),
    CategoryListAdapter.CategoryListItemListener {

    private lateinit var _binding: FragmentCategorySettingBinding
    private val mAdapter: CategoryListAdapter by lazy { CategoryListAdapter(this) }
    private val args: CategorySettingFragmentArgs by navArgs()
    private val viewModel: CategoryViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentCategorySettingBinding.bind(view)

        initView()
        observe()
        viewModel.getCategories(args.type)
    }

    private fun observe() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.categories.collect { result ->
                    mAdapter.submitList(result)
                }
            }
        }
    }

    private fun initView() {
        _binding.apply {
            if (args.type == CategoryTypes.EXPENSE)
                titleTv.text = getString(R.string.expenses_category)
            if (args.type == CategoryTypes.INCOME)
                titleTv.text = getString(R.string.income_category)
            recyclerView.apply {
                adapter = mAdapter
                layoutManager = LinearLayoutManager(requireContext())
                setHasFixedSize(true)
            }
            backBtn.setOnClickListener { findNavController().popBackStack() }
            addBtn.setOnClickListener {
                CustomEditDialog(
                    titleText = getString(R.string.expenses_category),
                    isEditMode = false,
                    save = {
                        viewModel.insertCategory(
                            Category(
                                name = it,
                                type = args.type
                            )
                        )
                    },
                    update = {}
                ).show(childFragmentManager, "CustomEditDialog")
            }
        }
    }

    override fun onClick(categoryWithSubCategories: CategoryWithSubCategories) {
        val action =
            CategorySettingFragmentDirections.actionCategorySettingFragmentToSubcategorySettingFragment(
                categoryWithSubCategories.category.id!!
            )
        findNavController().navigate(action)
    }

    override fun onDelete(categoryWithSubCategories: CategoryWithSubCategories) {
        requireContext().showAlertDialog(
            getString(R.string.delete_category_title),
            getString(R.string.delete_category_message)
        ) {
            viewModel.deleteCategory(categoryWithSubCategories)
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