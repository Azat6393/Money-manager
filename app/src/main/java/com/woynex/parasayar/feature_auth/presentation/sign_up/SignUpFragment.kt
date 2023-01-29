package com.woynex.parasayar.feature_auth.presentation.sign_up

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import coil.decode.SvgDecoder
import coil.load
import coil.transform.CircleCropTransformation
import com.woynex.parasayar.MainActivity
import com.woynex.parasayar.R
import com.woynex.parasayar.core.domain.model.Currency
import com.woynex.parasayar.core.domain.model.User
import com.woynex.parasayar.core.presentation.currency.CurrenciesViewModel
import com.woynex.parasayar.core.utils.*
import com.woynex.parasayar.core.utils.custom_dialog.SelectCurrencyDialog
import com.woynex.parasayar.databinding.FragmentSignUpBinding
import com.woynex.parasayar.feature_auth.domain.model.CountryInfo
import com.woynex.parasayar.feature_auth.presentation.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SignUpFragment : Fragment(R.layout.fragment_sign_up) {

    private lateinit var _binding: FragmentSignUpBinding
    private val viewModel: AuthViewModel by viewModels()
    private val currencyViewModel: CurrenciesViewModel by viewModels()

    private var selectedCountry: CountryInfo? = null
    private var selectedCurrency: Currency? = null

    @Inject
    lateinit var sharedPreferencesHelper: SharedPreferencesHelper

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSignUpBinding.bind(view)

        initView()
        observe()
    }

    private fun observe() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.signUpResponse.collect { result ->
                    when (result) {
                        is Resource.Empty -> isLoading(false)
                        is Resource.Error -> {
                            isLoading(false)
                            result.message?.let { requireContext().showToastMessage(it) }
                        }
                        is Resource.Loading -> isLoading(true)
                        is Resource.Success -> {
                            sharedPreferencesHelper.setDefaultCurrency(selectedCurrency!!)
                            val intent = Intent(requireActivity(), MainActivity::class.java)
                            startActivity(intent)
                            isLoading(false)
                            requireActivity().finish()
                        }
                    }
                }
            }
        }
    }

    private fun initView() {
        _binding.apply {
            defaultCurrency.setOnFocusChangeListener { v, hasFocus ->
                if (hasFocus) {
                    SelectCurrencyDialog(
                        viewModel = currencyViewModel
                    ) {
                        defaultCurrency.clearFocus()
                        selectedCurrency = it
                        defaultCurrency.setText("${it.cc} - ${it.name} (${it.symbol})")
                    }.show(childFragmentManager, "Select Currency")
                }
            }
            signUp.setOnClickListener {
                if (isAllInputsFilled()) {
                    viewModel.signUpWithEmail(
                        user = User(
                            first_name = nameEt.text.toString(),
                            last_name = lastNameEt.text.toString(),
                            phone_number = "${selectedCountry?.number}${_binding.phoneNumberTv.text}",
                            email = _binding.emailEt.text.toString()
                        ),
                        password = passwordEt.text.toString()
                    )
                }
            }
        }
        initAutoComplete()
    }

    private fun isLoading(state: Boolean) {
        _binding.apply {
            progressBar.isVisible = state
            signUp.isVisible = !state
        }
    }

    private fun initAutoComplete() {
        val countryList = getJsonFromAssets(requireContext(), Constants.countryListJsonName)
            ?.fromJsonToCountyList()
        val currentCountry = countryList?.find { it.name == "Turkey" }
        selectedCountry = currentCountry
        currentCountry?.let { fillCountryInfo(it) }
        _binding.apply {
            countryInfoContainer.setOnClickListener {
                if (countryList != null) {
                    CountriesDialog(countryList) {
                        fillCountryInfo(it)
                    }.show(childFragmentManager, "Country Dialog")
                }
            }
        }
    }

    private fun fillCountryInfo(countryInfo: CountryInfo) {
        _binding.apply {
            countryFlag.load(countryInfo.flag) {
                crossfade(true)
                placeholder(R.drawable.add_image_icon)
                decoderFactory(SvgDecoder.Factory())
                transformations(CircleCropTransformation())
                build()
            }
            countryCodeNumber.text = countryInfo.number
            selectedCountry = countryInfo
        }
    }

    private fun isAllInputsFilled(): Boolean {
        return when {
            _binding.emailEt.text.toString().isBlank() -> {
                requireContext().showToastMessage(getString(R.string.please_input_email_address))
                return false
            }
            _binding.passwordEt.text.toString().isBlank() -> {
                requireContext().showToastMessage(getString(R.string.please_input_password))
                return false
            }
            _binding.phoneNumberTv.text.toString().isBlank() -> {
                requireContext().showToastMessage(getString(R.string.input_phone_number))
                return false
            }
            _binding.defaultCurrency.text.toString().isBlank() -> {
                requireContext().showToastMessage(getString(R.string.choose_default_currency))
                return false
            }
            _binding.passwordEt.text.toString().length <= 5 -> {
                requireContext().showToastMessage(getString(R.string.password_characters_error))
                return false
            }
            _binding.nameEt.text.toString().isBlank() -> {
                requireContext().showToastMessage(getString(R.string.input_first_name))
                return false
            }
            _binding.lastNameEt.text.toString().isBlank() -> {
                requireContext().showToastMessage(getString(R.string.input_last_name))
                return false
            }
            else -> {
                true
            }
        }
    }
}