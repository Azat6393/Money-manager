package com.woynex.parasayar.feature_trans.presentation.mounthly

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
import com.woynex.parasayar.databinding.FragmentMonthlyBinding
import com.woynex.parasayar.feature_trans.TransCoreViewModel
import com.woynex.parasayar.feature_trans.presentation.adapter.YearTransAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MonthlyFragment : Fragment(R.layout.fragment_monthly) {

    private lateinit var _binding: FragmentMonthlyBinding
    private val mAdapter: YearTransAdapter by lazy { YearTransAdapter() }
    private val viewModel: MonthlyViewModel by viewModels()
    private val coreViewModel: TransCoreViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentMonthlyBinding.bind(view)

        initRecyclerView()
        observe()
    }

    private fun initRecyclerView() {
        _binding.monthlyTv.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun observe() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.yearTrans.collect { result ->
                    mAdapter.submitList(result)
                }
            }
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                coreViewModel.selectedDate.collect { date ->
                    viewModel.getYearTrans(date)
                }
            }
        }
    }
}