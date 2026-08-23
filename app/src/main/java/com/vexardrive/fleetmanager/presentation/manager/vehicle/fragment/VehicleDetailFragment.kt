package com.vexardrive.fleetmanager.presentation.manager.vehicle.fragment

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
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.vexardrive.fleetmanager.R
import com.vexardrive.fleetmanager.data.remote.dto.manager.vehicle.VehicleDetailDto
import com.vexardrive.fleetmanager.databinding.FragmentVehicleDetailBinding
import com.vexardrive.fleetmanager.presentation.manager.vehicle.state.UpdateVehicleStatusUiState
import com.vexardrive.fleetmanager.presentation.manager.vehicle.state.VehicleDetailUiState
import com.vexardrive.fleetmanager.presentation.manager.vehicle.viewmodel.VehicleViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class VehicleDetailFragment : Fragment() {
    private var _binding: FragmentVehicleDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: VehicleViewModel by viewModels()

    private val args: VehicleDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentVehicleDetailBinding.inflate(
            inflater,
            container,
            false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.btnEditVehicle.setOnClickListener {

            val action =
                VehicleDetailFragmentDirections
                    .actionVehicleDetailFragmentToEditVehicleFragment(
                        args.vehicleId
                    )

            findNavController().navigate(action)
        }
        binding.btnDeactivateVehicle.setOnClickListener {

            val currentStatus =
                viewModel.vehicleDetail.value.let { state ->

                    if (state is VehicleDetailUiState.Success) {
                        state.data.data.status
                    } else {
                        return@setOnClickListener
                    }
                }

            if (currentStatus == "INACTIVE") {

                viewModel.updateVehicleStatus(
                    args.vehicleId,
                    "AVAILABLE"
                )

            } else if(currentStatus == "AVAILABLE"){

                viewModel.updateVehicleStatus(
                    args.vehicleId,
                    "INACTIVE"
                )
            }
            else{
                Toast.makeText(requireContext(), "Vehicle cannot be deactivated while active in a trip or maintenance.", Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.getVehicleById(args.vehicleId)

        observeVehicle()
        observeUpdateVehicleStatus()

    }

    private fun observeVehicle() {

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(
                Lifecycle.State.STARTED
            ) {

                viewModel.vehicleDetail.collect { state ->

                    when (state) {

                        VehicleDetailUiState.Idle -> Unit

                        VehicleDetailUiState.Loading -> {
                            binding.vehicleDetailShimmer.visibility = View.VISIBLE
                            binding.vehicleDetailShimmer.startShimmer()
                            binding.llAction.visibility = View.GONE
                            binding.nsLayout.visibility = View.GONE
                        }

                        is VehicleDetailUiState.Success -> {
                            binding.vehicleDetailShimmer.visibility = View.GONE
                            binding.vehicleDetailShimmer.stopShimmer()
                            binding.llAction.visibility = View.VISIBLE
                            binding.nsLayout.visibility = View.VISIBLE
                            updateStatusButton(state.data.data.status)
                            bindVehicle(state.data.data)
                        }

                        is VehicleDetailUiState.Error -> {
                            binding.vehicleDetailShimmer.visibility = View.GONE
                            binding.vehicleDetailShimmer.stopShimmer()
                        }
                    }
                }
            }
        }
    }

    private fun bindVehicle(vehicle: VehicleDetailDto) {

        binding.tvRegistrationNumber.text =
            vehicle.registration_number

        binding.tvVehicleName.text =
            "${vehicle.make} ${vehicle.model}"

        binding.tvVehicleType.text =
            vehicle.vehicle_type

        binding.tvStatus.text =
            vehicle.status.replace("_", " ")

        binding.rowRegistration.tvLabel.text =
            "Registration Number"
        binding.rowRegistration.tvValue.text =
            vehicle.registration_number

        binding.rowVehicleType.tvLabel.text =
            "Vehicle Type"
        binding.rowVehicleType.tvValue.text =
            vehicle.vehicle_type

        binding.rowMake.tvLabel.text =
            "Make"
        binding.rowMake.tvValue.text =
            vehicle.make

        binding.rowModel.tvLabel.text =
            "Model"
        binding.rowModel.tvValue.text =
            vehicle.model

        binding.rowYear.tvLabel.text =
            "Year"
        binding.rowYear.tvValue.text =
            vehicle.year.toString()

        binding.rowFuelType.tvLabel.text =
            "Fuel Type"
        binding.rowFuelType.tvValue.text =
            vehicle.fuel_type

        binding.rowMileage.tvLabel.text =
            "Current Mileage"
        binding.rowMileage.tvValue.text =
            "${vehicle.current_mileage} km"

        binding.rowInsurance.tvLabel.text =
            "Insurance Expiry"
        binding.rowInsurance.tvValue.text =
            vehicle.insurance_expiry

        binding.rowRegistrationExpiry.tvLabel.text =
            "Registration Expiry"
        binding.rowRegistrationExpiry.tvValue.text =
            vehicle.registration_expiry
    }
    private fun showDeactivateDialog() {

        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Deactivate Vehicle?")
            .setMessage(
                "This vehicle will be marked as inactive. " +
                        "You can change its status later."
            )
            .setNegativeButton("Cancel", null)
            .setPositiveButton("Deactivate") { _, _ ->

                viewModel.updateVehicleStatus(
                    args.vehicleId,
                    "INACTIVE"
                )
            }
            .show()
    }

    private fun observeUpdateVehicleStatus() {

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(
                Lifecycle.State.STARTED
            ) {

                viewModel.updateVehicleStatus.collect { state ->

                    when (state) {

                        UpdateVehicleStatusUiState.Idle -> Unit

                        UpdateVehicleStatusUiState.Loading -> {
                            binding.btnDeactivateVehicle.isEnabled = false
                            binding.btnDeactivateVehicle.text = "Updating..."
                        }

                        is UpdateVehicleStatusUiState.Success -> {

                            binding.btnDeactivateVehicle.isEnabled = true
                            binding.btnDeactivateVehicle.text = "Deactivate"

                            Toast.makeText(
                                requireContext(),
                                state.data.message,
                                Toast.LENGTH_SHORT
                            ).show()

                            findNavController().navigateUp()
                        }

                        is UpdateVehicleStatusUiState.Error -> {

                            binding.btnDeactivateVehicle.isEnabled = true
                            binding.btnDeactivateVehicle.text = "Deactivate"

                            Toast.makeText(
                                requireContext(),
                                state.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }
    }
    private fun updateStatusButton(status: String) {

        if (status == "INACTIVE") {

            // Activate
            binding.btnDeactivateVehicle.text = "Activate"

            binding.btnDeactivateVehicle.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.vexar_teal
                )
            )

            binding.btnDeactivateVehicle.backgroundTintList =
                ColorStateList.valueOf(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.activate_background
                    )
                )

        } else if(status == "AVAILABLE") {

            // Deactivate
            binding.btnDeactivateVehicle.text = "Deactivate"

            binding.btnDeactivateVehicle.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.red
                )
            )

            binding.btnDeactivateVehicle.backgroundTintList =
                ColorStateList.valueOf(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.deactivate_background
                    )
                )
        }else{
            binding.btnDeactivateVehicle.isEnabled = false
            binding.btnDeactivateVehicle.text = "Unavailable"
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}