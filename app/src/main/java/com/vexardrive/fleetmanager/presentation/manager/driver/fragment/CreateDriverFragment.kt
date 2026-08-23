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
import com.vexardrive.fleetmanager.R
import com.vexardrive.fleetmanager.data.remote.dto.manager.driver.CreateDriverRequest
import com.vexardrive.fleetmanager.databinding.FragmentCreateDriverBinding
import com.vexardrive.fleetmanager.databinding.FragmentEditVehicleBinding
import com.vexardrive.fleetmanager.presentation.manager.driver.state.CreateDriverState
import com.vexardrive.fleetmanager.presentation.manager.driver.viewmodel.DriverViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@AndroidEntryPoint
class CreateDriverFragment : Fragment() {
    private var _binding: FragmentCreateDriverBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DriverViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCreateDriverBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupClickListeners()
        observeCreateDriverState()
    }
    private fun setupClickListeners() {

        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.etLicenseExpiry.setOnClickListener {
            showLicenseExpiryPicker()
        }

        binding.licenseExpiryInputLayout.setEndIconOnClickListener {
            showLicenseExpiryPicker()
        }

        binding.btnCreateDriver.setOnClickListener {
            createDriver()
        }
    }

    private fun createDriver() {

        val name = binding.etName.text?.toString()?.trim().orEmpty()
        val email = binding.etEmail.text?.toString()?.trim().orEmpty()
        val phone = binding.etPhone.text?.toString()?.trim().orEmpty()
        val password = binding.etPassword.text?.toString()?.trim().orEmpty()
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
            binding.phoneInputLayout.error = "Phone number is required"
            return
        }

        if (password.isEmpty()) {
            binding.passwordInputLayout.error = "Password is required"
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

        val request = CreateDriverRequest(
            name = name,
            email = email,
            phone = phone,
            password = password,
            licenseNumber = licenseNumber,
            licenseExpiry = licenseExpiry
        )

        viewModel.createDriver(request)
    }

    private fun clearErrors() {
        binding.nameInputLayout.error = null
        binding.emailInputLayout.error = null
        binding.phoneInputLayout.error = null
        binding.passwordInputLayout.error = null
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
                    SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

                binding.etLicenseExpiry.setText(
                    formatter.format(selectedDate.time)
                )

            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun observeCreateDriverState() {

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(
                Lifecycle.State.STARTED
            ) {
                viewModel.createDriverState.collect { state ->

                    when (state) {

                        CreateDriverState.Idle -> Unit

                        CreateDriverState.Loading -> {
                            binding.btnCreateDriver.isEnabled = false
                            binding.btnCreateDriver.text = "Creating..."
                        }

                        is CreateDriverState.Success -> {

                            binding.btnCreateDriver.isEnabled = true
                            binding.btnCreateDriver.text =
                                "Create Driver"

                            Toast.makeText(
                                requireContext(),
                                state.response.message,
                                Toast.LENGTH_SHORT
                            ).show()

                            findNavController().navigateUp()
                        }

                        is CreateDriverState.Error -> {

                            binding.btnCreateDriver.isEnabled = true
                            binding.btnCreateDriver.text =
                                "Create Driver"

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