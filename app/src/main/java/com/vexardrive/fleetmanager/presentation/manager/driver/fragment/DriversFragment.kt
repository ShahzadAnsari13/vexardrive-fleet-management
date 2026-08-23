package com.vexardrive.fleetmanager.presentation.manager.driver.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.vexardrive.fleetmanager.R
import com.vexardrive.fleetmanager.databinding.FragmentDriversBinding
import com.vexardrive.fleetmanager.presentation.manager.driver.adapter.DriverAdapter
import com.vexardrive.fleetmanager.presentation.manager.driver.state.DriverListUiState
import com.vexardrive.fleetmanager.presentation.manager.driver.viewmodel.DriverViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class DriversFragment : Fragment(R.layout.fragment_drivers) {

    private var _binding: FragmentDriversBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DriverViewModel by viewModels()

    private lateinit var driverAdapter: DriverAdapter

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentDriversBinding.bind(view)

        setupRecyclerView()
        observeDrivers()

        binding.btnAddDriver.setOnClickListener {
            findNavController().navigate(
                R.id.action_driversFragment_to_createDriverFragment
            )
        }

        viewModel.getDrivers()
    }

    private fun setupRecyclerView() {

        driverAdapter = DriverAdapter(
            onDriverClick = { driver ->
                val action =  DriversFragmentDirections
                    .actionDriversFragmentToDriverDetailsFragment(
                        driverId = driver.id
                    )

                findNavController().navigate(action)
            }
        )

        binding.rvDrivers.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = driverAdapter
        }
    }

    private fun observeDrivers() {

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(
                Lifecycle.State.STARTED
            ) {
                viewModel.uiState.collect { state ->

                    when (state) {

                        DriverListUiState.Idle -> Unit

                        DriverListUiState.Loading -> {
                            binding.rvDrivers.isVisible = false
                            binding.layoutEmpty.isVisible = false
                            binding.driverShimmer.root.isVisible = true
                            binding.driverShimmer.shimmerDriverList.startShimmer()

                        }

                        is DriverListUiState.Success -> {
                            binding.driverShimmer.shimmerDriverList.stopShimmer()
                            binding.driverShimmer.root.isVisible = false
                            binding.rvDrivers.isVisible = true
                            binding.layoutEmpty.isVisible = false

                            driverAdapter.submitList(state.drivers)

                            binding.tvDriverCount.text =
                                state.drivers.size.toString()
                        }

                        DriverListUiState.Empty -> {
                            binding.rvDrivers.isVisible = false
                            binding.layoutEmpty.isVisible = true
                            binding.tvDriverCount.text = "0"
                            binding.driverShimmer.shimmerDriverList.stopShimmer()
                            binding.driverShimmer.root.isVisible = false
                        }

                        is DriverListUiState.Error -> {
                            binding.driverShimmer.shimmerDriverList.stopShimmer()
                            binding.driverShimmer.root.isVisible = false
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