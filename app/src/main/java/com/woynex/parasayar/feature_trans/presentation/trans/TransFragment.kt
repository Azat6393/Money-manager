package com.woynex.parasayar.feature_trans.presentation.trans

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.woynex.parasayar.R
import com.woynex.parasayar.core.utils.parseDateText
import com.woynex.parasayar.core.utils.parseYear
import com.woynex.parasayar.databinding.FragmentTransBinding
import com.woynex.parasayar.feature_trans.TransCoreViewModel
import com.woynex.parasayar.feature_trans.presentation.adapter.ViewPagerAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.time.LocalDate

@AndroidEntryPoint
class TransFragment : Fragment(R.layout.fragment_trans) {

    private lateinit var _binding: FragmentTransBinding
    private val coreViewModel: TransCoreViewModel by activityViewModels()
    private lateinit var date: LocalDate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentTransBinding.bind(view)

        date = LocalDate.now()
        updateDate()

        _binding.apply {
            addTransFab.setOnClickListener {
                val action = TransFragmentDirections.actionTransFragmentToTransDetailsFragment()
                findNavController().navigate(action)
            }
        }

        parseWithMonth()
        initViewPager()
        observe()
    }

    private fun observe() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                coreViewModel.yearInfo.collect { result ->
                    _binding.income.text = result?.income.toString()
                    _binding.expenses.text = result?.expence.toString()
                    _binding.total.text = result?.total.toString()
                }
            }
        }
    }

    private fun parseOnlyYear() {
        _binding.apply {
            dateTv.text = parseYear(date)
            dateLeftBtn.setOnClickListener {
                date = date.minusYears(1)
                dateTv.text = parseYear(date)
                updateDate()
            }
            dateRightBtn.setOnClickListener {
                date = date.plusYears(1)
                dateTv.text = parseYear(date)
                updateDate()
            }
            dateTv.isEnabled = false
        }
    }

    private fun parseWithMonth() {
        _binding.apply {
            dateTv.text = parseDateText(date)
            dateLeftBtn.setOnClickListener {
                date = date.minusMonths(1)
                dateTv.text = parseDateText(date)
                updateDate()
            }
            dateRightBtn.setOnClickListener {
                date = date.plusMonths(1)
                dateTv.text = parseDateText(date)
                updateDate()
            }
            dateTv.setOnClickListener {
                SelectMonthDialog(date) { newDate ->
                    date = newDate
                    dateTv.text = parseDateText(date)
                    updateDate()
                }.show(childFragmentManager, "SelectMonthDialog")
            }
            dateTv.isEnabled = true
        }
    }

    private fun updateDate() {
        coreViewModel.updateDate(date)
        coreViewModel.getTransByYear()
    }

    private fun initViewPager() {
        _binding.apply {
            viewPager.adapter = ViewPagerAdapter(this@TransFragment)
            viewPager.offscreenPageLimit = 1
            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                when (position) {
                    0 -> tab.text = getString(R.string.daily)
                    1 -> tab.text = getString(R.string.calendar)
                    2 -> tab.text = getString(R.string.weekly)
                    3 -> tab.text = getString(R.string.monthly)
                    4 -> tab.text = getString(R.string.total)
                }
            }.attach()
            viewPager.isUserInputEnabled = false
            tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    tab?.let {
                        when (tab.position) {
                            0 -> parseWithMonth()
                            1 -> parseWithMonth()
                            2 -> parseWithMonth()
                            3 -> parseOnlyYear()
                            4 -> parseWithMonth()
                        }
                    }
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                }

                override fun onTabReselected(tab: TabLayout.Tab?) {
                }
            })
        }
    }
}