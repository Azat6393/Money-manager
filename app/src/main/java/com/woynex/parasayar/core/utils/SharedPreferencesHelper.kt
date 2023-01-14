package com.woynex.parasayar.core.utils

import android.content.SharedPreferences
import androidx.core.content.edit
import com.woynex.parasayar.core.domain.model.Currency

class SharedPreferencesHelper(private val preferences: SharedPreferences) {

    companion object {
        const val DATABASE_NAME = "para_sayar_preferences"
        const val DEFAULT_CURRENCY_CC = "default_currency_cc"
        const val DEFAULT_CURRENCY_SYMBOL = "default_currency_symbol"
        const val DEFAULT_CURRENCY_NAME = "default_currency_name"
    }

    private var default_currency_cc: String?
        get() = preferences.getString(DEFAULT_CURRENCY_CC, "USD")
        set(value) {
            preferences.edit { putString(DEFAULT_CURRENCY_CC, value) }
        }

    private var default_currency_symbol: String?
        get() = preferences.getString(DEFAULT_CURRENCY_SYMBOL, "\$")
        set(value) {
            preferences.edit { putString(DEFAULT_CURRENCY_SYMBOL, value) }
        }

    private var default_currency_name: String?
        get() = preferences.getString(DEFAULT_CURRENCY_NAME, "United States dollar")
        set(value) {
            preferences.edit { putString(DEFAULT_CURRENCY_NAME, value) }
        }


    fun getDefaultCurrency(): Currency {
        return Currency(
            cc = default_currency_cc!!,
            name = default_currency_name!!,
            symbol = default_currency_symbol!!
        )
    }

    fun setDefaultCurrency(currency: Currency) {
        default_currency_cc = currency.cc
        default_currency_symbol = currency.symbol
        default_currency_name = currency.name
    }
}