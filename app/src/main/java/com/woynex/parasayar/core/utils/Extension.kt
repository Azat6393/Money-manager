package com.woynex.parasayar.core.utils

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.woynex.parasayar.R
import com.woynex.parasayar.core.domain.model.Currency
import com.woynex.parasayar.feature_statistics.domain.model.CategoryStatistics
import com.woynex.parasayar.feature_trans.domain.model.*
import java.security.Timestamp
import java.time.*
import java.time.temporal.TemporalAdjuster
import java.time.temporal.TemporalAdjusters
import java.time.temporal.TemporalQueries.localDate


fun String.fromJsonToCurrency(): List<Currency> {
    val gson = Gson()
    return gson.fromJson(this, Array<Currency>::class.java).asList()
}

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

fun List<Trans>.convertToDailyTransListForAccountDetails(accountId: Int): List<DailyTrans> {
    val dailyTransList = arrayListOf<DailyTrans>()
    val newList = this.groupBy { it.day }.values
    newList.forEach { transList ->
        var allExpense = transList.filter { it.type == TransTypes.EXPENSE }.sumOf { it.amount }
        var allIncome = transList.filter { it.type == TransTypes.INCOME }.sumOf { it.amount }
        allIncome += transList.filter { it.type == TransTypes.TRANSFER && it.to_account_id == accountId }.sumOf { it.amount }
        allExpense += transList.filter { it.type == TransTypes.TRANSFER && it.account_id == accountId }.sumOf { it.amount }
        val newDailyTrans = DailyTrans(
            day = parseDayOfMonthDate(transList[0].date_in_millis),
            dayOfWeek = parseDayOfWeekDate(transList[0].date_in_millis),
            date = parseMonthYearDate(transList[0].date_in_millis),
            income = allIncome,
            expenses = allExpense,
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

fun List<Trans>.convertToYearInfoForAccountDetails(accountId: Int): YearInfo {
    var income = this.filter { it.type == TransTypes.INCOME }.sumOf { it.amount }
    var expence = this.filter { it.type == TransTypes.EXPENSE }.sumOf { it.amount }
    income += this.filter { it.type == TransTypes.TRANSFER && it.to_account_id == accountId }.sumOf { it.amount }
    expence += this.filter { it.type == TransTypes.TRANSFER && it.account_id == accountId }.sumOf { it.amount }
    return YearInfo(
        income = income,
        expence = expence,
        total = income - expence
    )
}

fun List<Trans>.convertToYearTrans(date: LocalDate, currency: String): List<YearTrans> {
    val yearTransList = arrayListOf<YearTrans>()
    for (i in 1..12) {
        val monthTrans = this.filter { it.month == i }
        val income = monthTrans.filter { it.type == TransTypes.INCOME }.sumOf { it.amount }
        val expense = monthTrans.filter { it.type == TransTypes.EXPENSE }.sumOf { it.amount }
        val total = income - expense
        yearTransList.add(
            YearTrans(
                date = LocalDate.of(
                    date.year,
                    i,
                    date.dayOfMonth
                ),
                income = income,
                expence = expense,
                total = total,
                currency = currency
            )
        )
    }
    return yearTransList
}


fun List<Trans>.convertToWeekTrans(today: LocalDate, currency: String): List<WeekTrans> {
    val weekTrans = arrayListOf<WeekTrans>()

    val ym: YearMonth = YearMonth.of(today.year, today.month)

    val firstOfMonth: LocalDate = ym.atDay(1)
    val ta: TemporalAdjuster = TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY)
    val previousOrSameMonday = firstOfMonth.with(ta)

    val endOfMonth = ym.atEndOfMonth()
    var weekStart = previousOrSameMonday
    do {
        val weekStop = weekStart.plusDays(6)
        var income = 0.00
        var expense = 0.00
        var total = 0.00
        var currentDate = weekStart
        do {
            income += this.filter {
                it.type == TransTypes.INCOME && it.day == currentDate.dayOfMonth && it.month == currentDate.monthValue && it.currency == currency
            }.sumOf { it.amount }
            expense += this.filter {
                it.type == TransTypes.EXPENSE && it.day == currentDate.dayOfMonth && it.month == currentDate.monthValue && it.currency == currency
            }.sumOf { it.amount }
            currentDate = currentDate.plusDays(1)
        } while (currentDate.dayOfWeek.value <= weekStop.dayOfWeek.value)
        total = income - expense
        weekTrans.add(
            WeekTrans(
                startWeek = weekStart,
                endWeek = weekStop,
                income = income,
                expense = expense,
                total = total,
                currency = currency
            )
        )
        weekStart = weekStart.plusWeeks(1)
    } while (!weekStart.isAfter(endOfMonth))
    return weekTrans
}

fun Context.checkPermission(permission: String): Int {
    if (ContextCompat.checkSelfPermission(
            this, permission
        ) == PackageManager.PERMISSION_GRANTED
    ) {
        return 0
    }
    return -1
}

fun Context.showAlertDialog(
    title: String,
    message: String,
    onPositive: () -> Unit
) {
    val dialog = AlertDialog.Builder(this)
    dialog
        .setTitle(title)
        .setMessage(message)
        .setPositiveButton(
            getString(R.string.yes)
        ) { dialog, which -> onPositive() }
        .setNegativeButton(
            getString(R.string.no)
        ) { dialog, which -> dialog.dismiss() }
    dialog.show()
}

fun Context.showToastMessage(
    message: String
) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

fun Uri.uriToBitmap(context: Context): Bitmap {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        val source = ImageDecoder.createSource(context.contentResolver, this)
        ImageDecoder.decodeBitmap(source)
    } else {
        MediaStore.Images.Media.getBitmap(context.contentResolver, this)
    }
}

fun Map<String?, List<Trans>>.convertToCategoryStatisticsList(currency: String): List<CategoryStatistics> {

    val categoryStatistics = mutableListOf<CategoryStatistics>()
    val total = this.map { it -> it.value.sumOf { it.amount } }.sumOf { it }

    this.forEach { (t, u) ->
        val totalAmount = u.sumOf { it.amount }
        val percentage = ((totalAmount / total) * 100).toInt()
        val color = interpolateColor((totalAmount / total).toFloat())
        categoryStatistics.add(
            CategoryStatistics(
                category_name = t ?: "",
                category_id = u[0].category_id,
                total_amount = totalAmount,
                currency = currency,
                percentage = percentage,
                color = color
            )
        )
    }
    return categoryStatistics.sortedByDescending { it.percentage }
}

private fun interpolate(a: Float, b: Float, proportion: Float): Float {
    return a + (b - a) * proportion
}


private fun interpolateColor(proportion: Float): Int {
    val hsva = FloatArray(3)
    val hsvb = FloatArray(3)
    Color.colorToHSV(Color.YELLOW, hsva)
    Color.colorToHSV(Color.RED, hsvb)
    for (i in 0..2) {
        hsvb[i] = interpolate(hsva[i], hsvb[i], proportion)
    }
    return Color.HSVToColor(hsvb)
}

fun LocalDate.toMillisecond(): Long{
    return this.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()
}