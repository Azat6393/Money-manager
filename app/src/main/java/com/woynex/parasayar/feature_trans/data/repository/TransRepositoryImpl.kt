package com.woynex.parasayar.feature_trans.data.repository

import com.woynex.parasayar.core.utils.TransTypes
import com.woynex.parasayar.feature_trans.data.room.TransDao
import com.woynex.parasayar.feature_trans.domain.model.Trans
import com.woynex.parasayar.feature_trans.domain.repository.TransRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TransRepositoryImpl @Inject constructor(
    private val dao: TransDao
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

    override fun getTransByDay(day: Int, month: Int, year: Int): Flow<List<Trans>> {
        return dao.getTransByDay(day, month, year)
    }

    override fun getTransByWeek(month: Int, year: Int): Flow<List<Trans>> {
        return dao.getTransByWeek(
            month,
            year,
            if (month == 12) 1 else month + 1,
            if (month == 1) 12 else month - 1
        )
    }

    override fun getTransByYear(year: Int): Flow<List<Trans>> {
        return dao.getTransByYear(year)
    }

    override fun getAllTrans(): Flow<List<Trans>> {
        return dao.getAllTrans()
    }

    override fun getTransByMonth(month: Int, year: Int): Flow<List<Trans>> {
        return dao.getTransByMonth(month, year)
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
}