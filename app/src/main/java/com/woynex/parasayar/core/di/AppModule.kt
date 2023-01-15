package com.woynex.parasayar.core.di

import android.app.Application
import android.content.Context
import android.content.Context.MODE_PRIVATE
import androidx.room.Room
import com.woynex.parasayar.core.data.repository.CurrencyRepository
import com.woynex.parasayar.core.data.room.CurrencyDao
import com.woynex.parasayar.core.data.room.ParaSayarDatabase
import com.woynex.parasayar.core.domain.repository.CurrencyRepositoryImpl
import com.woynex.parasayar.core.utils.RoomConstants.PARA_SAYAR_DEFAULT_DB
import com.woynex.parasayar.core.utils.SharedPreferencesHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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
    ).createFromAsset(PARA_SAYAR_DEFAULT_DB).build()

    @Provides
    @Singleton
    fun provideCurrencyDao(database: ParaSayarDatabase): CurrencyDao {
        return database.currencyDao
    }

    @Provides
    @Singleton
    fun provideCurrencyRepo(dao: CurrencyDao): CurrencyRepository {
        return CurrencyRepositoryImpl(dao)
    }

    @Provides
    @Singleton
    fun provideSharedPreferencesHelper(@ApplicationContext context: Context): SharedPreferencesHelper {
        return SharedPreferencesHelper(
            context.getSharedPreferences(
                SharedPreferencesHelper.DATABASE_NAME,
                MODE_PRIVATE
            )
        )
    }
}
