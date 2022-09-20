package com.woynex.parasayar.feature_trans.data.room

import androidx.room.*
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

    @Query("SELECT * FROM trans WHERE day=:day")
    fun getTransByDay(day: Int): Flow<List<Trans>>

    @Query("SELECT * FROM trans WHERE year=:year")
    fun getTransByYear(year: Int): Flow<List<Trans>>

    @Query("SELECT * FROM trans")
    fun getAllTrans(): Flow<List<Trans>>

    @Query("SELECT * FROM trans WHERE month=:month")
    fun getTransByMonth(month: Int): Flow<List<Trans>>

    @Query("SELECT * FROM trans WHERE month=:month AND account_name=:accountName")
    fun filterTransByMonthAndAccount(month: Int, accountName: String): Flow<List<Trans>>

    @Query("SELECT * FROM trans WHERE month=:month AND type=:type")
    fun filterTransByMonthAndExpensesOrIncome(month: Int, type: String): Flow<List<Trans>>

    @Query("SELECT * FROM trans WHERE month=:month AND type=:type AND category=:category")
    fun filterTransByMonthAndExpensesCategory(
        month: Int,
        category: String,
        type: String = "expense"
    ): Flow<List<Trans>>

    @Query("SELECT * FROM trans WHERE month=:month AND type=:type AND category=:category")
    fun filterTransByMonthAndIncomeCategory(
        month: Int,
        category: String,
        type: String = "income"
    ): Flow<List<Trans>>

}