package com.woynex.parasayar.feature_auth.presentation.sign_in

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.woynex.parasayar.R
import com.woynex.parasayar.core.utils.showToastMessage
import com.woynex.parasayar.databinding.BottomSheetForgotPasswordBinding
import com.woynex.parasayar.feature_auth.presentation.AuthViewModel

class ForgotPasswordBottomSheet(private val viewModel: AuthViewModel) :
    BottomSheetDialogFragment() {

    private lateinit var _binding: BottomSheetForgotPasswordBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_sheet_forgot_password, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = BottomSheetForgotPasswordBinding.bind(view)

        _binding.forgotBackButton.setOnClickListener {
            this.dismiss()
        }
        _binding.forgotSendButton.setOnClickListener {
            if (_binding.forgotEditText.text.toString().isBlank()) {
                requireContext().showToastMessage(getString(R.string.please_input_email_address))
            } else {
                isLoading(true)
                val email = _binding.forgotEditText.text.toString()
                viewModel.forgotPassword(
                    email = email,
                    onSuccess = {
                        isLoading(false)
                        requireContext().showToastMessage(getString(R.string.please_check_your_mail))
                        this.dismiss()
                    },
                    onError = {
                        isLoading(false)
                        it.localizedMessage?.let { it1 -> requireContext().showToastMessage(it1) }

                    }
                )
            }
        }
    }

    private fun isLoading(state: Boolean) {
        _binding.apply {
            forgotProgressBar.isVisible = state
            forgotSendButton.isVisible = !state
        }
    }
}