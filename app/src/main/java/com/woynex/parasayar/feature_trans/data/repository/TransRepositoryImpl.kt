package com.woynex.parasayar.feature_trans.data.repository

import com.woynex.parasayar.core.data.room.CurrencyDao
import com.woynex.parasayar.core.domain.model.Currency
import com.woynex.parasayar.core.utils.TransTypes
import com.woynex.parasayar.feature_trans.data.room.TransDao
import com.woynex.parasayar.feature_trans.domain.model.Trans
import com.woynex.parasayar.feature_trans.domain.repository.TransRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TransRepositoryImpl @Inject constructor(
    private val dao: TransDao,
    private val currencyDao: CurrencyDao
) : TransRepository {
    override suspend fun insertTrans(trans: Trans) {
        dao.insertTrans(trans)
    }

    override suspend fun updateTrans(trans: Trans) {
        dao.updateTrans(trans)
        if (trans.type == TransTypes.EXPENSE && trans.fee_trans_id != null) {
            dao.updateFeeAmountFromTrans(trans.amount, trans.fee_trans_id)
        }
    }

    override suspend fun deleteTrans(trans: Trans) {
        dao.deleteTrans(trans)
    }

    override suspend fun getTransById(id: Int): Trans {
        return dao.getTransById(id)
    }

    override fun getTransByDay(
        day: Int,
        month: Int,
        year: Int,
        currency: String
    ): Flow<List<Trans>> {
        return dao.getTransByDay(day, month, year, currency)
    }

    override fun getTransByWeek(month: Int, year: Int, currency: String): Flow<List<Trans>> {
        return dao.getTransByWeek(
            month,
            year,
            if (month == 12) 1 else month + 1,
            if (month == 1) 12 else month - 1, currency
        )
    }

    override fun getTransByYear(year: Int, currency: String): Flow<List<Trans>> {
        return dao.getTransByYear(year, currency)
    }

    override fun getAllTrans(currency: String): Flow<List<Trans>> {
        return dao.getAllTrans(currency)
    }

    override fun getTransByMonth(month: Int, year: Int, currency: String): Flow<List<Trans>> {
        return dao.getTransByMonth(month, year, currency)
    }

    override fun filterTransByMonthAndAccount(
        month: Int,
        accountName: String,
        year: Int
    ): Flow<List<Trans>> {
        return dao.filterTransByMonthAndAccount(month, accountName, year)
    }

    override fun filterTransByMonthAndExpensesOrIncome(
        month: Int,
        type: String,
        year: Int
    ): Flow<List<Trans>> {
        return dao.filterTransByMonthAndExpensesOrIncome(month, type, year)
    }

    override fun filterTransByMonthAndExpensesCategory(
        month: Int,
        category: String,
        type: String,
        year: Int
    ): Flow<List<Trans>> {
        return dao.filterTransByMonthAndExpensesCategory(month, category, type, year)
    }

    override fun filterTransByMonthAndIncomeCategory(
        month: Int,
        category: String,
        type: String,
        year: Int
    ): Flow<List<Trans>> {
        return dao.filterTransByMonthAndIncomeCategory(month, category, type, year)
    }

    override suspend fun updateFeeAmount(amount: Double, transId: String) {
        dao.updateFeeAmount(amount, transId)
    }

    override suspend fun deleteFeeTrans(transId: String) {
        dao.deleteFeeTrans(transId)
    }

    override fun getAllCurrency(): Flow<List<Currency>> {
        return currencyDao.getAllCurrency()
    }

    override fun getTransByCategory(
        category_id: Int,
        currency: String,
        month: Int,
        year: Int
    ): Flow<List<Trans>> {
        return dao.getTransByCategory(category_id, currency, month, year)
    }

    override fun getTransBySubcategory(
        subcategory_id: Int,
        currency: String,
        month: Int,
        year: Int
    ): Flow<List<Trans>> {
        return dao.getTransBySubcategory(subcategory_id, currency, month, year)
    }

    override fun getTransByCategoryYearly(
        category_id: Int,
        currency: String,
        year: Int
    ): Flow<List<Trans>> {
        return dao.getTransByCategoryYearly(category_id, currency, year)
    }

    override fun getTransBySubcategoryYearly(
        subcategory_id: Int,
        currency: String,
        year: Int
    ): Flow<List<Trans>> {
        return dao.getTransBySubcategoryYearly(subcategory_id, currency, year)
    }
}