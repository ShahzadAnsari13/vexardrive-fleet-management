package com.vexardrive.fleetmanager.presentation.manager.vehicle.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.vexardrive.fleetmanager.data.remote.dto.manager.vehicle.VehicleDto
import com.vexardrive.fleetmanager.databinding.ItemVehicleBinding

class VehicleAdapter(
    private val onVehicleClick: (VehicleDto) -> Unit
) : ListAdapter<VehicleDto, VehicleAdapter.VehicleViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): VehicleViewHolder {

        val binding = ItemVehicleBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return VehicleViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: VehicleViewHolder,
        position: Int
    ) {
        holder.bind(getItem(position))
    }

    inner class VehicleViewHolder(
        private val binding: ItemVehicleBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(vehicle: VehicleDto) {

            binding.tvRegistrationNumber.text =
                vehicle.registration_number

            binding.tvVehicleInfo.text =
                "${vehicle.make} ${vehicle.model} • ${vehicle.vehicle_type}"

            binding.tvStatus.text =
                vehicle.status.replace("_", " ")

            binding.root.setOnClickListener {
                onVehicleClick(vehicle)
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<VehicleDto>() {

        override fun areItemsTheSame(
            oldItem: VehicleDto,
            newItem: VehicleDto
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: VehicleDto,
            newItem: VehicleDto
        ): Boolean {
            return oldItem == newItem
        }
    }
}