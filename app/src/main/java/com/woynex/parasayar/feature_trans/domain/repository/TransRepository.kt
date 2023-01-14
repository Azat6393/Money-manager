package com.woynex.parasayar.feature_trans.domain.repository

import com.woynex.parasayar.core.domain.model.Currency
import com.woynex.parasayar.core.utils.TransTypes
import com.woynex.parasayar.feature_trans.domain.model.Trans
import kotlinx.coroutines.flow.Flow

interface TransRepository {

    suspend fun insertTrans(trans: Trans)

    suspend fun updateTrans(trans: Trans)

    suspend fun deleteTrans(trans: Trans)

    suspend fun getTransById(id: Int): Trans

    fun getTransByDay(day: Int, month: Int, year: Int, currency: String): Flow<List<Trans>>

    fun getTransByYear(year: Int, currency: String): Flow<List<Trans>>

    fun getAllTrans(currency: String): Flow<List<Trans>>

    fun getTransByMonth(month: Int, year: Int, currency: String): Flow<List<Trans>>

    fun getTransByWeek(month: Int, year: Int, currency: String): Flow<List<Trans>>

    fun filterTransByMonthAndAccount(month: Int, accountName: String, year: Int): Flow<List<Trans>>

    fun filterTransByMonthAndExpensesOrIncome(month: Int, type: String, year: Int): Flow<List<Trans>>

    fun filterTransByMonthAndExpensesCategory(
        month: Int,
        category: String,
        type: String = TransTypes.EXPENSE,
        year: Int
    ): Flow<List<Trans>>

    fun filterTransByMonthAndIncomeCategory(
        month: Int,
        category: String,
        type: String = TransTypes.INCOME,
        year: Int
    ): Flow<List<Trans>>

    suspend fun updateFeeAmount(amount: Double, transId: String)

    suspend fun deleteFeeTrans(transId: String)

    fun getAllCurrency(): Flow<List<Currency>>
}