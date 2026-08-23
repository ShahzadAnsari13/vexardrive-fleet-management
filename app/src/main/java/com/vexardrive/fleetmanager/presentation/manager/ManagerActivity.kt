package com.vexardrive.fleetmanager.presentation.manager

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.vexardrive.fleetmanager.R
import com.vexardrive.fleetmanager.databinding.ActivityManagerBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ManagerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityManagerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityManagerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navController =
            supportFragmentManager
                .findFragmentById(R.id.managerNavHost)
                ?.findNavController()
                ?: return

        binding.managerBottomNavigation.setupWithNavController(navController)
        navController.addOnDestinationChangedListener { _, destination, _ ->

            when (destination.id) {

                R.id.dashboardFragment,
                R.id.vehiclesFragment,
                R.id.driversFragment,
                R.id.tripsFragment,
                R.id.moreFragment -> {
                    binding.managerBottomNavigation.visibility = View.VISIBLE
                }
                else -> {
                    binding.managerBottomNavigation.visibility = View.GONE
                }
            }
        }
    }
}