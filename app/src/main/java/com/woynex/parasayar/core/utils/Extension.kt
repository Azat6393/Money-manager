package com.woynex.parasayar.core.utils

import android.view.View
import com.google.android.material.snackbar.Snackbar
import com.woynex.parasayar.feature_trans.domain.model.DailyTrans
import com.woynex.parasayar.feature_trans.domain.model.Trans
import com.woynex.parasayar.feature_trans.domain.model.YearInfo
import kotlin.math.exp


fun View.showSnackBar(text: String) {
    Snackbar.make(this, text, Snackbar.LENGTH_LONG).show()
}

fun List<Trans>.convertToDailyTransList(): List<DailyTrans> {
    val dailyTransList = arrayListOf<DailyTrans>()
    val newList = this.groupBy { it.day }.values
    newList.forEach { transList ->
        val allExpense = transList.filter { it.type == TransTypes.EXPENSE }
        val allIncome = transList.filter { it.type == TransTypes.INCOME }
        val newDailyTrans = DailyTrans(
            day = parseDayOfMonthDate(transList[0].date_in_millis),
            dayOfWeek = parseDayOfWeekDate(transList[0].date_in_millis),
            date = parseMonthYearDate(transList[0].date_in_millis),
            income = allIncome.sumOf { it.amount },
            expenses = allExpense.sumOf { it.amount },
            arrayList = transList.sortedByDescending { it.date_in_millis }
        )
        dailyTransList.add(newDailyTrans)
    }
    return dailyTransList.sortedByDescending { it.day }
}

fun List<Trans>.convertToYearInfo(): YearInfo {
    val income = this.filter { it.type == TransTypes.INCOME }.sumOf { it.amount }
    val expence = this.filter { it.type == TransTypes.EXPENSE }.sumOf { it.amount }
    return YearInfo(
        income = income,
        expence = expence,
        total = income - expence
    )
}