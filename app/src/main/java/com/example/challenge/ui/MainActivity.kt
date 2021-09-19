package com.example.challenge.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.challenge.R
import com.example.challenge.databinding.MainActivityBinding
import com.google.android.libraries.places.api.Places

/**
 * Main Activity & Only Activity
 * @property binding MainActivityBinding
 * @property navController NavController
 * @property appBarConfiguration AppBarConfiguration
 */
class MainActivity : AppCompatActivity(){
    private lateinit var binding : MainActivityBinding
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.main_activity)
        binding.lifecycleOwner = this
        setSupportActionBar(binding.mainToolbar)
        Places.initialize(this, resources.getString(R.string.places_api_key)) // setup places remember to add places api key to strings.xml
        setUpViews()
    }

    /**
     * Setup Nav component with Actionbar & BottomNavView
     */
    private fun setUpViews(){
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        binding.bottomNavView.setupWithNavController(navController)
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.blogFragment,
                R.id.mapFragment
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    override fun onSupportNavigateUp(): Boolean {
        super.onSupportNavigateUp()
        onBackPressed()
        return navController.navigateUp(appBarConfiguration)
    }

}