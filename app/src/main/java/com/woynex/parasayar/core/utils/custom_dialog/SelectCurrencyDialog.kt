package com.woynex.parasayar.core.utils.custom_dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.woynex.parasayar.R
import com.woynex.parasayar.core.domain.model.Currency
import com.woynex.parasayar.core.presentation.currency.CurrenciesViewModel
import com.woynex.parasayar.core.utils.adapter.CurrencyAdapter
import com.woynex.parasayar.core.utils.fromJsonToCurrency
import com.woynex.parasayar.core.utils.getJsonFromAssets
import com.woynex.parasayar.core.utils.showToastMessage
import com.woynex.parasayar.databinding.DialogAddCurrencyBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class SelectCurrencyDialog(
    private val viewModel: CurrenciesViewModel,
    private val onSelect: (Currency) -> Unit
) : DialogFragment(), CurrencyAdapter.CurrencyItemListener {

    private lateinit var _binding: DialogAddCurrencyBinding
    private val mAdapter: CurrencyAdapter by lazy { CurrencyAdapter(this, false) }
    private var currencyList: List<Currency>? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_add_currency, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = DialogAddCurrencyBinding.bind(view)

        _binding.currencyRv.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
        observe()
        viewModel.getCurrencies(requireContext())

        _binding.outlinedTextField.editText?.doAfterTextChanged { text ->
            val newList =
                currencyList?.filter {
                    it.name.lowercase().startsWith(text.toString().lowercase()) ||
                            it.cc.lowercase().startsWith(text.toString().lowercase())
                }
            mAdapter.submitList(newList)
        }
    }

    private fun observe() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.allCurrencies.collect { result ->
                    currencyList = result
                    mAdapter.submitList(result)
                }
            }
        }
    }

    override fun onClick(item: Currency) {
        onSelect(item)
        this.dismiss()
    }

    override fun onDelete(item: Currency) {

    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
    }
}