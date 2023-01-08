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

    @Query("SELECT * FROM trans WHERE day=:day AND month=:month AND year=:year")
    fun getTransByDay(day: Int, month: Int, year: Int): Flow<List<Trans>>

    @Query("SELECT * FROM trans WHERE year=:year")
    fun getTransByYear(year: Int): Flow<List<Trans>>

    @Query("SELECT * FROM trans")
    fun getAllTrans(): Flow<List<Trans>>

    @Query("SELECT * FROM trans WHERE month=:month AND year=:year")
    fun getTransByMonth(month: Int, year: Int): Flow<List<Trans>>

    @Query("SELECT * FROM trans WHERE month=:month AND account_name=:accountName AND year=:year")
    fun filterTransByMonthAndAccount(month: Int, accountName: String, year: Int): Flow<List<Trans>>

    @Query("SELECT * FROM trans WHERE month=:month AND type=:type AND year=:year")
    fun filterTransByMonthAndExpensesOrIncome(month: Int, type: String, year: Int): Flow<List<Trans>>

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
}