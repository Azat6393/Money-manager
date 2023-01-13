package com.woynex.parasayar.core.data.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.woynex.parasayar.core.domain.model.Currency
import kotlinx.coroutines.flow.Flow

@Dao
interface CurrencyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCurrency(currency: Currency)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateCurrency(currency: Currency)

    @Delete
    suspend fun deleteCurrency(currency: Currency)

    @Query("SELECT * FROM currency")
    fun getAllCurrency(): Flow<List<Currency>>
}