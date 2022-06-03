package com.mechwv.placeeventmap

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.yandex.mapkit.MapKitFactory
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {


    private var initialized: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initialized = savedInstanceState?.getBoolean("IS_INITIALIZED", false) == true

        initialize(BuildConfig.YANDEX_KEY, this)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController
        NavigationUI.setupWithNavController(findViewById<BottomNavigationView>(R.id.bottomNav), navController)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean("IS_INITIALIZED", initialized)
    }

    fun initialize(apiKey: String, context: Context) {
        if (initialized) {
            return
        }

        MapKitFactory.setApiKey(apiKey)
        MapKitFactory.initialize(context)
        initialized = true
    }
}