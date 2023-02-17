package com.woynex.parasayar.feature_trans.presentation.total

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.woynex.parasayar.R
import com.woynex.parasayar.core.utils.SharedPreferencesHelper
import com.woynex.parasayar.core.utils.TransTypes
import com.woynex.parasayar.core.utils.bottom_sheet.AmountInputBottomSheet
import com.woynex.parasayar.core.utils.custom_dialog.CategoriesDialog
import com.woynex.parasayar.databinding.FragmentAddBudgetBinding
import com.woynex.parasayar.feature_settings.domain.model.Category
import com.woynex.parasayar.feature_settings.domain.model.SubCategory
import com.woynex.parasayar.feature_trans.TransCoreViewModel
import com.woynex.parasayar.feature_trans.domain.model.CategoryBudget
import com.woynex.parasayar.feature_trans.domain.model.SubcategoryBudget
import com.woynex.parasayar.feature_trans.presentation.trans_details.TransDetailsFragmentDirections
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AddBudgetFragment : Fragment(R.layout.fragment_add_budget) {

    private lateinit var _binding: FragmentAddBudgetBinding
    private val viewModel: BudgetViewModel by viewModels()
    private val coreViewModel: TransCoreViewModel by viewModels()

    private var categoriesDialog: CategoriesDialog? = null

    private var selectedCategory: Category? = null
    private var selectedSubCategory: SubCategory? = null
    private var selectedCurrency = ""
    private var selectedAmount = 0.00

    @Inject
    lateinit var sharedPreferencesHelper: SharedPreferencesHelper

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAddBudgetBinding.bind(view)

        selectedCurrency = sharedPreferencesHelper.getDefaultCurrency().symbol
        _binding.amount.setText("$selectedCurrency 0.00")

        _binding.category.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                if (categoriesDialog != null)
                    categoriesDialog!!.visible()
            }
        }

        _binding.amount.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                categoriesDialog?.invisible()
                val dialog = AmountInputBottomSheet(
                    editText = _binding.amount,
                    defaultCurrency = selectedCurrency,
                    coreViewModel = coreViewModel,
                    input = { currency, amount ->
                        selectedCurrency = currency
                        selectedAmount = amount
                        _binding.amount.setText("$currency $amount")
                    },
                    navigateToListCurrency = {
                        val action =
                            TransDetailsFragmentDirections.actionTransDetailsFragmentToCurrenciesFragment()
                        findNavController().navigate(action)
                    }
                )
                dialog.show(childFragmentManager, "AmountInputBottomSheet")
            }
        }
        _binding.saveBtn.setOnClickListener {
            if (selectedCategory != null && selectedAmount > 0.00) {
                if (selectedSubCategory != null) {
                    viewModel.insertSubcategoryBudget(
                        SubcategoryBudget(
                            category_id = selectedCategory!!.id!!,
                            category_name = selectedCategory!!.name,
                            amount = selectedAmount,
                            currency = selectedCurrency,
                            subcategory_id = selectedSubCategory!!.id!!,
                            subcategory_name = selectedSubCategory!!.name
                        )
                    )
                } else {
                    viewModel.insertCategoryBudget(
                        CategoryBudget(
                            category_id = selectedCategory!!.id!!,
                            category_name = selectedCategory!!.name,
                            amount = selectedAmount,
                            currency = selectedCurrency
                        )
                    )
                }
                lifecycleScope.launch {
                    delay(2000L)
                    findNavController().popBackStack()
                }
            }
        }
        _binding.backBtn.setOnClickListener { findNavController().popBackStack() }
        observe()
        viewModel.getExpenseCategories()
    }

    private fun observe() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.categoryWithSubCategories.collect { result ->
                    categoriesDialog = CategoriesDialog(
                        _binding.categoryContainer,
                        requireContext(),
                        result,
                        onClose = {
                            _binding.category.clearFocus()
                        },
                        onEdit = {
                            val action =
                                AddBudgetFragmentDirections.actionAddBudgetFragmentToCategorySettingFragment(
                                    TransTypes.EXPENSE
                                )
                            findNavController().navigate(action)
                        }
                    ) { category, subCategory ->
                        selectedCategory = category
                        selectedSubCategory = subCategory
                        _binding.category.setText(
                            if (subCategory != null) "${category.name}/${subCategory.name}" else category.name
                        )
                    }
                }
            }
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