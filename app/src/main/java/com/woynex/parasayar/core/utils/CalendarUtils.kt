package com.woynex.parasayar.core.utils

import com.woynex.parasayar.feature_trans.domain.model.CalendarDay
import com.woynex.parasayar.feature_trans.domain.model.Trans
import com.woynex.parasayar.feature_trans.domain.model.WeekTrans
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.Month
import java.time.YearMonth
import java.time.temporal.TemporalAdjuster
import java.time.temporal.TemporalAdjusters

class CalendarUtils {

    companion object {

        fun daysInMonthList(
            selectedDate: LocalDate,
            mDate: LocalDate,
            transList: List<Trans>
        ): ArrayList<CalendarDay?> {
            val daysInMonthList = arrayListOf<CalendarDay?>()
            val yearMonth = YearMonth.from(mDate)

            val daysInMonth = yearMonth.lengthOfMonth()

            val firstOfMonth = selectedDate.withDayOfMonth(1)
            val dayOfWeek = firstOfMonth.dayOfWeek.value

            for (i in 1..42) {
                if (i <= dayOfWeek || i > daysInMonth + dayOfWeek) {
                    daysInMonthList.add(null)
                } else {
                    val date = LocalDate.of(
                        selectedDate.year,
                        selectedDate.month,
                        i - dayOfWeek
                    )
                    val income =
                        transList.filter { it.day == date.dayOfMonth && it.type == TransTypes.INCOME }
                            .sumOf { it.amount }
                    val expense =
                        transList.filter { it.day == date.dayOfMonth && it.type == TransTypes.EXPENSE }
                            .sumOf { it.amount }
                    val total = income - expense
                    if (income == 0.0 && expense == 0.0 && total == 0.0) {
                        daysInMonthList.add(
                            CalendarDay(
                                date = date,
                                income = null,
                                expense = null,
                                total = null
                            )
                        )
                    } else {
                        daysInMonthList.add(
                            CalendarDay(
                                date = date,
                                income = income,
                                expense = expense,
                                total = total
                            )
                        )
                    }
                }
            }
            return daysInMonthList
        }
    }
}
