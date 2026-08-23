package com.vexardrive.fleetmanager.presentation.manager.dashboard.fragment

import android.icu.util.Calendar
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.vexardrive.fleetmanager.R
import com.vexardrive.fleetmanager.data.remote.dto.manager.dashboard.DashboardResponse
import com.vexardrive.fleetmanager.data.remote.dto.manager.dashboard.RecentIncidentDto
import com.vexardrive.fleetmanager.databinding.FragmentLoginBinding
import com.vexardrive.fleetmanager.databinding.FragmentManagerDashboardBinding
import com.vexardrive.fleetmanager.presentation.manager.dashboard.viewmodel.DashboardState
import com.vexardrive.fleetmanager.presentation.manager.dashboard.viewmodel.ManagerDashboardViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class ManagerDashboardFragment : Fragment() {
    private var _binding: FragmentManagerDashboardBinding? = null
    private val binding
        get() = _binding!!

    private val viewModel: ManagerDashboardViewModel by viewModels()
    private var isInitialLoad = true
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentManagerDashboardBinding.inflate(
            inflater,
            container,
            false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvGreet.text = getGreeting()
        binding.swipeRefresh.setOnRefreshListener {
            isInitialLoad = false
            viewModel.getDashboard()
        }

        observeDashboard()

        binding.currentAssignmentsCard.setOnClickListener{
            findNavController().navigate(R.id.action_dashboardFragment_to_assignmentListFragment)
        }

        viewModel.getDashboard()
    }
    private fun observeDashboard() {

        viewLifecycleOwner.lifecycleScope.launch {

            viewLifecycleOwner.repeatOnLifecycle(
                Lifecycle.State.STARTED
            ) {

                viewModel.state.collect { state ->

                    when (state) {

                        DashboardState.Idle -> Unit

                        DashboardState.Loading -> {
                            if(isInitialLoad){
                                binding.shimmerLayout.dashboardShimmer.startShimmer()
                                binding.shimmerLayout.dashboardShimmer.visibility = View.VISIBLE
                                binding.dashboardScroll.visibility = View.GONE
                                return@collect
                            }else{
                                binding.swipeRefresh.isRefreshing = true
                            }

                        }

                        is DashboardState.Success -> {
                            if(isInitialLoad){
                                binding.shimmerLayout.dashboardShimmer.stopShimmer()
                                binding.shimmerLayout.dashboardShimmer.visibility = View.GONE
                                binding.dashboardScroll.visibility = View.VISIBLE
                            }else{
                                binding.swipeRefresh.isRefreshing = false
                            }

                            bindDashboard(state.data)
                        }

                        is DashboardState.Error -> {
                            if(isInitialLoad){
                                binding.shimmerLayout.dashboardShimmer.stopShimmer()
                                binding.shimmerLayout.dashboardShimmer.visibility = View.GONE
                                binding.dashboardScroll.visibility = View.GONE
                            }else{
                                binding.swipeRefresh.isRefreshing = false
                            }
                        }
                    }
                }
            }
        }
    }


    private fun bindDashboard(
        dashboard: DashboardResponse
    ) {

        binding.apply {

            // Vehicles
            tvTotalVehicles.text =
                dashboard.vehicles.total_vehicles

            tvAvailableVehicles.text =
                dashboard.vehicles.available_vehicles

            tvOnTripVehicles.text =
                dashboard.vehicles.on_trip_vehicles

            tvMaintenanceVehicles.text =
                dashboard.vehicles.in_maintenance_vehicles

            tvInactiveVehicles.text =
                dashboard.vehicles.inactive_vehicles

            // Trips
            tvActiveTrips.text =
                dashboard.trips.active_trips

            // Distance
            tvTodayDistance.text =
                dashboard.todayDistance.today_distance

            // Maintenance
            tvMaintenanceDue.text =
                dashboard.maintenanceDue.maintenance_due

            // Documents
            tvInsuranceExpiry.text =
                "Insurance                 ${dashboard.expiringDocuments.insurance}"

            tvRegistrationExpiry.text =
                "Registration             ${dashboard.expiringDocuments.registration}"

            tvLicenseExpiry.text =
                "License                       ${dashboard.expiringDocuments.license}"

            // Incidents
            bindRecentIncidents(
                dashboard.recentIncidents
            )
        }
    }

    private fun bindRecentIncidents(
        incidents: List<RecentIncidentDto>
    ) {

        if (incidents.isEmpty()) {
            binding.incidentContainer.visibility =
                View.GONE

            binding.tvMoreIncidents.visibility =
                View.GONE
            binding.emptyState.visibility = View.VISIBLE
            return
        }

        val latestIncident = incidents.first()
        binding.emptyState.visibility = View.GONE
        binding.incidentContainer.visibility =
            View.VISIBLE

        binding.tvIncidentVehicle.text =
            latestIncident.registration_number

        binding.tvIncidentDriver.text =
            latestIncident.issue

        binding.tvIncidentSeverity.text =
            latestIncident.severity

        binding.tvIncidentTime.text =
            latestIncident.status

        val remaining = incidents.size - 1

        if (remaining > 0) {
            binding.tvMoreIncidents.visibility =
                View.VISIBLE

            binding.tvMoreIncidents.text =
                "+ $remaining more incidents"
        } else {
            binding.tvMoreIncidents.visibility =
                View.GONE
        }
    }

    private fun getGreeting():String{
        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        return when (hour) {
            in 0..11 -> "Good Morning Manager ☕"
            in 12..16 -> "Good Afternoon Manager 🌤"
            else -> "Good Evening Manager ✨"
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}