package com.woynex.parasayar.core.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.woynex.parasayar.feature_accounts.data.room.AccountsDao
import com.woynex.parasayar.feature_accounts.domain.model.Account
import com.woynex.parasayar.feature_accounts.domain.model.AccountGroup
import com.woynex.parasayar.feature_settings.data.room.SettingsDao
import com.woynex.parasayar.feature_trans.data.room.TransDao
import com.woynex.parasayar.feature_settings.domain.model.Category
import com.woynex.parasayar.feature_trans.domain.model.Trans

@Database(
    entities = [Trans::class, Category::class, Account::class, AccountGroup::class],
    version = 1
)
abstract class ParaSayarDatabase : RoomDatabase() {
    abstract val accountsDao: AccountsDao
    abstract val transDao: TransDao
    abstract val settingsDao: SettingsDao
}