package com.woynex.parasayar.feature_accounts.presentation.add_account

import android.os.Bundle
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.woynex.parasayar.R
import com.woynex.parasayar.databinding.FragmentAddAccountBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddAccountFragment: Fragment(R.layout.fragment_add_account) {

    private lateinit var _binding: FragmentAddAccountBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAddAccountBinding.bind(view)

        _binding.apply {
            backBtn.setOnClickListener {
                findNavController().popBackStack()
            }
        }
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