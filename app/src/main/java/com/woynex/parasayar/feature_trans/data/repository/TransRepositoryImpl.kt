package com.woynex.parasayar.feature_trans.data.repository

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
    }

    override suspend fun deleteTrans(trans: Trans) {
        dao.deleteTrans(trans)
    }

    override suspend fun getTransById(id: Int): Trans {
        return dao.getTransById(id)
    }

    override fun getTransByDay(day: Int): Flow<List<Trans>> {
        return dao.getTransByDay(day)
    }

    override fun getTransByYear(year: Int): Flow<List<Trans>> {
        return dao.getTransByYear(year)
    }

    override fun getAllTrans(): Flow<List<Trans>> {
        return dao.getAllTrans()
    }

    override fun getTransByMonth(month: Int): Flow<List<Trans>> {
        return dao.getTransByMonth(month)
    }
}