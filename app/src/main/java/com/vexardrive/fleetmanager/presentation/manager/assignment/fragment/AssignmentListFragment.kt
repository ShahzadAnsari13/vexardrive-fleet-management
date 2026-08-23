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
import com.vexardrive.fleetmanager.R
import com.vexardrive.fleetmanager.data.remote.dto.manager.assignment.AssignmentDto
import com.vexardrive.fleetmanager.databinding.FragmentAssignmentListBinding
import com.vexardrive.fleetmanager.presentation.manager.assignment.adapter.AssignmentAdapter
import com.vexardrive.fleetmanager.presentation.manager.assignment.state.AssignmentListState
import com.vexardrive.fleetmanager.presentation.manager.assignment.viewmodel.AssignmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class AssignmentListFragment :
    Fragment(R.layout.fragment_assignment_list) {

    private var _binding: FragmentAssignmentListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AssignmentViewModel by viewModels()

    private lateinit var assignmentAdapter: AssignmentAdapter

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentAssignmentListBinding.bind(view)

        setupRecyclerView()
        setupClicks()
        observeAssignmentList()

        viewModel.getAssignments()
    }

    private fun setupRecyclerView() {

        assignmentAdapter = AssignmentAdapter { assignment ->

            val action =
                AssignmentListFragmentDirections
                    .actionAssignmentListFragmentToAssignmentDetailsFragment(
                        assignmentId = assignment.id
                    )

            findNavController().navigate(action)
        }

        binding.rvAssignments.adapter = assignmentAdapter
    }

    private fun setupClicks() {

        binding.btnAddAssignment.setOnClickListener {
            navigateToCreateAssignment()
        }

        binding.fabAddAssignment.setOnClickListener {
            navigateToCreateAssignment()
        }

        binding.swipeRefreshAssignments.setOnRefreshListener {
            viewModel.getAssignments()
        }
    }

    private fun navigateToCreateAssignment() {

//        findNavController().navigate(
//            R.id.action_assignmentListFragment_to_createAssignmentFragment
//        )
    }

    private fun observeAssignmentList() {

        viewLifecycleOwner.lifecycleScope.launch {

            viewLifecycleOwner.repeatOnLifecycle(
                Lifecycle.State.STARTED
            ) {

                viewModel.assignmentListState.collect { state ->

                    when (state) {

                        AssignmentListState.Idle -> Unit

                        AssignmentListState.Loading -> {
                            showLoading()
                        }

                        is AssignmentListState.Success -> {

                            hideLoading()

                            binding.swipeRefreshAssignments.isRefreshing =
                                false

                            val assignments = state.assignments

                            binding.tvAssignmentCount.text =
                                assignments.size.toString()

                            if (assignments.isEmpty()) {
                                showEmptyState()
                            } else {
                                showAssignments(assignments)
                            }
                        }

                        is AssignmentListState.Error -> {

                            hideLoading()

                            binding.swipeRefreshAssignments.isRefreshing =
                                false

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
        binding.assignmentListShimmer.root.visibility = View.VISIBLE
        binding.assignmentListShimmer.root.startShimmer()
        binding.rvAssignments.visibility = View.GONE

        // Agar shimmer add kiya hai to yahan start karenge
    }

    private fun hideLoading() {
        binding.assignmentListShimmer.root.stopShimmer()
        binding.assignmentListShimmer.root.visibility = View.GONE
        binding.rvAssignments.visibility = View.VISIBLE
    }

    private fun showAssignments(
        assignments: List<AssignmentDto>
    ) {

        binding.rvAssignments.visibility = View.VISIBLE
        binding.emptyAssignmentContainer.visibility = View.GONE

        assignmentAdapter.submitList(assignments)
    }

    private fun showEmptyState() {

        binding.rvAssignments.visibility = View.GONE
        binding.emptyAssignmentContainer.visibility = View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}