package com.woynex.parasayar.core.utils.custom_dialog

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import com.woynex.parasayar.R
import com.woynex.parasayar.databinding.DialogSelectMonthBinding
import java.time.LocalDate
import java.time.Month


class SelectMonthDialog(private var date: LocalDate, private val onSelect: (LocalDate) -> Unit) :
    DialogFragment(R.layout.dialog_select_month) {

    private lateinit var _binding: DialogSelectMonthBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window!!.requestFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = DialogSelectMonthBinding.bind(view)

        _binding.apply {
            close.setOnClickListener {
                this@SelectMonthDialog.dismiss()
            }
            dateTv.text = date.year.toString()
            dateLeftBtn.setOnClickListener {
                date = date.minusYears(1)
                dateTv.text = date.year.toString()
                onSelect(date)
            }
            dateRightBtn.setOnClickListener {
                date = date.plusYears(1)
                dateTv.text = date.year.toString()
                onSelect(date)
            }
            initSelectedMonth()
        }
    }

    private fun initSelectedMonth() {
        _binding.apply {
            jan.setOnClickListener {
                date = date.withMonth(Month.JANUARY.value)
                onSelect(date)
            }
            feb.setOnClickListener {
                date = date.withMonth(Month.FEBRUARY.value)
                onSelect(date)
            }
            mar.setOnClickListener {
                date = date.withMonth(Month.MARCH.value)
                onSelect(date)
            }
            apr.setOnClickListener {
                date = date.withMonth(Month.APRIL.value)
                onSelect(date)
            }
            may.setOnClickListener {
                date = date.withMonth(Month.MAY.value)
                onSelect(date)
            }
            jun.setOnClickListener {
                date = date.withMonth(Month.JUNE.value)
                onSelect(date)
            }
            jul.setOnClickListener {
                date = date.withMonth(Month.JULY.value)
                onSelect(date)
            }
            aug.setOnClickListener {
                date = date.withMonth(Month.AUGUST.value)
                onSelect(date)
            }
            sep.setOnClickListener {
                date = date.withMonth(Month.SEPTEMBER.value)
                onSelect(date)
            }
            oct.setOnClickListener {
                date = date.withMonth(Month.OCTOBER.value)
                onSelect(date)
            }
            nov.setOnClickListener {
                date = date.withMonth(Month.NOVEMBER.value)
                onSelect(date)
            }
            dec.setOnClickListener {
                date = date.withMonth(Month.DECEMBER.value)
                onSelect(date)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
            dialog.window!!.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }
}