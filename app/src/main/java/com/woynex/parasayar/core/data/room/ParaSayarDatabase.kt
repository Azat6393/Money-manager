package com.woynex.parasayar.core.data.room

import androidx.room.Database
import androidx.room.DatabaseConfiguration
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteOpenHelper
import com.woynex.parasayar.core.domain.model.Currency
import com.woynex.parasayar.feature_accounts.data.room.AccountsDao
import com.woynex.parasayar.feature_accounts.domain.model.Account
import com.woynex.parasayar.feature_accounts.domain.model.AccountGroup
import com.woynex.parasayar.feature_settings.data.room.SettingsDao
import com.woynex.parasayar.feature_trans.data.room.TransDao
import com.woynex.parasayar.feature_settings.domain.model.Category
import com.woynex.parasayar.feature_settings.domain.model.SubCategory
import com.woynex.parasayar.feature_trans.data.room.BitmapConverters
import com.woynex.parasayar.feature_trans.domain.model.Trans

@Database(
    entities = [Trans::class, Category::class, Account::class, AccountGroup::class, SubCategory::class, Currency::class],
    version = 1,
    exportSchema = true
)
@TypeConverters(BitmapConverters::class)
abstract class ParaSayarDatabase : RoomDatabase() {
    abstract val accountsDao: AccountsDao
    abstract val transDao: TransDao
    abstract val settingsDao: SettingsDao
    abstract val currencyDao: CurrencyDao
}