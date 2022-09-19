package com.woynex.parasayar.feature_trans.presentation.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.woynex.parasayar.feature_trans.presentation.calendar.CalendarFragment
import com.woynex.parasayar.feature_trans.presentation.daily.DailyFragment
import com.woynex.parasayar.feature_trans.presentation.mounthly.MonthlyFragment
import com.woynex.parasayar.feature_trans.presentation.total.TotalFragment
import com.woynex.parasayar.feature_trans.presentation.weekly.WeeklyFragment

class ViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int {
        return 5
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> DailyFragment()
            1 -> CalendarFragment()
            2 -> WeeklyFragment()
            3 -> MonthlyFragment()
            4 -> TotalFragment()
            else -> DailyFragment()
        }
    }
}