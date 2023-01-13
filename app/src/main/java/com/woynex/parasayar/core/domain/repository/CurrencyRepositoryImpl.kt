package com.woynex.parasayar.core.domain.repository

import com.woynex.parasayar.core.data.repository.CurrencyRepository
import com.woynex.parasayar.core.data.room.CurrencyDao
import com.woynex.parasayar.core.domain.model.Currency
import kotlinx.coroutines.flow.Flow

class CurrencyRepositoryImpl(private val dao: CurrencyDao): CurrencyRepository {

    override suspend fun insertCurrency(currency: Currency) {
        dao.insertCurrency(currency)
    }

    override suspend fun updateCurrency(currency: Currency) {
        dao.updateCurrency(currency)
    }

    override suspend fun deleteCurrency(currency: Currency) {
        dao.deleteCurrency(currency)
    }

    override fun getAllCurrency(): Flow<List<Currency>> {
        return dao.getAllCurrency()
    }
}