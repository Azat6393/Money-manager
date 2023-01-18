package com.woynex.parasayar.feature_statistics.presentation.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.woynex.parasayar.feature_statistics.presentation.expenses.StatisticsExpensesFragment
import com.woynex.parasayar.feature_statistics.presentation.income.StatisticsIncomeFragment
import com.woynex.parasayar.feature_trans.presentation.calendar.CalendarFragment
import com.woynex.parasayar.feature_trans.presentation.daily.DailyFragment
import com.woynex.parasayar.feature_trans.presentation.mounthly.MonthlyFragment
import com.woynex.parasayar.feature_trans.presentation.total.TotalFragment
import com.woynex.parasayar.feature_trans.presentation.weekly.WeeklyFragment

class StatisticsViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> StatisticsIncomeFragment()
            1 -> StatisticsExpensesFragment()
            else -> StatisticsIncomeFragment()
        }
    }
}