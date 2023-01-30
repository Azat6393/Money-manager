package com.woynex.parasayar.feature_settings.presentation.settings

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.woynex.parasayar.AuthActivity
import com.woynex.parasayar.R
import com.woynex.parasayar.core.utils.CategoryTypes
import com.woynex.parasayar.core.utils.SharedPreferencesHelper
import com.woynex.parasayar.core.utils.showAlertDialog
import com.woynex.parasayar.core.utils.showToastMessage
import com.woynex.parasayar.databinding.FragmentSettingsBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlin.math.sign

@AndroidEntryPoint
class SettingsFragment : Fragment(R.layout.fragment_settings) {

    private lateinit var _binding: FragmentSettingsBinding
    private val viewModel: SettingsViewModel by viewModels()

    private var isEditMode = false

    @Inject
    lateinit var sharedPreferencesHelper: SharedPreferencesHelper

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSettingsBinding.bind(view)

        initView()
    }

    private fun initView(){
        _binding.apply {
            changeEditMode(false)
            val name = "${sharedPreferencesHelper.getUser().first_name} ${sharedPreferencesHelper.getUser().last_name}"
            nameTv.setText(name)
            setting.setOnClickListener {
                val action =
                    SettingsFragmentDirections.actionSettingsFragmentToConfigurationFragment()
                findNavController().navigate(action)
            }
            webSite.setOnClickListener { navigateToWebView("http://woynapp.com") }
            privacyPolicy.setOnClickListener { navigateToWebView("http://woynapp.com/gizlilik-politikasi/") }
            termOfService.setOnClickListener { navigateToWebView("http://woynapp.com/terms-of-service/") }
            signOutBtn.setOnClickListener {
                requireActivity().showAlertDialog(
                    title = getString(R.string.sign_out),
                    message = getString(R.string.sign_out_message)
                ){
                    viewModel.signOut()
                    val intent = Intent(requireActivity(), AuthActivity::class.java)
                    startActivity(intent)
                    requireActivity().finish()
                }
            }
            aboutUsBtn.setOnClickListener {
                val action = SettingsFragmentDirections.actionSettingsFragmentToAboutUsFragment()
                findNavController().navigate(action)
            }
            editButton.setOnClickListener {
                changeEditMode(true)
            }
            saveButton.setOnClickListener {
                saveChanges()
                changeEditMode(false)
            }
            darkModeSwitch.isChecked = sharedPreferencesHelper.darkMode
            darkModeSwitch.setOnCheckedChangeListener { compoundButton, b ->
                if (b) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    sharedPreferencesHelper.darkMode = b
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    sharedPreferencesHelper.darkMode = b
                }
            }
        }
    }

    private fun saveChanges() {
        val newName = _binding.nameTv.text.toString()
        if (newName.isBlank()){
            requireContext().showToastMessage(getString(R.string.please_input_first_and_lastname))
        }else {
            viewModel.updateName(name = newName)
        }
    }

    private fun changeEditMode(state: Boolean) {
        isEditMode = state
        _binding.apply {
            editButton.isVisible = !state
            saveButton.isVisible = state
            if (state) {
                nameTv.inputType = InputType.TYPE_CLASS_TEXT
                nameTv.isEnabled = true
            } else {
                nameTv.inputType = InputType.TYPE_NULL
                nameTv.isEnabled = false
            }
        }
    }

    private fun navigateToWebView(url: String) {
        val action = SettingsFragmentDirections.actionSettingsFragmentToWebViewFragment(url)
        findNavController().navigate(action)
    }
}