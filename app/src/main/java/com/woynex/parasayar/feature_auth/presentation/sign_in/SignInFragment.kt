package com.woynex.parasayar.feature_auth.presentation.sign_in

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.woynex.parasayar.MainActivity
import com.woynex.parasayar.R
import com.woynex.parasayar.core.utils.Resource
import com.woynex.parasayar.core.utils.showSnackBar
import com.woynex.parasayar.databinding.FragmentSignInBinding
import com.woynex.parasayar.feature_auth.presentation.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignInFragment : Fragment(R.layout.fragment_sign_in) {

    private lateinit var _binding: FragmentSignInBinding
    private val viewModel: AuthViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSignInBinding.bind(view)
        initView()
        observe()
    }

    private fun initView() {
        _binding.apply {
            loginBtn.setOnClickListener {
                if (isValid()) {
                    val email = _binding.emailEt.text.toString()
                    val password = _binding.passwordEt.text.toString()
                    viewModel.signInWithEmail(
                        email = email,
                        password = password
                    )
                }
            }
            signUpBtn.setOnClickListener {
                val action = SignInFragmentDirections.actionSignInFragmentToSignUpFragment()
                findNavController().navigate(action)
            }
            forgotPasswordBtn.setOnClickListener {
                ForgotPasswordBottomSheet(viewModel).show(childFragmentManager, "Forgot password")
            }
        }
    }

    private fun observe() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.signInResponse.collect { result ->
                    when (result) {
                        is Resource.Empty -> isLoading(false)
                        is Resource.Error -> {
                            viewModel.clear()
                            isLoading(false)
                            requireView().showSnackBar(
                                result.message ?: getString(
                                    R.string.error_message
                                )
                            )
                        }
                        is Resource.Loading -> isLoading(true)
                        is Resource.Success -> {
                            isLoading(false)
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

    private fun isValid(): Boolean {
        val email = _binding.emailEt.text.toString()
        val password = _binding.passwordEt.text.toString()
        return when {
            email.isBlank() -> {
                requireView().showSnackBar(getString(R.string.please_input_email_address))
                false
            }
            password.isBlank() -> {
                requireView().showSnackBar(getString(R.string.please_input_password))
                false
            }
            else -> true
        }
    }

    private fun isLoading(state: Boolean) {
        _binding.apply {
            progressBar.isVisible = state
            loginBtn.isVisible = !state
            signUpBtn.isVisible = !state
        }
    }
}