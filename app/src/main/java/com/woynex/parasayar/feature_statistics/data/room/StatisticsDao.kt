package com.woynex.parasayar.feature_statistics.data.room

import androidx.room.Dao
import androidx.room.Query
import com.woynex.parasayar.feature_trans.domain.model.Trans
import kotlinx.coroutines.flow.Flow

@Dao
interface StatisticsDao {

    @Query("SELECT * FROM trans WHERE month=:month AND year=:year AND type=:type")
    fun getCategoryWithTransMonthly(
        month: Int,
        year: Int,
        type: String
    ): Flow<List<Trans>>

    @Query("SELECT * FROM trans WHERE year=:year AND type=:type")
    fun getCategoryWithTransYear(
        year: Int,
        type: String
    ): Flow<List<Trans>>

}