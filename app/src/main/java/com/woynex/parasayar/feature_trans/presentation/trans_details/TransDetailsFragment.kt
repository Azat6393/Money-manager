package com.woynex.parasayar.feature_trans.presentation.trans_details

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
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
import com.woynex.parasayar.core.utils.bottom_sheet.AmountInputBottomSheet
import com.woynex.parasayar.core.utils.custom_dialog.AccountsDialog
import com.woynex.parasayar.core.utils.custom_dialog.CategoriesDialog
import com.woynex.parasayar.databinding.FragmentTransDetailsBinding
import com.woynex.parasayar.feature_accounts.domain.model.Account
import com.woynex.parasayar.feature_settings.domain.model.Category
import com.woynex.parasayar.feature_settings.domain.model.SubCategory
import com.woynex.parasayar.feature_trans.TransCoreViewModel
import com.woynex.parasayar.feature_trans.domain.model.Trans
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.util.*

@AndroidEntryPoint
class TransDetailsFragment : Fragment(R.layout.fragment_trans_details) {

    private lateinit var _binding: FragmentTransDetailsBinding
    private var transType: TransType = TransType.Expense
    private lateinit var dateAndTime: LocalDateTime
    private val viewModel: TransDetailsViewModel by viewModels()
    private val coreViewModel: TransCoreViewModel by viewModels()


    private var selectedAccount: Account? = null
    private var selectedToAccount: Account? = null
    private var selectedCategory: Category? = null
    private var selectedSubCategory: SubCategory? = null
    private var selectedCurrency = ""
    private var selectedAmount = 0.00
    private var feeAmount: Double? = null

    private var selectedBitmap: Bitmap? = null

    private val requestGetContentPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                if (PhotoPickerAvailabilityChecker.isPhotoPickerAvailable())
                    singlePhotoPickerLauncher
                else getImageBeforeAndroid11.launch("image/*")
            }
        }

    private val singlePhotoPickerLauncher =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { imageUri: Uri? ->
            selectedBitmap = imageUri?.uriToBitmap(requireContext())
            setImage()
        }

    private val getImageBeforeAndroid11: ActivityResultLauncher<String> =
        registerForActivityResult(ActivityResultContracts.GetContent()) { imageUri: Uri? ->
            selectedBitmap = imageUri?.uriToBitmap(requireContext())
            setImage()
        }

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
                            if (subCategory != null) "${category.name}/${subCategory.name}" else category.name
                        )
                    }.show(childFragmentManager, "Category Dialog")
                }
            }
            amount.setOnClickListener {
                AmountInputBottomSheet(
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
                ).show(childFragmentManager, "Amount Input Bottom Sheet")
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
            imageBtn.setOnClickListener {
                if (requireContext().checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED
                ) {
                    if (PhotoPickerAvailabilityChecker.isPhotoPickerAvailable())
                        singlePhotoPickerLauncher
                    else getImageBeforeAndroid11.launch("image/*")
                } else {
                    requestGetContentPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                }
            }
            closeBtn.setOnClickListener {
                removeImage()
            }
        }
        onTypeChange()
        observe()
    }

    private fun removeImage() {
        _binding.selectedImage.setImageBitmap(null)
        _binding.selectedImageContainer.isVisible = false
        _binding.closeBtn.isVisible = false
        selectedBitmap = null
    }

    private fun setImage() {
        _binding.selectedImage.setImageBitmap(selectedBitmap)
        _binding.selectedImageContainer.isVisible = true
        _binding.closeBtn.isVisible = true
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
        viewModel.insertTrans(trans = newTrans)
    }

    private fun doIncome() {
        val newTrans = createTrans()
        viewModel.insertTrans(trans = newTrans)
    }

    private fun doTransfer() {
        val newTrans = createTransferTrans()
        if (newTrans.fee_amount == null) {
            viewModel.insertTransferTrans(
                trans = newTrans
            )
        } else {
            val feeTrans =
                createFeeTrans(newTrans.fee_amount, newTrans.trans_id)
            viewModel.insertTransferTrans(
                trans = newTrans,
                feeTrans = feeTrans
            )
        }
    }

    private fun createFeeTrans(feeAmount: Double, fromUUID: String): Trans {
        val feeDate = dateAndTime.plusSeconds(10)
        return Trans(
            trans_id = generateUUID(),
            amount = feeAmount,
            currency = selectedCurrency,
            type = TransTypes.EXPENSE,
            account_name = selectedAccount!!.name,
            account_id = selectedAccount!!.id!!,
            category = getString(R.string.Others),
            fee_trans_id = fromUUID,
            note = getString(R.string.fees),
            description = "",
            photo = selectedBitmap,
            date_in_millis = feeDate.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli(),
            day = feeDate.dayOfMonth,
            month = feeDate.monthValue,
            year = feeDate.year,
            time = parseTimeDate(feeDate)
        )
    }

    private fun createTransferTrans(): Trans {
        return Trans(
            trans_id = generateUUID(),
            amount = selectedAmount,
            currency = selectedCurrency,
            type = getTransType(),
            account_name = selectedAccount!!.name,
            account_id = selectedAccount!!.id!!,
            to_account_name = selectedToAccount?.name,
            to_account_id = selectedToAccount?.id,
            fee_amount = if (feeAmount == null || feeAmount != 0.00) feeAmount else null,
            note = _binding.note.text.toString(),
            photo = selectedBitmap,
            description = _binding.description.text.toString(),
            date_in_millis = dateAndTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli(),
            day = dateAndTime.dayOfMonth,
            month = dateAndTime.monthValue,
            year = dateAndTime.year,
            time = parseTimeDate(dateAndTime)
        )
    }

    private fun createTrans(): Trans {
        return Trans(
            trans_id = generateUUID(),
            amount = selectedAmount,
            currency = selectedCurrency,
            type = getTransType(),
            account_name = selectedAccount!!.name,
            account_id = selectedAccount!!.id!!,
            category = selectedCategory?.name,
            category_id = selectedCategory?.id,
            subcategory = selectedSubCategory?.name,
            subcategory_id = selectedSubCategory?.id,
            note = _binding.note.text.toString(),
            photo = selectedBitmap,
            description = _binding.description.text.toString(),
            date_in_millis = dateAndTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli(),
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
            transType == TransType.Transfer && selectedAccount?.id == selectedToAccount?.id -> {
                requireView().showSnackBar(getString(R.string.same_account_error))
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
            title.text = getString(R.string.income)
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
            title.text = getString(R.string.expense)
        }
    }

    private fun onTransfer() {
        transType = TransType.Transfer
        refreshEditTexts()
        _binding.apply {
            categoryTitle.text = getString(R.string.to)
            accountTitle.text = getString(R.string.from)
            feesBtn.setOnClickListener {
                feesBtn.isVisible = false
                feesContainer.isVisible = true
                closeFeesBtn.isVisible = true
            }
            closeFeesBtn.setOnClickListener {
                feesContainer.isVisible = false
                feesBtn.isVisible = true
                fees.setText("")
                feeAmount = null
            }
            feesContainer.isVisible = feeAmount != null
            feesBtn.isVisible = feeAmount == null
            closeFeesBtn.isVisible = feeAmount != null
            title.text = getString(R.string.transfer)
            fees.doAfterTextChanged { text ->
                feeAmount = if (text.toString().isBlank()) {
                    null
                } else {
                    text.toString().toDouble()
                }
            }
        }
    }

    private fun refreshEditTexts() {
        _binding.apply {
            category.setText("")
            selectedCategory = null
            selectedSubCategory = null
            selectedToAccount = null
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