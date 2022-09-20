package com.woynex.parasayar.feature_settings.di

import com.woynex.parasayar.core.data.room.ParaSayarDatabase
import com.woynex.parasayar.feature_settings.data.repository.SettingsRepositoryImpl
import com.woynex.parasayar.feature_settings.data.room.SettingsDao
import com.woynex.parasayar.feature_settings.domain.repository.SettingsRepository
import com.woynex.parasayar.feature_settings.domain.use_case.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SettingsModule {

    @Provides
    @Singleton
    fun provideSettingsDao(database: ParaSayarDatabase): SettingsDao {
        return database.settingsDao
    }

    @Provides
    @Singleton
    fun provideSettingRepository(dao: SettingsDao): SettingsRepository {
        return SettingsRepositoryImpl(dao)
    }

    @Provides
    @Singleton
    fun provideCategoryUseCases(repo: SettingsRepository): CategoryUseCases {
        return CategoryUseCases(
            insertCategory = InsertCategory(repo),
            updateCategory = UpdateCategory(repo),
            deleteCategory = DeleteCategory(repo),
            getCategories = GetCategories(repo)
        )
    }

}