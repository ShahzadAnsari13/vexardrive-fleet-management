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
import androidx.navigation.fragment.navArgs
import com.vexardrive.fleetmanager.R
import com.vexardrive.fleetmanager.data.remote.dto.manager.vehicle.UpdateVehicleRequest
import com.vexardrive.fleetmanager.data.remote.dto.manager.vehicle.VehicleDetailDto
import com.vexardrive.fleetmanager.databinding.FragmentEditVehicleBinding
import com.vexardrive.fleetmanager.presentation.manager.vehicle.state.UpdateVehicleUiState
import com.vexardrive.fleetmanager.presentation.manager.vehicle.state.VehicleDetailUiState
import com.vexardrive.fleetmanager.presentation.manager.vehicle.viewmodel.VehicleViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
@AndroidEntryPoint
class EditVehicleFragment : Fragment() {
    private var _binding: FragmentEditVehicleBinding? = null
    private val binding get() = _binding!!

    private val viewModel: VehicleViewModel by viewModels()
    private val args: EditVehicleFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditVehicleBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }


        viewModel.getVehicleById(args.vehicleId)

        observeVehicle()
        observeUpdateVehicle()
        binding.btnUpdateVehicle.setOnClickListener {
            updateVehicle()
        }
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
                            binding.btnUpdateVehicle.isEnabled = false
                        }

                        is VehicleDetailUiState.Success -> {
                            binding.btnUpdateVehicle.isEnabled = true
                            bindVehicle(state.data.data)
                        }

                        is VehicleDetailUiState.Error -> {
                            binding.btnUpdateVehicle.isEnabled = true
                        }
                    }
                }
            }
        }
    }

    private fun bindVehicle(vehicle: VehicleDetailDto) {

        binding.etRegistration.setText(vehicle.registration_number)
        binding.etMake.setText(vehicle.make)
        binding.etModel.setText(vehicle.model)
        binding.etYear.setText(vehicle.year.toString())
        binding.etMileage.setText(vehicle.current_mileage.toString())
        binding.etInsuranceExpiry.setText(vehicle.insurance_expiry)
        binding.etRegistrationExpiry.setText(vehicle.registration_expiry)

        binding.actVehicleType.setText(vehicle.vehicle_type)
        binding.actFuelType.setText(vehicle.fuel_type)
    }

    private fun updateVehicle() {

        val request = UpdateVehicleRequest(
            registrationNumber =
                binding.etRegistration.text.toString().trim(),

            vehicleType =
                binding.actVehicleType.text.toString().trim(),

            make =
                binding.etMake.text.toString().trim(),

            model =
                binding.etModel.text.toString().trim(),

            year =
                binding.etYear.text.toString().toIntOrNull() ?: 0,

            fuelType =
                binding.actFuelType.text.toString().trim(),

            currentMileage =
                binding.etMileage.text.toString().toIntOrNull() ?: 0,

            insuranceExpiry =
                binding.etInsuranceExpiry.text.toString().trim(),

            registrationExpiry =
                binding.etRegistrationExpiry.text.toString().trim()
        )

        viewModel.updateVehicle(
            args.vehicleId,
            request
        )
    }
    private fun observeUpdateVehicle() {

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(
                Lifecycle.State.STARTED
            ) {

                viewModel.updateVehicle.collect { state ->

                    when (state) {

                        UpdateVehicleUiState.Idle -> Unit

                        UpdateVehicleUiState.Loading -> {
                            binding.btnUpdateVehicle.isEnabled = false
                            binding.btnUpdateVehicle.text = "Updating..."
                        }

                        is UpdateVehicleUiState.Success -> {
                            binding.btnUpdateVehicle.isEnabled = true
                            binding.btnUpdateVehicle.text = "Update Vehicle"

                            findNavController().navigateUp()
                        }

                        is UpdateVehicleUiState.Error -> {
                            binding.btnUpdateVehicle.isEnabled = true
                            binding.btnUpdateVehicle.text = "Update Vehicle"

                            // Error UI final phase mein
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