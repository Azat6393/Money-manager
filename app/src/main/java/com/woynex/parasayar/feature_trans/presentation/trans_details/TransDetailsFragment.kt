package com.woynex.parasayar.feature_trans.presentation.trans_details

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.woynex.parasayar.R
import com.woynex.parasayar.core.utils.TransType
import com.woynex.parasayar.core.utils.adapter.CategoryAdapter
import com.woynex.parasayar.core.utils.bottom_sheet.AmountInputBottomSheet
import com.woynex.parasayar.core.utils.custom_dialog.AccountsDialog
import com.woynex.parasayar.core.utils.custom_dialog.CategoriesDialog
import com.woynex.parasayar.core.utils.parseFullDate
import com.woynex.parasayar.core.utils.showSnackBar
import com.woynex.parasayar.databinding.FragmentTransDetailsBinding
import com.woynex.parasayar.feature_accounts.domain.model.Account
import com.woynex.parasayar.feature_settings.domain.model.Category
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.*

@AndroidEntryPoint
class TransDetailsFragment : Fragment(R.layout.fragment_trans_details) {

    private lateinit var _binding: FragmentTransDetailsBinding
    private var transType: TransType = TransType.Expense
    private lateinit var dateAndTime: LocalDateTime
    private val viewModel: TransDetailsViewModel by viewModels()

    private lateinit var selectedAccount: Account
    private lateinit var selectedToAccount: Account
    private var selectedCategory = ""

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentTransDetailsBinding.bind(view)

        dateAndTime = LocalDateTime.now()
        _binding.apply {
            date.setText(parseFullDate(dateAndTime))
            date.setOnClickListener {
                showDatePicker()
            }
            account.setOnClickListener {
                AccountsDialog(viewModel.accounts) {
                    selectedAccount = it
                    _binding.account.setText(it.name)
                }.show(childFragmentManager, "Accounts Dialog")
            }
            category.setOnClickListener {
                if (transType == TransType.Transfer) {
                    AccountsDialog(viewModel.accounts) {
                        selectedToAccount = it
                        _binding.category.setText(it.name)
                    }.show(childFragmentManager, "Accounts Dialog")
                } else {
                    if (transType == TransType.Income) {
                        viewModel.getIncomeCategories()
                    } else {
                        viewModel.getExpenseCategories()
                    }
                    CategoriesDialog(viewModel.categoryWithSubCategories) {
                        selectedCategory = it
                        _binding.category.setText(it)
                    }.show(childFragmentManager, "Category Dialog")
                }
            }
            amount.setOnClickListener {
                AmountInputBottomSheet {
                    _binding.amount.setText(it)
                }.show(childFragmentManager, "Amount Input Bottom Sheet")
            }
            saveBtn.setOnClickListener {
                if (isValid()) {

                }
            }
            backBtn.setOnClickListener {
                findNavController().popBackStack()
            }
        }
        onTypeChange()
    }

    private fun isValid(): Boolean {
        return when {
            _binding.account.text.toString().isBlank() -> {
                requireView().showSnackBar(getString(R.string.invalid_account_name))
                false
            }
            _binding.category.text.toString().isBlank() -> {
                if (transType == TransType.Transfer) {
                    requireView().showSnackBar(getString(R.string.invalid_to_account_name))
                } else {
                    requireView().showSnackBar(getString(R.string.invalid_category_name))
                }
                false
            }
            _binding.amount.text.toString().isBlank() -> {
                requireView().showSnackBar(getString(R.string.invalid_amount))
                false
            }
            else -> {
                true
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun showDatePicker() {
        val datePicker =
            MaterialDatePicker.Builder.datePicker()
                .setTitleText(getString(R.string.select_date))
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build()
        datePicker.show(childFragmentManager, "Date Picker")

        datePicker.addOnPositiveButtonClickListener {
            showTimePicker(it)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun showTimePicker(date: Long) {
        val timePicker =
            MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setHour(12)
                .setMinute(15)
                .setTitleText(getString(R.string.select_time))
                .build()

        timePicker.show(childFragmentManager, "Time Picker")
        timePicker.addOnPositiveButtonClickListener {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = date
            val dateAndTime = LocalDateTime.of(
                LocalDate.of(
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH) + 1,
                    calendar.get(Calendar.DAY_OF_MONTH)
                ), LocalTime.of(
                    timePicker.hour,
                    timePicker.minute
                )
            )
            _binding.date.setText(parseFullDate(dateAndTime))
        }
    }

    private fun onTypeChange() {
        _binding.radioGroup.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.income -> onIncome()
                R.id.expense -> onExpense()
                R.id.transfer -> onTransfer()
            }
        }
    }

    private fun onIncome() {
        transType = TransType.Income
        refreshEditTexts()
        _binding.apply {
            categoryTitle.text = getString(R.string.category)
            accountTitle.text = getString(R.string.account)
            feesBtn.isVisible = false
            feesContainer.isVisible = false
        }
    }

    private fun onExpense() {
        transType = TransType.Expense
        refreshEditTexts()
        _binding.apply {
            categoryTitle.text = getString(R.string.category)
            accountTitle.text = getString(R.string.account)
            feesBtn.isVisible = false
            feesContainer.isVisible = false
        }
    }

    private fun onTransfer() {
        transType = TransType.Transfer
        refreshEditTexts()
        _binding.apply {
            categoryTitle.text = getString(R.string.to)
            accountTitle.text = getString(R.string.from)
            feesBtn.isVisible = true
            feesBtn.setOnClickListener {
                feesContainer.isVisible = true
                feesBtn.isVisible = false
            }
            closeFeesBtn.setOnClickListener {
                feesContainer.isVisible = false
                feesBtn.isVisible = true
            }
        }
    }

    private fun refreshEditTexts() {
        _binding.apply {
            account.setText("")
            category.setText("")
            amount.setText("")
            note.setText("")
            description.setText("")
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