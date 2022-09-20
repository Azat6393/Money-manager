package com.woynex.parasayar.feature_trans.domain.repository

import com.woynex.parasayar.feature_trans.domain.model.Trans
import kotlinx.coroutines.flow.Flow

interface TransRepository {

    suspend fun insertTrans(trans: Trans)

    suspend fun updateTrans(trans: Trans)

    suspend fun deleteTrans(trans: Trans)

    suspend fun getTransById(id: Int): Trans

    fun getTransByDay(day: Int): Flow<List<Trans>>

    fun getTransByYear(year: Int): Flow<List<Trans>>

    fun getAllTrans(): Flow<List<Trans>>

    fun getTransByMonth(month: Int): Flow<List<Trans>>

}