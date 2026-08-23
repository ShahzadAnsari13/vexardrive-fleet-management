package com.vexardrive.fleetmanager.presentation.manager.assignment.fragment

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
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.vexardrive.fleetmanager.R
import com.vexardrive.fleetmanager.data.remote.dto.manager.assignment.AssignmentDetailsDto
import com.vexardrive.fleetmanager.databinding.FragmentAssignmentDetailsBinding
import com.vexardrive.fleetmanager.presentation.manager.assignment.state.AssignmentDetailsState
import com.vexardrive.fleetmanager.presentation.manager.assignment.state.DeleteAssignmentState
import com.vexardrive.fleetmanager.presentation.manager.assignment.viewmodel.AssignmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class AssignmentDetailsFragment :
    Fragment(R.layout.fragment_assignment_details) {

    private var _binding: FragmentAssignmentDetailsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AssignmentViewModel by viewModels()

    private val args: AssignmentDetailsFragmentArgs by navArgs()

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentAssignmentDetailsBinding.bind(view)

        setupClicks()
        observeAssignmentDetails()
        observeDeleteAssignment()

        viewModel.getAssignmentById(args.assignmentId)
    }

    private fun setupClicks() {

        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.btnDeleteAssignment.setOnClickListener {
            showDeleteConfirmation()
        }
    }

    private fun observeAssignmentDetails() {

        viewLifecycleOwner.lifecycleScope.launch {

            viewLifecycleOwner.repeatOnLifecycle(
                Lifecycle.State.STARTED
            ) {

                viewModel.assignmentDetailsState.collect { state ->

                    when (state) {

                        AssignmentDetailsState.Idle -> Unit

                        AssignmentDetailsState.Loading -> {
                            showLoading()
                        }

                        is AssignmentDetailsState.Success -> {

                            hideLoading()

                            bindAssignmentDetails(
                                state.assignment
                            )
                        }

                        is AssignmentDetailsState.Error -> {

                            hideLoading()

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

    private fun bindAssignmentDetails(
        assignment: AssignmentDetailsDto
    ) {

        binding.tvAssignmentId.text =
            assignment.id

        binding.tvDriverName.text =
            assignment.driver_name

        binding.tvVehicleNumber.text =
            assignment.registration_number

        binding.tvVehicleType.text =
            assignment.vehicle_type

        binding.tvStartDate.text =
            assignment.start_date

        binding.tvEndDate.text =
            assignment.end_date
    }

    private fun showDeleteConfirmation() {

        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Delete Assignment?")
            .setMessage(
                "This assignment will be permanently deleted. This action cannot be undone."
            )
            .setNegativeButton("Cancel", null)
            .setPositiveButton("Delete") { _, _ ->

                viewModel.deleteAssignment(
                    args.assignmentId
                )
            }
            .show()
    }

    private fun observeDeleteAssignment() {

        viewLifecycleOwner.lifecycleScope.launch {

            viewLifecycleOwner.repeatOnLifecycle(
                Lifecycle.State.STARTED
            ) {

                viewModel.deleteAssignmentState.collect { state ->

                    when (state) {

                        DeleteAssignmentState.Idle -> Unit

                        DeleteAssignmentState.Loading -> {
                            binding.btnDeleteAssignment.isEnabled = false
                        }

                        is DeleteAssignmentState.Success -> {

                            binding.btnDeleteAssignment.isEnabled = true

                            Toast.makeText(
                                requireContext(),
                                state.response.message,
                                Toast.LENGTH_SHORT
                            ).show()

                            findNavController().navigateUp()
                        }

                        is DeleteAssignmentState.Error -> {

                            binding.btnDeleteAssignment.isEnabled = true

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

    private fun showLoading() {
        binding.assignmentDetailsContent.visibility = View.GONE
        // shimmer yahan
    }

    private fun hideLoading() {
        binding.assignmentDetailsContent.visibility = View.VISIBLE
        // shimmer hide yahan
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}