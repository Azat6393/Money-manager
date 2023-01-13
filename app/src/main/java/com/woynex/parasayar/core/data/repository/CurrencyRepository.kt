package com.woynex.parasayar.core.data.repository

import com.woynex.parasayar.core.domain.model.Currency
import kotlinx.coroutines.flow.Flow

interface CurrencyRepository {

    suspend fun insertCurrency(currency: Currency)

    suspend fun updateCurrency(currency: Currency)

    suspend fun deleteCurrency(currency: Currency)

    fun getAllCurrency(): Flow<List<Currency>>

}