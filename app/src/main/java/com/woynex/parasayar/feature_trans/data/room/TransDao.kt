package com.woynex.parasayar.feature_trans.data.room

import androidx.room.*
import com.woynex.parasayar.core.utils.TransTypes
import com.woynex.parasayar.feature_trans.domain.model.Trans
import kotlinx.coroutines.flow.Flow
import java.time.Year

@Dao
interface TransDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrans(trans: Trans)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateTrans(trans: Trans)

    @Delete
    suspend fun deleteTrans(trans: Trans)

    @Query("SELECT * FROM trans WHERE id=:id")
    suspend fun getTransById(id: Int): Trans

    @Query("SELECT * FROM trans WHERE day=:day AND month=:month AND year=:year AND currency=:currency")
    fun getTransByDay(day: Int, month: Int, year: Int, currency: String): Flow<List<Trans>>

    @Query("SELECT * FROM trans WHERE year=:year AND currency=:currency")
    fun getTransByYear(year: Int, currency: String): Flow<List<Trans>>

    @Query("SELECT * FROM trans WHERE currency=:currency")
    fun getAllTrans(currency: String): Flow<List<Trans>>

    @Query("SELECT * FROM trans WHERE month=:month AND year=:year AND currency=:currency")
    fun getTransByMonth(month: Int, year: Int, currency: String): Flow<List<Trans>>

    @Query("SELECT * FROM trans WHERE month=:month OR month=:nextMonth OR month=:previousMonth AND year=:year AND currency=:currency")
    fun getTransByWeek(
        month: Int,
        year: Int,
        nextMonth: Int,
        previousMonth: Int,
        currency: String
    ): Flow<List<Trans>>

    @Query("SELECT * FROM trans WHERE month=:month AND account_name=:accountName AND year=:year")
    fun filterTransByMonthAndAccount(month: Int, accountName: String, year: Int): Flow<List<Trans>>

    @Query("SELECT * FROM trans WHERE month=:month AND type=:type AND year=:year")
    fun filterTransByMonthAndExpensesOrIncome(
        month: Int,
        type: String,
        year: Int
    ): Flow<List<Trans>>

    @Query("SELECT * FROM trans WHERE month=:month AND type=:type AND category=:category AND year=:year")
    fun filterTransByMonthAndExpensesCategory(
        month: Int,
        category: String,
        type: String = TransTypes.EXPENSE,
        year: Int
    ): Flow<List<Trans>>

    @Query("SELECT * FROM trans WHERE month=:month AND type=:type AND category=:category AND year=:year")
    fun filterTransByMonthAndIncomeCategory(
        month: Int,
        category: String,
        type: String = TransTypes.INCOME,
        year: Int
    ): Flow<List<Trans>>

    @Query("UPDATE trans SET amount=:amount WHERE fee_trans_id=:transId")
    suspend fun updateFeeAmount(amount: Double, transId: String)

    @Query("UPDATE trans SET fee_amount=:amount WHERE trans_id=:transId")
    suspend fun updateFeeAmountFromTrans(amount: Double, transId: String)

    @Query("DELETE FROM trans WHERE fee_trans_id=:transId")
    suspend fun deleteFeeTrans(transId: String)

    @Query("SELECT * FROM trans WHERE category_id=:category_id AND currency=:currency AND month=:month AND year=:year")
    fun getTransByCategory(
        category_id: Int,
        currency: String,
        month: Int,
        year: Int
    ): Flow<List<Trans>>

    @Query("SELECT * FROM trans WHERE subcategory_id=:subcategory_id AND currency=:currency AND month=:month AND year=:year")
    fun getTransBySubcategory(
        subcategory_id: Int,
        currency: String,
        month: Int,
        year: Int
    ): Flow<List<Trans>>

    @Query("SELECT * FROM trans WHERE category_id=:category_id AND currency=:currency AND year=:year")
    fun getTransByCategoryYearly(
        category_id: Int,
        currency: String,
        year: Int
    ): Flow<List<Trans>>

    @Query("SELECT * FROM trans WHERE subcategory_id=:subcategory_id AND currency=:currency AND year=:year")
    fun getTransBySubcategoryYearly(
        subcategory_id: Int,
        currency: String,
        year: Int
    ): Flow<List<Trans>>
}