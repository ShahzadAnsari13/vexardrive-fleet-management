package com.vexardrive.fleetmanager.presentation.manager.driver.fragment

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.vexardrive.fleetmanager.R
import com.vexardrive.fleetmanager.data.remote.dto.manager.driver.DriverDetailsDto
import com.vexardrive.fleetmanager.data.remote.dto.manager.driver.UpdateDriverRequest
import com.vexardrive.fleetmanager.databinding.FragmentEditDriverBinding
import com.vexardrive.fleetmanager.presentation.manager.driver.state.DriverDetailsState
import com.vexardrive.fleetmanager.presentation.manager.driver.state.UpdateDriverState
import com.vexardrive.fleetmanager.presentation.manager.driver.viewmodel.DriverViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


@AndroidEntryPoint
class EditDriverFragment : Fragment(R.layout.fragment_edit_driver) {

    private var _binding: FragmentEditDriverBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DriverViewModel by viewModels()
    private val args: EditDriverFragmentArgs by navArgs()

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentEditDriverBinding.bind(view)

        setupClicks()
        observeUpdateState()

        // Existing Driver Details se data lekar fields fill karna
        viewModel.driverDetailsState.value.let { state ->
            if (state is DriverDetailsState.Success) {
                bindDriver(state.driver)
            } else {
                viewModel.getDriverById(args.driverId)
            }
        }

        observeDriverDetails()
    }

    private fun setupClicks() {

        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.etLicenseExpiry.setOnClickListener {
            showLicenseExpiryPicker()
        }

        binding.licenseExpiryInputLayout.setEndIconOnClickListener {
            showLicenseExpiryPicker()
        }

        binding.btnUpdateDriver.setOnClickListener {
            updateDriver()
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
                            binding.btnUpdateDriver.isEnabled = false
                        }

                        is DriverDetailsState.Success -> {
                            binding.btnUpdateDriver.isEnabled = true
                            bindDriver(state.driver)
                        }

                        is DriverDetailsState.Error -> {
                            binding.btnUpdateDriver.isEnabled = true

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

    private fun bindDriver(driver: DriverDetailsDto) {

        binding.etName.setText(driver.name)
        binding.etEmail.setText(driver.email)
        binding.etPhone.setText(driver.phone)
        binding.etLicenseNumber.setText(driver.license_number)
        binding.etLicenseExpiry.setText(driver.license_expiry)
    }

    private fun updateDriver() {

        val name = binding.etName.text?.toString()?.trim().orEmpty()
        val email = binding.etEmail.text?.toString()?.trim().orEmpty()
        val phone = binding.etPhone.text?.toString()?.trim().orEmpty()
        val licenseNumber =
            binding.etLicenseNumber.text?.toString()?.trim().orEmpty()
        val licenseExpiry =
            binding.etLicenseExpiry.text?.toString()?.trim().orEmpty()

        if (name.isEmpty()) {
            binding.nameInputLayout.error = "Name is required"
            return
        }

        if (email.isEmpty()) {
            binding.emailInputLayout.error = "Email is required"
            return
        }

        if (phone.isEmpty()) {
            binding.phoneInputLayout.error = "Phone is required"
            return
        }

        if (licenseNumber.isEmpty()) {
            binding.licenseNumberInputLayout.error =
                "License number is required"
            return
        }

        if (licenseExpiry.isEmpty()) {
            binding.licenseExpiryInputLayout.error =
                "License expiry is required"
            return
        }

        clearErrors()

        val request = UpdateDriverRequest(
            name = name,
            email = email,
            phone = phone,
            licenseNumber = licenseNumber,
            licenseExpiry = licenseExpiry
        )

        viewModel.updateDriver(
            driverId = args.driverId,
            request = request
        )
    }

    private fun clearErrors() {
        binding.nameInputLayout.error = null
        binding.emailInputLayout.error = null
        binding.phoneInputLayout.error = null
        binding.licenseNumberInputLayout.error = null
        binding.licenseExpiryInputLayout.error = null
    }

    private fun showLicenseExpiryPicker() {

        val calendar = Calendar.getInstance()

        DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->

                val selectedDate = Calendar.getInstance().apply {
                    set(year, month, dayOfMonth)
                }

                val formatter =
                    SimpleDateFormat(
                        "yyyy-MM-dd",
                        Locale.getDefault()
                    )

                binding.etLicenseExpiry.setText(
                    formatter.format(selectedDate.time)
                )
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun observeUpdateState() {

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(
                Lifecycle.State.STARTED
            ) {
                viewModel.updateDriverState.collect { state ->

                    when (state) {

                        UpdateDriverState.Idle -> Unit

                        UpdateDriverState.Loading -> {
                            binding.btnUpdateDriver.isEnabled = false
                            binding.btnUpdateDriver.text = "Updating..."
                        }

                        is UpdateDriverState.Success -> {

                            binding.btnUpdateDriver.isEnabled = true
                            binding.btnUpdateDriver.text = "Update Driver"

                            Toast.makeText(
                                requireContext(),
                                state.response.message,
                                Toast.LENGTH_SHORT
                            ).show()

                            findNavController().navigateUp()
                        }

                        is UpdateDriverState.Error -> {

                            binding.btnUpdateDriver.isEnabled = true
                            binding.btnUpdateDriver.text = "Update Driver"

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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}