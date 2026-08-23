package com.vexardrive.fleetmanager.presentation.manager.driver.fragment

import android.content.res.ColorStateList
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.vexardrive.fleetmanager.R
import com.vexardrive.fleetmanager.data.remote.dto.manager.driver.DriverDetailsDto
import com.vexardrive.fleetmanager.databinding.FragmentDriverDetailsBinding
import com.vexardrive.fleetmanager.presentation.manager.driver.state.DriverDetailsState
import com.vexardrive.fleetmanager.presentation.manager.driver.state.UpdateDriverStatusState
import com.vexardrive.fleetmanager.presentation.manager.driver.viewmodel.DriverViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class DriverDetailsFragment : Fragment(R.layout.fragment_driver_details) {

    private var _binding: FragmentDriverDetailsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DriverViewModel by viewModels()

    private val args: DriverDetailsFragmentArgs by navArgs()

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentDriverDetailsBinding.bind(view)

        setupClicks()
        observeDriverDetails()

        observeUpdateDriverStatusState()
        if (args.driverId.isNotEmpty()) {
            viewModel.getDriverById(args.driverId)
        }
    }

    private fun setupClicks() {

        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.btnEditDriver.setOnClickListener {

            val action =
                DriverDetailsFragmentDirections
                    .actionDriverDetailsFragmentToEditDriverFragment(
                        driverId = args.driverId
                    )

            findNavController().navigate(action)
        }

        binding.btnDeactivateDriver.setOnClickListener {

            val currentStatus =
                viewModel.driverDetailsState.value.let { state ->

                    if (state is DriverDetailsState.Success) {
                        state.driver.status
                    } else {
                        return@setOnClickListener
                    }
                }

            when (currentStatus) {

                "ACTIVE" -> {
                    viewModel.updateDriverStatus(
                        args.driverId,
                        "INACTIVE"
                    )
                }

                "INACTIVE" -> {
                    viewModel.updateDriverStatus(
                        args.driverId,
                        "ACTIVE"
                    )
                }

                else -> {
                    Toast.makeText(
                        requireContext(),
                        "Invalid driver status.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun observeDriverDetails() {

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(
                Lifecycle.State.STARTED
            ) {
                viewModel.driverDetailsState.collect { state ->

                    when (state) {

                        DriverDetailsState.Idle -> Unit

                        DriverDetailsState.Loading -> {
                            binding.nsLayout.visibility = View.GONE
                            binding.driverDetailsShimmer.root.visibility = View.VISIBLE
                            binding.driverDetailsShimmer.root.startShimmer()


                        }

                        is DriverDetailsState.Success -> {
                            binding.driverDetailsShimmer.root.stopShimmer()
                            binding.driverDetailsShimmer.root.visibility = View.GONE
                            binding.nsLayout.visibility = View.VISIBLE
                            bindDriverDetails(state.driver)
                            updateStatusButton(state.driver.status)

                        }

                        is DriverDetailsState.Error -> {
                            binding.driverDetailsShimmer.root.stopShimmer()
                            binding.driverDetailsShimmer.root.visibility = View.GONE
                            binding.nsLayout.visibility = View.GONE
                            Toast.makeText(
                                requireContext(),
                                state.message,
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }
            }
        }
    }

    private fun bindDriverDetails(
        driver: DriverDetailsDto
    ) {
        binding.tvDriverName.text = driver.name
        binding.tvDriverId.text = "Driver ID: ${driver.id}"

        binding.tvDriverEmail.text =
            "Email: ${driver.email}"

        binding.tvDriverPhone.text =
            "Phone: ${driver.phone}"

        binding.tvLicenseNumber.text =
            "License Number: ${driver.license_number}"

        binding.tvLicenseExpiry.text =
            "License Expiry: ${driver.license_expiry}"

        binding.tvCreatedAt.text =
            "Created At: ${driver.created_at}"

        binding.tvUpdatedAt.text =
            "Updated At: ${driver.updated_at}"

        binding.tvDriverStatus.text =
            driver.status
    }

    private fun updateStatusButton(status: String) {

        when (status) {

            "ACTIVE" -> {

                binding.btnDeactivateDriver.isEnabled = true
                binding.btnDeactivateDriver.text = "Deactivate"

                binding.btnDeactivateDriver.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.red
                    )
                )

                binding.btnDeactivateDriver.backgroundTintList =
                    ColorStateList.valueOf(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.deactivate_background
                        )
                    )
            }

            "INACTIVE" -> {

                binding.btnDeactivateDriver.isEnabled = true
                binding.btnDeactivateDriver.text = "Activate"

                binding.btnDeactivateDriver.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.vexar_teal
                    )
                )

                binding.btnDeactivateDriver.backgroundTintList =
                    ColorStateList.valueOf(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.activate_background
                        )
                    )
            }

            else -> {

                binding.btnDeactivateDriver.isEnabled = false
                binding.btnDeactivateDriver.text = "Unavailable"
            }
        }
    }

    private fun observeUpdateDriverStatusState() {

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(
                Lifecycle.State.STARTED
            ) {
                viewModel.updateDriverStatusState.collect { state ->

                    when (state) {

                        UpdateDriverStatusState.Idle -> Unit

                        UpdateDriverStatusState.Loading -> {
                            binding.btnDeactivateDriver.isEnabled = false
                        }

                        is UpdateDriverStatusState.Success -> {

                            binding.btnDeactivateDriver.isEnabled = true

                            Toast.makeText(
                                requireContext(),
                                state.response.message,
                                Toast.LENGTH_SHORT
                            ).show()

                            // Fresh status fetch
                            viewModel.getDriverById(args.driverId)
                        }

                        is UpdateDriverStatusState.Error -> {

                            binding.btnDeactivateDriver.isEnabled = true

                            Toast.makeText(
                                requireContext(),
                                state.message,
                                Toast.LENGTH_LONG
                            ).show()

                            // Current status ke according button restore
                            viewModel.driverDetailsState.value.let { detailsState ->
                                if (detailsState is DriverDetailsState.Success) {
                                    updateStatusButton(detailsState.driver.status)
                                }
                            }
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