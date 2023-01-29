package com.woynex.parasayar.core.utils

import android.content.SharedPreferences
import androidx.core.content.edit
import com.woynex.parasayar.core.domain.model.Currency
import com.woynex.parasayar.core.domain.model.User

class SharedPreferencesHelper(private val preferences: SharedPreferences) {

    companion object {
        const val DATABASE_NAME = "para_sayar_preferences"

        // Currency
        const val DEFAULT_CURRENCY_CC = "default_currency_cc"
        const val DEFAULT_CURRENCY_SYMBOL = "default_currency_symbol"
        const val DEFAULT_CURRENCY_NAME = "default_currency_name"

        // User
        const val USER_ID = "user_id"
        const val USER_FIRST_NAME = "user_first_name"
        const val USER_LAST_NAME = "user_last_name"
        const val USER_PHONE_NAME = "user_phone_name"
        const val USER_EMAIL = "user_email"

        // Dark mode
        const val PREFERENCE_DARK_MODE = "dark_mode"
    }

    var darkMode: Boolean
        get() = preferences.getBoolean(PREFERENCE_DARK_MODE, false)
        set(value) {
            preferences.edit { putBoolean(PREFERENCE_DARK_MODE, value) }
        }

    private var defaultCurrencyCc: String?
        get() = preferences.getString(DEFAULT_CURRENCY_CC, "USD")
        set(value) {
            preferences.edit { putString(DEFAULT_CURRENCY_CC, value) }
        }

    private var defaultCurrencySymbol: String?
        get() = preferences.getString(DEFAULT_CURRENCY_SYMBOL, "\$")
        set(value) {
            preferences.edit { putString(DEFAULT_CURRENCY_SYMBOL, value) }
        }

    private var defaultCurrencyName: String?
        get() = preferences.getString(DEFAULT_CURRENCY_NAME, "United States dollar")
        set(value) {
            preferences.edit { putString(DEFAULT_CURRENCY_NAME, value) }
        }

    fun getUser(): User {
        return User(
            id = preferences.getString(USER_ID, ""),
            first_name = preferences.getString(USER_FIRST_NAME, ""),
            last_name = preferences.getString(USER_LAST_NAME, ""),
            phone_number = preferences.getString(USER_PHONE_NAME, ""),
            email = preferences.getString(USER_EMAIL, "")
        )
    }

    fun setUser(user: User) {
        preferences.edit { putString(USER_ID, user.id) }
        preferences.edit { putString(USER_FIRST_NAME, user.first_name) }
        preferences.edit { putString(USER_LAST_NAME, user.last_name) }
        preferences.edit { putString(USER_PHONE_NAME, user.phone_number) }
        preferences.edit { putString(USER_EMAIL, user.email) }
    }

    fun updateName(firstName: String){
        preferences.edit { putString(USER_FIRST_NAME, firstName) }
    }

    fun updateLastName(lastName: String){
        preferences.edit { putString(USER_LAST_NAME, lastName) }
    }

    fun signOut() {
        preferences.edit { putString(USER_ID, "") }
        preferences.edit { putString(USER_FIRST_NAME, "") }
        preferences.edit { putString(USER_LAST_NAME, "") }
        preferences.edit { putString(USER_PHONE_NAME, "") }
        preferences.edit { putString(USER_EMAIL, "") }
    }

    fun getDefaultCurrency(): Currency {
        return Currency(
            cc = defaultCurrencyCc!!,
            name = defaultCurrencyName!!,
            symbol = defaultCurrencySymbol!!
        )
    }

    fun setDefaultCurrency(currency: Currency) {
        defaultCurrencyCc = currency.cc
        defaultCurrencySymbol = currency.symbol
        defaultCurrencyName = currency.name
    }
}