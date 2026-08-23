package com.vexardrive.fleetmanager.presentation.manager.driver.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.vexardrive.fleetmanager.R
import com.vexardrive.fleetmanager.data.remote.dto.manager.driver.DriverDto
import com.vexardrive.fleetmanager.databinding.ItemDriverBinding

class DriverAdapter(
    private val onDriverClick: (DriverDto) -> Unit
) : ListAdapter<DriverDto, DriverAdapter.DriverViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DriverViewHolder {

        val binding = ItemDriverBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return DriverViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: DriverViewHolder,
        position: Int
    ) {
        holder.bind(getItem(position))
    }

    inner class DriverViewHolder(
        private val binding: ItemDriverBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(driver: DriverDto) {

            binding.tvDriverName.text = driver.name

            binding.tvLicenseNumber.text =
                "License: ${driver.license_number}"

            binding.tvLicenseExpiry.text =
                "Expiry: ${driver.license_expiry}"

            binding.tvDriverStatus.text = driver.status

            if (driver.status == "ACTIVE") {
                binding.tvDriverStatus.setTextColor(
                    Color.parseColor("#168653")
                )
                binding.tvDriverStatus.background =
                    ContextCompat.getDrawable(
                        binding.root.context,
                        R.drawable.bg_driver_status_active
                    )
            } else {
                binding.tvDriverStatus.setTextColor(
                    Color.parseColor("#6B7280")
                )
                binding.tvDriverStatus.background =
                    ContextCompat.getDrawable(
                        binding.root.context,
                        R.drawable.bg_driver_status_inactive
                    )
            }

            binding.root.setOnClickListener {
                onDriverClick(driver)
            }


        }
    }

    class DiffCallback : DiffUtil.ItemCallback<DriverDto>() {

        override fun areItemsTheSame(
            oldItem: DriverDto,
            newItem: DriverDto
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: DriverDto,
            newItem: DriverDto
        ): Boolean {
            return oldItem == newItem
        }
    }
}