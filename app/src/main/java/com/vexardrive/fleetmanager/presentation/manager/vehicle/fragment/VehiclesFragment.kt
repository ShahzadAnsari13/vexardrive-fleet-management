package com.vexardrive.fleetmanager.presentation.manager.vehicle.fragment

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
import androidx.recyclerview.widget.LinearLayoutManager
import com.vexardrive.fleetmanager.R
import com.vexardrive.fleetmanager.databinding.FragmentVehiclesBinding
import com.vexardrive.fleetmanager.presentation.manager.vehicle.adapter.VehicleAdapter
import com.vexardrive.fleetmanager.presentation.manager.vehicle.state.VehicleUiState
import com.vexardrive.fleetmanager.presentation.manager.vehicle.viewmodel.VehicleViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class VehiclesFragment : Fragment() {
    private var _binding: FragmentVehiclesBinding? = null
    private val binding get() = _binding!!

    private val viewModel: VehicleViewModel by viewModels()

    private lateinit var vehicleAdapter: VehicleAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentVehiclesBinding.inflate(
            inflater,
            container,
            false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observeVehicles()
        binding.fabAddVehicle.setOnClickListener {

            findNavController().navigate(
                R.id.action_vehiclesFragment_to_createVehicleFragment
            )
        }
        viewModel.getVehicles()
    }

    private fun setupRecyclerView() {

        vehicleAdapter = VehicleAdapter { vehicle ->

            val action =

                VehiclesFragmentDirections
                    .actionVehiclesFragmentToVehicleDetailFragment(
                        vehicle.id
                    )

            findNavController().navigate(action)
        }

        binding.rvVehicles.apply {
            adapter = vehicleAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun observeVehicles() {

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(
                Lifecycle.State.STARTED
            ) {

                viewModel.vehicles.collect { state ->

                    when (state) {

                        VehicleUiState.Idle -> Unit

                        VehicleUiState.Loading -> {
                            binding.rvVehicles.visibility = View.GONE
                            binding.vehicleShimmer.visibility = View.VISIBLE
                            binding.vehicleShimmer.startShimmer()

                        }

                        is VehicleUiState.Success -> {
                            binding.vehicleShimmer.visibility = View.GONE
                            binding.vehicleShimmer.stopShimmer()
                            binding.rvVehicles.visibility = View.VISIBLE

                            vehicleAdapter.submitList(
                                state.data.data
                            )

                            binding.tvVehicleCount.text =
                                "${state.data.data.size} Vehicles"
                        }

                        VehicleUiState.Empty -> {
                            binding.vehicleShimmer.visibility = View.GONE
                            binding.vehicleShimmer.stopShimmer()
                        }

                        is VehicleUiState.Error -> {
                            binding.vehicleShimmer.visibility = View.GONE
                            binding.vehicleShimmer.stopShimmer()
                        }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}