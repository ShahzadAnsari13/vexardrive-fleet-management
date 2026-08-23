package com.vexardrive.fleetmanager.presentation.manager.assignment.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.vexardrive.fleetmanager.data.remote.dto.manager.assignment.AssignmentDto
import com.vexardrive.fleetmanager.databinding.ItemAssignmentBinding

class AssignmentAdapter(
    private val onAssignmentClick: (AssignmentDto) -> Unit
) : ListAdapter<AssignmentDto, AssignmentAdapter.AssignmentViewHolder>(
    AssignmentDiffCallback()
) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AssignmentViewHolder {

        val binding = ItemAssignmentBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return AssignmentViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: AssignmentViewHolder,
        position: Int
    ) {
        holder.bind(getItem(position))
    }

    inner class AssignmentViewHolder(
        private val binding: ItemAssignmentBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(assignment: AssignmentDto) {

            binding.tvDriverName.text =
                assignment.driver_name

            binding.tvVehicleNumber.text =
                assignment.registration_number

            binding.tvStartDate.text =
                assignment.start_date

            binding.tvEndDate.text =
                assignment.end_date

            binding.root.setOnClickListener {
                onAssignmentClick(assignment)
            }
        }
    }

    class AssignmentDiffCallback :
        DiffUtil.ItemCallback<AssignmentDto>() {

        override fun areItemsTheSame(
            oldItem: AssignmentDto,
            newItem: AssignmentDto
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: AssignmentDto,
            newItem: AssignmentDto
        ): Boolean {
            return oldItem == newItem
        }
    }
}