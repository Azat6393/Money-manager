package com.woynex.parasayar.feature_auth.presentation.sign_up

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.woynex.parasayar.R
import com.woynex.parasayar.databinding.BottomSheetCountriesBinding
import com.woynex.parasayar.feature_auth.domain.model.CountryInfo
import com.woynex.parasayar.feature_auth.presentation.adapter.CountryAdapter

class CountriesDialog(
    private val countryList: List<CountryInfo>,
    private val selectedCountry: (CountryInfo) -> Unit
) : DialogFragment(),
    CountryAdapter.OnItemClickListener {

    private lateinit var _binding: BottomSheetCountriesBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_sheet_countries, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = BottomSheetCountriesBinding.bind(view)

        val mAdapter = CountryAdapter(this)
        _binding.recyclerView.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
        mAdapter.submitList(countryList)
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
    }

    override fun onClick(countryInfo: CountryInfo) {
        selectedCountry(countryInfo)
        this.dismiss()
    }
}