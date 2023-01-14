package com.woynex.parasayar.feature_trans.presentation.calendar

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.woynex.parasayar.R
import com.woynex.parasayar.core.domain.model.Currency
import com.woynex.parasayar.core.utils.OnItemClickListener
import com.woynex.parasayar.databinding.BottomSheetCalendarDetailsBinding
import com.woynex.parasayar.feature_trans.domain.model.Trans
import com.woynex.parasayar.feature_trans.presentation.adapter.DailyTransAdapterParent
import kotlinx.coroutines.launch
import java.time.LocalDate

class DailyTransBottomSheet(
    private val date: LocalDate,
    private val calendarViewModel: CalendarViewModel,
    private val currency: Currency,
    private val onClickTrans: (Trans) -> Unit
) : BottomSheetDialogFragment(), OnItemClickListener<Trans> {

    private lateinit var _binding: BottomSheetCalendarDetailsBinding
    private val mAdapter: DailyTransAdapterParent by lazy { DailyTransAdapterParent(this) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_sheet_calendar_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = BottomSheetCalendarDetailsBinding.bind(view)

        initRecyclerView()
        observe()
        calendarViewModel.getDailyTrans(
            day = date.dayOfMonth,
            month = date.monthValue,
            year = date.year,
            currency = currency.symbol
        )
    }

    private fun initRecyclerView() {
        _binding.dailyTransRv.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun observe() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                calendarViewModel.dailyTrans.collect { result ->
                    mAdapter.submitList(result)
                }
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        calendarViewModel.clearDailyTrans()
    }

    override fun onClick(item: Trans) {
        onClickTrans(item)
        this.dismiss()
    }
}