package com.woynex.parasayar.feature_trans.presentation.weekly

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.woynex.parasayar.R
import com.woynex.parasayar.core.domain.model.Currency
import com.woynex.parasayar.core.utils.SharedPreferencesHelper
import com.woynex.parasayar.databinding.FragmentWeeklyBinding
import com.woynex.parasayar.feature_trans.TransCoreViewModel
import com.woynex.parasayar.feature_trans.presentation.adapter.WeekTransAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@AndroidEntryPoint
class WeeklyFragment : Fragment(R.layout.fragment_weekly) {

    private lateinit var _binding: FragmentWeeklyBinding
    private val coreViewModel: TransCoreViewModel by activityViewModels()
    private val viewModel: WeeklyViewModel by viewModels()
    private val mAdapter: WeekTransAdapter by lazy { WeekTransAdapter() }


    private var selectedDate: LocalDate? = null
    private var selectedCurrency: Currency? = null

    @Inject
    lateinit var preferencesHelper: SharedPreferencesHelper

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentWeeklyBinding.bind(view)

        initRecyclerView()
        observe()
    }

    private fun observe() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.transList.collect { result ->
                    mAdapter.submitList(result)
                }
            }
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                coreViewModel.selectedDate.collect { date ->
                    selectedDate = date
                    viewModel.getTransByMonth(
                        date,
                        selectedCurrency?.symbol ?: preferencesHelper.getDefaultCurrency().symbol
                    )
                }
            }
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                coreViewModel.selectedCurrency.collect { result ->
                    selectedCurrency = result
                    viewModel.getTransByMonth(
                        selectedDate ?: LocalDate.now(),
                        selectedCurrency?.symbol ?: preferencesHelper.getDefaultCurrency().symbol
                    )
                }
            }
        }
    }

    private fun initRecyclerView() {
        _binding.monthlyTv.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }
}