package com.woynex.parasayar.core.utils.bottom_sheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.woynex.parasayar.R
import com.woynex.parasayar.databinding.BottomAmountInputBinding

class AmountInputBottomSheet(private val input: (String) -> Unit) : BottomSheetDialogFragment() {

    private lateinit var _binding: BottomAmountInputBinding
    private var inputNumber = ""

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

        _binding.radioGroup.setOnCheckedChangeListener { group, checkedId ->
            updateCallNumber()
        }
        initButtons()
    }

    private fun initButtons() {
        _binding.apply {
            deleteBtn.setOnClickListener {
                inputNumber = inputNumber.dropLast(1)
                updateCallNumber()
            }
            oneBtn.setOnClickListener {
                inputNumber += "1"
                updateCallNumber()
            }
            twoBtn.setOnClickListener {
                inputNumber += "2"
                updateCallNumber()
            }
            threeBtn.setOnClickListener {
                inputNumber += "3"
                updateCallNumber()
            }
            fourBtn.setOnClickListener {
                inputNumber += "4"
                updateCallNumber()
            }
            fiveBtn.setOnClickListener {
                inputNumber += "5"
                updateCallNumber()
            }
            sixBtn.setOnClickListener {
                inputNumber += "6"
                updateCallNumber()
            }
            sevenBtn.setOnClickListener {
                inputNumber += "7"
                updateCallNumber()
            }
            eightBtn.setOnClickListener {
                inputNumber += "8"
                updateCallNumber()
            }
            nineBtn.setOnClickListener {
                inputNumber += "9"
                updateCallNumber()
            }
            zeroBtn.setOnClickListener {
                inputNumber += "0"
                updateCallNumber()
            }
            zeroBtn.setOnLongClickListener {
                inputNumber += "+"
                updateCallNumber()
                true
            }
            starBtn.setOnClickListener {
                inputNumber += "*"
                updateCallNumber()
            }
            dotBtn.setOnClickListener {
                if (!inputNumber.contains(".")) {
                    inputNumber += "."
                    updateCallNumber()
                }
            }
            minusBtn.setOnClickListener {
                if (inputNumber.isBlank()) {
                    inputNumber += "-"
                    updateCallNumber()
                }
            }
        }
    }

    private fun updateCallNumber() {
        when (_binding.radioGroup.checkedRadioButtonId) {
            R.id.dollar -> {
                val fullInput = "\$ $inputNumber"
                input(fullInput)
            }
            R.id.euro -> {
                val fullInput = "€ $inputNumber"
                input(fullInput)
            }
            R.id.pound -> {
                val fullInput = "£ $inputNumber"
                input(fullInput)
            }
            R.id.lira -> {
                val fullInput = "₺ $inputNumber"
                input(fullInput)
            }
        }
    }
}