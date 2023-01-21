package com.woynex.parasayar.feature_statistics.data.room

import androidx.room.Dao
import androidx.room.Query
import com.woynex.parasayar.core.domain.model.Currency
import com.woynex.parasayar.core.utils.TransTypes
import com.woynex.parasayar.feature_trans.domain.model.Trans
import kotlinx.coroutines.flow.Flow

@Dao
interface StatisticsDao {

    @Query("SELECT * FROM trans WHERE month=:month AND year=:year AND type=:type AND currency=:currency")
    fun getCategoryWithTransMonthly(
        month: Int,
        year: Int,
        type: String,
        currency: String
    ): Flow<List<Trans>>

    @Query("SELECT * FROM trans WHERE year=:year AND type=:type AND currency=:currency")
    fun getCategoryWithTransYear(
        year: Int,
        type: String,
        currency: String
    ): Flow<List<Trans>>

    @Query("SELECT * FROM trans WHERE date_in_millis>=:startDate AND date_in_millis<=:endDate  AND currency=:currency AND type=:type")
    fun getCategoryWithTransPeriod(
        startDate: Long,
        endDate: Long,
        currency: String,
        type: String
    ): Flow<List<Trans>>

}