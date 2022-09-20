package com.woynex.parasayar.core.di

import android.app.Application
import androidx.room.Room
import com.woynex.parasayar.core.data.room.ParaSayarDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
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
}
