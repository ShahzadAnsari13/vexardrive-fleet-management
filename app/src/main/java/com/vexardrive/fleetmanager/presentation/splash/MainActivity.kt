package com.vexardrive.fleetmanager.presentation.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.vexardrive.fleetmanager.R
import com.vexardrive.fleetmanager.presentation.auth.AuthActivity
import com.vexardrive.fleetmanager.presentation.driver.DriverActivity
import com.vexardrive.fleetmanager.presentation.manager.ManagerActivity
import com.vexardrive.fleetmanager.presentation.splash.viewmodel.SplashViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val viewModel: SplashViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        observeSession()
    }

    private fun observeSession() {

        viewModel.checkUserSession { role ->

            val intent = when (role) {

                "FLEET_MANAGER" ->
                    Intent(this, ManagerActivity::class.java)

                "DRIVER" ->
                    Intent(this, DriverActivity::class.java)

                else ->
                    Intent(this, AuthActivity::class.java)
            }

            intent.flags =
                Intent.FLAG_ACTIVITY_NEW_TASK or
                        Intent.FLAG_ACTIVITY_CLEAR_TASK

            startActivity(intent)
        }
    }
}