package com.km.civilian.cpr.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.km.civilian.cpr.R
import com.km.civilian.cpr.util.NotificationBuilder

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViewModel()
        initNavFragment()
        checkSmsPermission()
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
    }

    /**
     * Checks if sms permissions have been granted. Prompt popup to ask for permission if not granted.
     */
    private fun checkSmsPermission() {
        if (
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.RECEIVE_SMS
            ) == PackageManager.PERMISSION_DENIED
        ) {
            registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) {}.launch(Manifest.permission.RECEIVE_SMS)
        }
    }

    /**
     * Initialize the navigation graph with the toolbar and navigation fragment host.
     */
    private fun initNavFragment() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        val navController = navHostFragment.navController

        val appBarConfiguration = AppBarConfiguration(navController.graph)
        findViewById<Toolbar>(R.id.toolbar)
            .setupWithNavController(navController, appBarConfiguration)
    }

    /**
     * when the app is opened remove the notifications.
     */
    override fun onStart() {
        super.onStart()
        NotificationBuilder.cancelAllNotifications(this)
    }

}