package com.woynex.parasayar

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.woynex.parasayar.core.utils.Constants
import com.woynex.parasayar.core.utils.SharedPreferencesHelper
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class ParaSayarApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        val sharedPreferencesHelper = SharedPreferencesHelper(
            getSharedPreferences(
                SharedPreferencesHelper.DATABASE_NAME,
                Context.MODE_PRIVATE
            )
        )
        if (sharedPreferencesHelper.darkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            setTheme(R.style.Theme_ParaSayar_Dark)
        }
        else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            setTheme(R.style.Theme_ParaSayar_Light)
        }

        FirebaseMessaging.getInstance().subscribeToTopic("allDevices")

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("FCM", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result

            // Log and toast
            Log.d("FCM", token)
        })
    }

}