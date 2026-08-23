package com.vexardrive.fleetmanager.presentation.manager.vehicle.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.vexardrive.fleetmanager.data.remote.dto.manager.vehicle.CreateVehicleRequest
import com.vexardrive.fleetmanager.databinding.FragmentEditVehicleBinding
import com.vexardrive.fleetmanager.presentation.manager.vehicle.state.CreateVehicleUiState
import com.vexardrive.fleetmanager.presentation.manager.vehicle.viewmodel.VehicleViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CreateVehicleFragment : Fragment() {

    private var _binding: FragmentEditVehicleBinding? = null
    private val binding get() = _binding!!

    private val viewModel: VehicleViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

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
        binding.tvTitle.text = "Create Vehicle"
        binding.btnUpdateVehicle.text = "Create Vehicle"

        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.btnUpdateVehicle.setOnClickListener {
            createVehicle()
        }

        observeCreateVehicle()
    }

    private fun createVehicle() {

        val registration = binding.etRegistration.text.toString().trim()
        val vehicleType = binding.actVehicleType.text.toString().trim()
        val make = binding.etMake.text.toString().trim()
        val model = binding.etModel.text.toString().trim()
        val year = binding.etYear.text.toString().trim()
        val fuelType = binding.actFuelType.text.toString().trim()
        val mileage = binding.etMileage.text.toString().trim()
        val insuranceExpiry = binding.etInsuranceExpiry.text.toString().trim()
        val registrationExpiry =
            binding.etRegistrationExpiry.text.toString().trim()

        when {
            registration.isEmpty() -> {
                binding.etRegistration.error = "Registration number is required"
                binding.etRegistration.requestFocus()
                return
            }

            vehicleType.isEmpty() -> {
                binding.actVehicleType.error = "Vehicle type is required"
                binding.actVehicleType.requestFocus()
                return
            }

            make.isEmpty() -> {
                binding.etMake.error = "Make is required"
                binding.etMake.requestFocus()
                return
            }

            model.isEmpty() -> {
                binding.etModel.error = "Model is required"
                binding.etModel.requestFocus()
                return
            }

            year.isEmpty() -> {
                binding.etYear.error = "Year is required"
                binding.etYear.requestFocus()
                return
            }

            year.toIntOrNull() == null -> {
                binding.etYear.error = "Enter a valid year"
                binding.etYear.requestFocus()
                return
            }

            fuelType.isEmpty() -> {
                binding.actFuelType.error = "Fuel type is required"
                binding.actFuelType.requestFocus()
                return
            }

            mileage.isEmpty() -> {
                binding.etMileage.error = "Mileage is required"
                binding.etMileage.requestFocus()
                return
            }

            mileage.toDoubleOrNull() == null -> {
                binding.etMileage.error = "Enter a valid mileage"
                binding.etMileage.requestFocus()
                return
            }

            insuranceExpiry.isEmpty() -> {
                binding.etInsuranceExpiry.error =
                    "Insurance expiry is required"
                binding.etInsuranceExpiry.requestFocus()
                return
            }

            registrationExpiry.isEmpty() -> {
                binding.etRegistrationExpiry.error =
                    "Registration expiry is required"
                binding.etRegistrationExpiry.requestFocus()
                return
            }
        }

        val request = CreateVehicleRequest(
            registrationNumber = registration,
            vehicleType = vehicleType,
            make = make,
            model = model,
            year = year.toInt(),
            fuelType = fuelType,
            currentMileage = mileage.toDouble(),
            insuranceExpiry = insuranceExpiry,
            registrationExpiry = registrationExpiry
        )

        viewModel.createVehicle(request)
    }

    private fun observeCreateVehicle() {

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(
                Lifecycle.State.STARTED
            ) {

                viewModel.createVehicle.collect { state ->

                    when (state) {

                        CreateVehicleUiState.Idle -> Unit

                        CreateVehicleUiState.Loading -> {
                            binding.btnUpdateVehicle.isEnabled = false
                            binding.btnUpdateVehicle.text = "Creating..."
                        }

                        is CreateVehicleUiState.Success -> {

                            binding.btnUpdateVehicle.isEnabled = true

                            Toast.makeText(
                                requireContext(),
                                state.data.message,
                                Toast.LENGTH_SHORT
                            ).show()

                            findNavController().navigateUp()
                        }

                        is CreateVehicleUiState.Error -> {

                            binding.btnUpdateVehicle.isEnabled = true
                            binding.btnUpdateVehicle.text = "Create Vehicle"

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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}