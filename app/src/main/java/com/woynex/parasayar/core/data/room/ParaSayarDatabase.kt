package com.woynex.parasayar.core.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.woynex.parasayar.core.domain.model.Currency
import com.woynex.parasayar.feature_accounts.data.room.AccountsDao
import com.woynex.parasayar.feature_accounts.domain.model.AccountDto
import com.woynex.parasayar.feature_accounts.domain.model.AccountGroup
import com.woynex.parasayar.feature_settings.data.room.SettingsDao
import com.woynex.parasayar.feature_trans.data.room.TransDao
import com.woynex.parasayar.feature_settings.domain.model.Category
import com.woynex.parasayar.feature_settings.domain.model.SubCategory
import com.woynex.parasayar.feature_statistics.data.room.StatisticsDao
import com.woynex.parasayar.feature_trans.data.room.BitmapConverters
import com.woynex.parasayar.feature_trans.data.room.BudgetDao
import com.woynex.parasayar.feature_trans.domain.model.CategoryBudget
import com.woynex.parasayar.feature_trans.domain.model.SubcategoryBudget
import com.woynex.parasayar.feature_trans.domain.model.Trans

@Database(
    entities = [
        Trans::class,
        Category::class,
        AccountDto::class,
        AccountGroup::class,
        SubCategory::class,
        Currency::class,
        CategoryBudget::class,
        SubcategoryBudget::class,
    ],
    version = 1,
    exportSchema = true
)
@TypeConverters(BitmapConverters::class)
abstract class ParaSayarDatabase : RoomDatabase() {
    abstract val accountsDao: AccountsDao
    abstract val transDao: TransDao
    abstract val settingsDao: SettingsDao
    abstract val currencyDao: CurrencyDao
    abstract val statisticsDao: StatisticsDao
    abstract val budgetDao: BudgetDao
}