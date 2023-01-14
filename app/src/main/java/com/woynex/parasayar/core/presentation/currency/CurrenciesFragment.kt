package com.woynex.parasayar.core.presentation.currency

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.woynex.parasayar.R
import com.woynex.parasayar.core.domain.model.Currency
import com.woynex.parasayar.core.utils.adapter.CurrencyAdapter
import com.woynex.parasayar.core.utils.custom_dialog.AddCurrencyDialog
import com.woynex.parasayar.databinding.FragmentCurrenciesBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CurrenciesFragment : Fragment(R.layout.fragment_currencies),
    CurrencyAdapter.CurrencyItemListener {

    private lateinit var _binding: FragmentCurrenciesBinding
    private val viewModel: CurrenciesViewModel by viewModels()
    private val mAdapter: CurrencyAdapter by lazy { CurrencyAdapter(this, true) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentCurrenciesBinding.bind(view)

        initView()
        observe()
        viewModel.getAllCurrencies()
    }

    private fun observe() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.currencies.collect { result ->
                    mAdapter.submitList(result)
                }
            }
        }
    }

    private fun initView() {
        _binding.currencyRv.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
        _binding.addBtn.setOnClickListener {
            AddCurrencyDialog(viewModel = viewModel).show(
                childFragmentManager,
                "Add Currency Dialog"
            )
        }
        _binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun onClick(item: Currency) {
        findNavController().popBackStack()
    }

    override fun onDelete(item: Currency) {
        viewModel.deleteCurrency(currency = item)
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