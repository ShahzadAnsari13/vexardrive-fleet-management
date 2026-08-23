package com.vexardrive.fleetmanager.presentation.manager.assignment.fragment

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.ArrayAdapter
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.vexardrive.fleetmanager.R
import com.vexardrive.fleetmanager.data.remote.dto.manager.assignment.CreateAssignmentRequest
import com.vexardrive.fleetmanager.databinding.FragmentCreateAssignmentBinding
import com.vexardrive.fleetmanager.databinding.FragmentCreateDriverBinding
import com.vexardrive.fleetmanager.presentation.manager.assignment.state.CreateAssignmentState
import com.vexardrive.fleetmanager.presentation.manager.assignment.viewmodel.AssignmentViewModel
import com.vexardrive.fleetmanager.presentation.manager.driver.state.DriverListUiState
import com.vexardrive.fleetmanager.presentation.manager.driver.viewmodel.DriverViewModel
import com.vexardrive.fleetmanager.presentation.manager.vehicle.state.VehicleUiState
import com.vexardrive.fleetmanager.presentation.manager.vehicle.viewmodel.VehicleViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class CreateAssignmentFragment : Fragment() {
    private var _binding: FragmentCreateAssignmentBinding? = null
    private val binding get() = _binding!!
    private val driverViewModel: DriverViewModel by activityViewModels()
    private val vehicleViewModel: VehicleViewModel by viewModels()

    private val assignmentViewModel : AssignmentViewModel by viewModels()
    private var selectedVehicleId: String? = null
    private var selectedDriverId: String? = null
    private var startDate: String? = null
    private var endDate: String? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCreateAssignmentBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeDrivers()
        observeVehicles()
        driverViewModel.getDrivers()
        vehicleViewModel.getVehicles()
        observeCreateAssignment()
        binding.etStartDate.setOnClickListener {
            showStartDatePicker()
        }

        binding.startDateInputLayout.setEndIconOnClickListener {
            showStartDatePicker()
        }
        binding.etEndDate.setOnClickListener {
            showEndDatePicker()
        }

        binding.endDateInputLayout.setEndIconOnClickListener {
            showEndDatePicker()
        }

        binding.btnCreateAssignment.setOnClickListener {
            createAssignment()
        }
    }

    private fun observeDrivers() {

        viewLifecycleOwner.lifecycleScope.launch {

            viewLifecycleOwner.repeatOnLifecycle(
                Lifecycle.State.STARTED
            ) {

                driverViewModel.uiState.collect { state ->

                    when (state) {

                        DriverListUiState.Idle -> Unit

                        DriverListUiState.Loading -> {
                            binding.etDriver.isEnabled = false
                        }

                        is DriverListUiState.Success -> {

                            binding.etDriver.isEnabled = true

                            val drivers = state.drivers

                            val driverNames =
                                drivers.map { it.name }

                            binding.etDriver.setAdapter(
                                ArrayAdapter(
                                    requireContext(),
                                    android.R.layout.simple_dropdown_item_1line,
                                    driverNames
                                )
                            )

                            binding.etDriver.setOnItemClickListener {
                                    _, _, position, _ ->

                                selectedDriverId =
                                    drivers[position].id
                            }
                        }

                        DriverListUiState.Empty -> {
                            binding.etDriver.isEnabled = false
                        }

                        is DriverListUiState.Error -> {

                            binding.etDriver.isEnabled = false

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
    private fun observeVehicles() {

        viewLifecycleOwner.lifecycleScope.launch {

            viewLifecycleOwner.repeatOnLifecycle(
                Lifecycle.State.STARTED
            ) {

                vehicleViewModel.vehicles.collect { state ->

                    when (state) {

                        VehicleUiState.Idle -> Unit

                        VehicleUiState.Loading -> {
                            binding.etVehicle.isEnabled = false
                        }

                        is VehicleUiState.Success -> {

                            val availableVehicles =
                                state.data.data.filter {
                                    it.status != "INACTIVE"
                                }

                            if (availableVehicles.isEmpty()) {
                                binding.etVehicle.isEnabled = false
                                return@collect
                            }

                            binding.etVehicle.isEnabled = true

                            val vehicleNumbers =
                                availableVehicles.map {
                                    it.registration_number
                                }

                            binding.etVehicle.setAdapter(
                                ArrayAdapter(
                                    requireContext(),
                                    android.R.layout.simple_dropdown_item_1line,
                                    vehicleNumbers
                                )
                            )

                            binding.etVehicle.setOnItemClickListener {
                                    _, _, position, _ ->

                                selectedVehicleId =
                                    availableVehicles[position].id

                                binding.vehicleInputLayout.error = null
                            }
                        }

                        VehicleUiState.Empty -> {
                            binding.etVehicle.isEnabled = false
                        }

                        is VehicleUiState.Error -> {

                            binding.etVehicle.isEnabled = false

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
    private fun showStartDatePicker() {

        val calendar = Calendar.getInstance()

        DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->

                val selectedCalendar =
                    Calendar.getInstance().apply {
                        set(year, month, dayOfMonth)
                    }

                startDate = SimpleDateFormat(
                    "yyyy-MM-dd",
                    Locale.getDefault()
                ).format(selectedCalendar.time)

                binding.etStartDate.setText(startDate)

                binding.startDateInputLayout.error = null
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }
    private fun showEndDatePicker() {

        val calendar = Calendar.getInstance()

        DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->

                val selectedCalendar =
                    Calendar.getInstance().apply {
                        set(year, month, dayOfMonth)
                    }

                endDate = SimpleDateFormat(
                    "yyyy-MM-dd",
                    Locale.getDefault()
                ).format(selectedCalendar.time)

                binding.etEndDate.setText(endDate)

                binding.endDateInputLayout.error = null
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun createAssignment() {

        binding.driverInputLayout.error = null
        binding.vehicleInputLayout.error = null
        binding.startDateInputLayout.error = null
        binding.endDateInputLayout.error = null

        val driverId = selectedDriverId
        val vehicleId = selectedVehicleId

        if (driverId == null) {
            binding.driverInputLayout.error = "Please select driver"
            return
        }

        if (vehicleId == null) {
            binding.vehicleInputLayout.error = "Please select vehicle"
            return
        }

        if (!validateDates()) {
            return
        }

        val request = CreateAssignmentRequest(
            driverId = driverId,
            vehicleId = vehicleId,
            startDate = startDate!!,
            endDate = endDate!!
        )

        assignmentViewModel.createAssignment(request)
    }



    private fun observeCreateAssignment() {

        viewLifecycleOwner.lifecycleScope.launch {

            viewLifecycleOwner.repeatOnLifecycle(
                Lifecycle.State.STARTED
            ) {

                assignmentViewModel.createAssignmentState.collect { state ->

                    when (state) {

                        CreateAssignmentState.Idle -> Unit

                        CreateAssignmentState.Loading -> {
                            binding.btnCreateAssignment.isEnabled = false
                            binding.btnCreateAssignment.text = "Creating..."
                        }

                        is CreateAssignmentState.Success -> {

                            binding.btnCreateAssignment.isEnabled = true
                            binding.btnCreateAssignment.text =
                                "Create Assignment"

                            Toast.makeText(
                                requireContext(),
                                state.response.message,
                                Toast.LENGTH_SHORT
                            ).show()

                            findNavController().navigateUp()
                        }

                        is CreateAssignmentState.Error -> {

                            binding.btnCreateAssignment.isEnabled = true
                            binding.btnCreateAssignment.text =
                                "Create Assignment"

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
    private fun validateDates(): Boolean {

        if (startDate == null) {
            binding.startDateInputLayout.error =
                "Please select start date"
            return false
        }

        if (endDate == null) {
            binding.endDateInputLayout.error =
                "Please select end date"
            return false
        }

        if (endDate!! < startDate!!) {
            binding.endDateInputLayout.error =
                "End date cannot be before start date"
            return false
        }

        return true
    }
}