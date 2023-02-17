package com.woynex.parasayar

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.woynex.parasayar.core.utils.SharedPreferencesHelper
import com.woynex.parasayar.databinding.ActivityMainBinding
import com.woynex.parasayar.feature_trans.presentation.trans.TransFragmentDirections
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController

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

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostController =
            supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        navController = navHostController.findNavController()
        binding.bottomNavigation.setupWithNavController(navController)

        binding.bottomNavigation.setupWithNavController(navController)
        binding.bottomNavigation.background = null
        binding.bottomNavigation.menu.getItem(2).isEnabled = false

        binding.addTransFab.setOnClickListener {
            navController.navigate(R.id.transDetailsFragment)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}