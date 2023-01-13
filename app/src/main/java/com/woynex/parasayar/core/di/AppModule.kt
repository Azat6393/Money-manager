package com.woynex.parasayar.core.di

import android.app.Application
import androidx.room.Room
import com.woynex.parasayar.core.data.repository.CurrencyRepository
import com.woynex.parasayar.core.data.room.ParaSayarDatabase
import com.woynex.parasayar.core.domain.repository.CurrencyRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.util.*
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideParaSayarDatabase(
        app: Application
    ) = Room.databaseBuilder(
        app,
        ParaSayarDatabase::class.java,
        "para_sayar_database"
    ).createFromAsset("database/para_sayar.db").build()

    @Provides
    @Singleton
    fun provideCurrencyRepo(database: ParaSayarDatabase): CurrencyRepository{
        return CurrencyRepositoryImpl(database.currencyDao)
    }
}
