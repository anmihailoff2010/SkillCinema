package com.example.skillcinema.ui

import android.os.Bundle
import android.preference.PreferenceManager
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.example.skillcinema.R
import com.example.skillcinema.databinding.ActivityMainBinding
import com.example.skillcinema.ui.intro.IntroFragment
import dagger.hilt.android.AndroidEntryPoint


const val TAG = "com.example.skillcinema.ui"

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHost =
            supportFragmentManager.findFragmentById(R.id.main_container) as NavHostFragment
        val navController = navHost.navController

        navController.navigate(R.id.mainFragment)

        PreferenceManager.getDefaultSharedPreferences(this).apply {
            if (!getBoolean(IntroFragment.PREFERENCES_NAME, false)) {
                navController.navigate(R.id.introFragment)
            } else {
                navController.navigate(R.id.mainFragment)
            }
        }
    }
}