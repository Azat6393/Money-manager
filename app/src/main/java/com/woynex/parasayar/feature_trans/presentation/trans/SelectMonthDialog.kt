package com.woynex.parasayar.feature_trans.presentation.trans

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.annotation.RequiresApi
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

    @RequiresApi(Build.VERSION_CODES.O)
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
            }
            dateRightBtn.setOnClickListener {
                date = date.plusYears(1)
                dateTv.text = date.year.toString()
            }
            initSelectedMonth()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initSelectedMonth() {
        _binding.apply {
            jan.setOnClickListener {
                date = date.withMonth(Month.JANUARY.value)
                onSelect(date)
                this@SelectMonthDialog.dismiss()
            }
            feb.setOnClickListener {
                date = date.withMonth(Month.FEBRUARY.value)
                onSelect(date)
                this@SelectMonthDialog.dismiss()
            }
            mar.setOnClickListener {
                date = date.withMonth(Month.MARCH.value)
                onSelect(date)
                this@SelectMonthDialog.dismiss()
            }
            apr.setOnClickListener {
                date = date.withMonth(Month.APRIL.value)
                onSelect(date)
                this@SelectMonthDialog.dismiss()
            }
            may.setOnClickListener {
                date = date.withMonth(Month.MAY.value)
                onSelect(date)
                this@SelectMonthDialog.dismiss()
            }
            jun.setOnClickListener {
                date = date.withMonth(Month.JUNE.value)
                onSelect(date)
                this@SelectMonthDialog.dismiss()
            }
            jul.setOnClickListener {
                date = date.withMonth(Month.JULY.value)
                onSelect(date)
                this@SelectMonthDialog.dismiss()
            }
            aug.setOnClickListener {
                date = date.withMonth(Month.AUGUST.value)
                onSelect(date)
                this@SelectMonthDialog.dismiss()
            }
            SEP.setOnClickListener {
                date = date.withMonth(Month.SEPTEMBER.value)
                onSelect(date)
                this@SelectMonthDialog.dismiss()
            }
            oct.setOnClickListener {
                date = date.withMonth(Month.OCTOBER.value)
                onSelect(date)
                this@SelectMonthDialog.dismiss()
            }
            nov.setOnClickListener {
                date = date.withMonth(Month.NOVEMBER.value)
                onSelect(date)
                this@SelectMonthDialog.dismiss()
            }
            dec.setOnClickListener {
                date = date.withMonth(Month.DECEMBER.value)
                onSelect(date)
                this@SelectMonthDialog.dismiss()
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