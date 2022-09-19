package com.woynex.parasayar.feature_trans.presentation.trans

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.woynex.parasayar.R
import com.woynex.parasayar.core.utils.parseDateText
import com.woynex.parasayar.databinding.FragmentTransBinding
import com.woynex.parasayar.feature_trans.presentation.adapter.ViewPagerAdapter
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate

@AndroidEntryPoint
class TransFragment : Fragment(R.layout.fragment_trans) {

    private lateinit var _binding: FragmentTransBinding

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentTransBinding.bind(view)

        var date = LocalDate.now()

        _binding.apply {
            dateTv.text = parseDateText(date)
            dateLeftBtn.setOnClickListener {
                date = date.minusMonths(1)
                dateTv.text = parseDateText(date)
            }
            dateRightBtn.setOnClickListener {
                date = date.plusMonths(1)
                dateTv.text = parseDateText(date)
            }
            dateTv.setOnClickListener {
                SelectMonthDialog(date) { newDate ->
                    date = newDate
                    dateTv.text = parseDateText(date)
                }.show(childFragmentManager, "SelectMonthDialog")
            }
            initViewPager()
        }
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
        }
    }
}