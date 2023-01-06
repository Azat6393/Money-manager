package com.woynex.parasayar.core.utils.bottom_sheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.woynex.parasayar.R
import com.woynex.parasayar.databinding.BottomAmountInputBinding

class AmountInputBottomSheet(private val input: (String, Double) -> Unit) : BottomSheetDialogFragment() {

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
            updateAmount()
        }
        initButtons()
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
        when (_binding.radioGroup.checkedRadioButtonId) {
            R.id.dollar -> {
                input("\$", if (inputNumber.isBlank()) 0.00 else inputNumber.toDouble())
            }
            R.id.euro -> {
                input("€", if (inputNumber.isBlank()) 0.00 else inputNumber.toDouble())
            }
            R.id.pound -> {
                input("£", if (inputNumber.isBlank()) 0.00 else inputNumber.toDouble())
            }
            R.id.lira -> {
                input("₺", if (inputNumber.isBlank()) 0.00 else inputNumber.toDouble())
            }
        }
    }
}