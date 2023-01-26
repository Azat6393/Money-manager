package com.woynex.parasayar.core.utils.bottom_sheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.woynex.parasayar.R
import com.woynex.parasayar.core.domain.model.Currency
import com.woynex.parasayar.databinding.BottomAmountInputBinding
import com.woynex.parasayar.feature_trans.TransCoreViewModel
import kotlinx.coroutines.launch

class AmountInputBottomSheet(
    private val defaultCurrency: String,
    private val coreViewModel: TransCoreViewModel,
    private val input: (String, Double) -> Unit,
    private val navigateToListCurrency: () -> Unit
) : BottomSheetDialogFragment() {

    private lateinit var _binding: BottomAmountInputBinding
    private var inputNumber = ""
    private var firstTime = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_amount_input, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = BottomAmountInputBinding.bind(view)

        _binding.currencyListBtn.setOnClickListener { navigateToListCurrency() }
        initButtons()
        observe()
        _binding.currencyFilter.editText?.doAfterTextChanged { text ->
            if (firstTime) firstTime = false else updateAmount()
        }
        _binding.currencyFilter.editText?.setText(defaultCurrency)
        coreViewModel.getCurrencies()
    }

    private fun observe() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                coreViewModel.currencies.collect { result ->
                    initCurrencySelector(result)
                }
            }
        }
    }

    private fun initCurrencySelector(currencyList: List<Currency>) {
        val adapter = ArrayAdapter(requireContext(), R.layout.item_select_currency,
            currencyList.map { it.symbol }
        )
        (_binding.currencyFilter.editText as? AutoCompleteTextView)?.setAdapter(adapter)
    }

    private fun initButtons() {
        _binding.apply {
            deleteBtn.setOnClickListener {
                inputNumber = inputNumber.dropLast(1)
                updateAmount()
            }
            oneBtn.setOnClickListener {
                inputNumber += "1"
                updateAmount()
            }
            twoBtn.setOnClickListener {
                inputNumber += "2"
                updateAmount()
            }
            threeBtn.setOnClickListener {
                inputNumber += "3"
                updateAmount()
            }
            fourBtn.setOnClickListener {
                inputNumber += "4"
                updateAmount()
            }
            fiveBtn.setOnClickListener {
                inputNumber += "5"
                updateAmount()
            }
            sixBtn.setOnClickListener {
                inputNumber += "6"
                updateAmount()
            }
            sevenBtn.setOnClickListener {
                inputNumber += "7"
                updateAmount()
            }
            eightBtn.setOnClickListener {
                inputNumber += "8"
                updateAmount()
            }
            nineBtn.setOnClickListener {
                inputNumber += "9"
                updateAmount()
            }
            zeroBtn.setOnClickListener {
                inputNumber += "0"
                updateAmount()
            }
            zeroBtn.setOnLongClickListener {
                inputNumber += "+"
                updateAmount()
                true
            }
            starBtn.setOnClickListener {
                inputNumber += "*"
                updateAmount()
            }
            dotBtn.setOnClickListener {
                if (!inputNumber.contains(".")) {
                    inputNumber += "."
                    updateAmount()
                }
            }
            minusBtn.setOnClickListener {
                if (inputNumber.isBlank()) {
                    inputNumber = (inputNumber.toFloat() * (-1)).toString()
                    updateAmount()
                }
            }
            doneBtn.setOnClickListener {
                this@AmountInputBottomSheet.dismiss()
            }
        }
    }

    private fun updateAmount() {
        val currency = _binding.currencyFilter.editText?.text.toString()
        input(currency, if (inputNumber.isBlank()) 0.00 else inputNumber.toDouble())
    }
}