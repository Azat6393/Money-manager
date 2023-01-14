package com.woynex.parasayar.feature_trans.presentation.daily

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.woynex.parasayar.R
import com.woynex.parasayar.core.domain.model.Currency
import com.woynex.parasayar.core.utils.OnItemClickListener
import com.woynex.parasayar.core.utils.SharedPreferencesHelper
import com.woynex.parasayar.databinding.FragmentDailyBinding
import com.woynex.parasayar.feature_trans.TransCoreViewModel
import com.woynex.parasayar.feature_trans.domain.model.Trans
import com.woynex.parasayar.feature_trans.presentation.adapter.DailyTransAdapterParent
import com.woynex.parasayar.feature_trans.presentation.trans.TransFragmentDirections
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@AndroidEntryPoint
class DailyFragment : Fragment(R.layout.fragment_daily), OnItemClickListener<Trans> {

    private lateinit var _binding: FragmentDailyBinding
    private val viewModel: DailyViewModel by viewModels()
    private val coreViewModel: TransCoreViewModel by activityViewModels()
    private val mAdapter: DailyTransAdapterParent by lazy { DailyTransAdapterParent(this) }

    private var selectedDate: LocalDate? = null
    private var selectedCurrency: Currency? = null

    @Inject
    lateinit var preferencesHelper: SharedPreferencesHelper

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentDailyBinding.bind(view)

        initRecyclerView()
        observe()
    }

    private fun initRecyclerView() {
        _binding.dailyRv.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
    }

    private fun observe() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.dailyTrans.collect { result ->
                    mAdapter.submitList(result)
                }
            }
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                coreViewModel.selectedDate.collect { date ->
                    selectedDate = date
                    viewModel.getMonthlyTrans(
                        month = date.monthValue,
                        year = date.year,
                        selectedCurrency?.symbol ?: preferencesHelper.getDefaultCurrency().symbol
                    )
                }
            }
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                coreViewModel.selectedCurrency.collect { result ->
                    selectedCurrency = result
                    viewModel.getMonthlyTrans(
                        month = selectedDate?.monthValue ?: LocalDate.now().monthValue,
                        year = selectedDate?.year ?: LocalDate.now().year,
                        selectedCurrency?.symbol ?: preferencesHelper.getDefaultCurrency().symbol
                    )
                }
            }
        }
    }

    override fun onClick(item: Trans) {
        val action = TransFragmentDirections.actionTransFragmentToTransEditFragment(item)
        findNavController().navigate(action)
    }
}