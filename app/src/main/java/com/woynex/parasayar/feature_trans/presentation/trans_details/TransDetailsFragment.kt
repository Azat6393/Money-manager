package com.woynex.parasayar.feature_trans.presentation.trans_details

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.woynex.parasayar.R
import com.woynex.parasayar.core.utils.*
import com.woynex.parasayar.core.utils.adapter.CategoryAdapter
import com.woynex.parasayar.core.utils.bottom_sheet.AmountInputBottomSheet
import com.woynex.parasayar.core.utils.custom_dialog.AccountsDialog
import com.woynex.parasayar.core.utils.custom_dialog.CategoriesDialog
import com.woynex.parasayar.databinding.FragmentTransDetailsBinding
import com.woynex.parasayar.feature_accounts.domain.model.Account
import com.woynex.parasayar.feature_settings.domain.model.Category
import com.woynex.parasayar.feature_trans.domain.model.Trans
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_add_account.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneOffset
import java.util.*

@AndroidEntryPoint
class TransDetailsFragment : Fragment(R.layout.fragment_trans_details) {

    private lateinit var _binding: FragmentTransDetailsBinding
    private var transType: TransType = TransType.Expense
    private lateinit var dateAndTime: LocalDateTime
    private val viewModel: TransDetailsViewModel by viewModels()

    private var selectedAccount: Account? = null
    private var selectedToAccount: Account? = null
    private var selectedCategory = ""
    private var selectedSubCategory: String? = null
    private var selectedCurrency = ""
    private var selectedAmount = 0.00

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
                    CategoriesDialog(viewModel.categoryWithSubCategories) { category, subCategory ->
                        selectedCategory = category
                        selectedSubCategory = subCategory
                        _binding.category.setText(
                            if (subCategory != null) "$category/$subCategory" else category
                        )
                    }.show(childFragmentManager, "Category Dialog")
                }
            }
            amount.setOnClickListener {
                AmountInputBottomSheet { currency, amount ->
                    selectedCurrency = currency
                    selectedAmount = amount
                    _binding.amount.setText("$currency $amount")
                }.show(childFragmentManager, "Amount Input Bottom Sheet")
            }
            saveBtn.setOnClickListener {
                if (isValid()) {
                    when (transType) {
                        TransType.Expense -> doExpense()
                        TransType.Income -> doIncome()
                        TransType.Transfer -> doTransfer()
                    }
                }
            }
            backBtn.setOnClickListener {
                findNavController().popBackStack()
            }
        }
        onTypeChange()
        observe()
    }

    private fun observe() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.saveStatus.collect { result ->
                    when (result) {
                        is Resource.Empty -> Unit
                        is Resource.Error -> Unit
                        is Resource.Loading -> Unit
                        is Resource.Success -> {
                            findNavController().popBackStack()
                        }
                    }
                }
            }
        }
    }

    private fun doExpense() {
        val newTrans = createTrans()
        viewModel.insertTrans(trans = newTrans, selectedAccount!!)
    }

    private fun doIncome() {
        val newTrans = createTrans()
        viewModel.insertTrans(trans = newTrans, selectedAccount!!)
    }

    private fun doTransfer() {
        val newTrans = createTransferTrans()
        if (newTrans.fee_amount == null) {
            viewModel.insertTransferTrans(
                trans = newTrans,
                to = selectedToAccount!!,
                from = selectedAccount!!
            )
        } else {
            val feeTrans =
                createFeeTrans(newTrans.fee_amount, newTrans.category, newTrans.account_name)
            viewModel.insertTransferTrans(
                trans = newTrans,
                to = selectedToAccount!!,
                from = selectedAccount!!,
                feeTrans = feeTrans
            )
        }
    }

    private fun createFeeTrans(feeAmount: Double, to: String, from: String): Trans {
        return Trans(
            amount = feeAmount,
            account_name = from,
            category = "Others",
            note = "Fees",
            description = "",
            type = TransTypes.EXPENSE,
            photo = "",
            currency = selectedCurrency,
            date_in_millis = dateAndTime.toInstant(ZoneOffset.ofTotalSeconds(0)).toEpochMilli(),
            day = dateAndTime.dayOfMonth,
            month = dateAndTime.monthValue,
            year = dateAndTime.year,
            time = parseTimeDate(dateAndTime)
        )
    }

    private fun createTransferTrans(): Trans {
        val fee = _binding.fees.text.toString()
        return Trans(
            amount = selectedAmount,
            category = selectedToAccount!!.name,
            account_name = selectedAccount!!.name,
            subcategory = selectedSubCategory,
            note = _binding.note.text.toString(),
            description = _binding.description.text.toString(),
            photo = "",
            fee_amount = if (fee.isNotBlank() && fee.toDouble() != 0.00) fee.toDouble() else null,
            currency = selectedCurrency,
            type = getTransType(),
            date_in_millis = dateAndTime.toInstant(ZoneOffset.ofTotalSeconds(0)).toEpochMilli(),
            day = dateAndTime.dayOfMonth,
            month = dateAndTime.monthValue,
            year = dateAndTime.year,
            time = parseTimeDate(dateAndTime)
        )
    }

    private fun createTrans(): Trans {
        return Trans(
            amount = selectedAmount,
            category = selectedCategory,
            account_name = selectedAccount!!.name,
            subcategory = selectedSubCategory,
            note = _binding.note.text.toString(),
            description = _binding.description.text.toString(),
            photo = "",
            currency = selectedCurrency,
            type = getTransType(),
            date_in_millis = dateAndTime.toInstant(ZoneOffset.ofTotalSeconds(0)).toEpochMilli(),
            day = dateAndTime.dayOfMonth,
            month = dateAndTime.monthValue,
            year = dateAndTime.year,
            time = parseTimeDate(dateAndTime)
        )
    }

    private fun getTransType(): String {
        return when (transType) {
            TransType.Expense -> TransTypes.EXPENSE
            TransType.Income -> TransTypes.INCOME
            TransType.Transfer -> TransTypes.TRANSFER
        }
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
            selectedCurrency.isBlank() || selectedAmount == 0.00 -> {
                requireView().showSnackBar(getString(R.string.invalid_amount))
                false
            }
            else -> {
                true
            }
        }
    }

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
            val selectedDateAndTime = LocalDateTime.of(
                LocalDate.of(
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH) + 1,
                    calendar.get(Calendar.DAY_OF_MONTH)
                ), LocalTime.of(
                    timePicker.hour,
                    timePicker.minute
                )
            )
            dateAndTime = selectedDateAndTime
            _binding.date.setText(parseFullDate(selectedDateAndTime))
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
            selectedAccount = null
            selectedToAccount = null
            selectedCategory = ""
            selectedSubCategory = null
            selectedCurrency = ""
            selectedAmount = 0.00
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