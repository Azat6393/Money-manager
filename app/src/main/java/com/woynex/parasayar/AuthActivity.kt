package com.woynex.parasayar

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.woynex.parasayar.core.utils.SharedPreferencesHelper
import com.woynex.parasayar.databinding.ActivityAuthBinding
import com.woynex.parasayar.feature_auth.presentation.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AuthActivity : AppCompatActivity() {

    private lateinit var _binding: ActivityAuthBinding
    private lateinit var navController: NavController
    private val viewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        val sharedPreferencesHelper = SharedPreferencesHelper(
            getSharedPreferences(
                SharedPreferencesHelper.DATABASE_NAME,
                Context.MODE_PRIVATE
            )
        )
        if (sharedPreferencesHelper.darkMode)
            setTheme(R.style.Theme_ParaSayar_Dark)
        else
            setTheme(R.style.Theme_ParaSayar_Light)
        super.onCreate(savedInstanceState)

        installSplashScreen().apply {
            setKeepOnScreenCondition {
                viewModel.isLoading.value
            }
        }
        setContentView(R.layout.activity_auth)
        _binding = ActivityAuthBinding.inflate(layoutInflater)

        lifecycleScope.launch {
            if (viewModel.isAuth.value) {
                val intent = Intent(this@AuthActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        val navHostController =
            supportFragmentManager.findFragmentById(R.id.authNavHostFragment) as NavHostFragment
        navController = navHostController.navController
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}