package com.vexardrive.fleetmanager.presentation.auth.fragment

import android.content.Intent
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
import com.vexardrive.fleetmanager.databinding.FragmentRegisterBinding
import com.vexardrive.fleetmanager.presentation.auth.event.AuthEvent
import com.vexardrive.fleetmanager.presentation.auth.state.AuthState
import com.vexardrive.fleetmanager.presentation.auth.viewmodel.AuthViewModel
import com.vexardrive.fleetmanager.presentation.manager.ManagerActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlin.jvm.java

@AndroidEntryPoint
class RegisterFragment : Fragment() {
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AuthViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(
            inflater,
            container,
            false
        )

        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeState()
        observeEvents()
        binding.tvLogin.setOnClickListener {
            findNavController().navigate(
                R.id.action_registerFragment_to_loginFragment
            )
        }

        binding.btnRegister.setOnClickListener {
            register()
        }
    }
    private fun register() {
        val name = binding.etName.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()
        val phone = binding.etPhone.text.toString().trim()
        val password = binding.etPassword.text.toString()
        val confirmPassword = binding.etConfirmPassword.text.toString()

        if (name.isEmpty()) {
            binding.tilName.error = "Name is required"
            return
        }

        if (email.isEmpty()) {
            binding.tilEmail.error = "Email is required"
            return
        }

        if (phone.isEmpty()) {
            binding.tilPhone.error = "Phone is required"
            return
        }

        if (password.isEmpty()) {
            binding.tilPassword.error = "Password is required"
            return
        }
        if (password != confirmPassword) {
            binding.tilConfirmPassword.error = "Passwords do not match"
            return
        }

        clearErrors()

        viewModel.register(
            name = name,
            email = email,
            phone = phone,
            password = password,
            confirmPassword = confirmPassword
        )
    }

    private fun observeState() {

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(
                Lifecycle.State.STARTED
            ) {
                viewModel.uiState.collect { state ->

                    when (state) {

                        AuthState.Idle -> {
                            binding.btnRegister.isEnabled = true
                            binding.btnRegister.text = "CREATE ACCOUNT"
                        }

                        AuthState.Loading -> {
                            binding.btnRegister.isEnabled = false
                            binding.btnRegister.text = "Creating account..."
                        }
                    }
                }
            }
        }
    }
    private fun observeEvents() {

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(
                Lifecycle.State.STARTED
            ) {
                viewModel.events.collect { event ->

                    when (event) {

                        is AuthEvent.RegisterSuccess -> {
                            binding.btnRegister.text = "ACCOUNT CREATED"
                            startActivity(
                                Intent(requireContext(), ManagerActivity::class.java)
                            )
                            requireActivity().finish()
                        }

                        is AuthEvent.Error -> {
                            binding.btnRegister.text = "CREATE ACCOUNT"
                            Toast.makeText(
                                requireContext(),
                                event.message,
                                Toast.LENGTH_LONG
                            ).show()
                        }

                        is AuthEvent.LoginSuccess -> {
                            // Not handled here
                        }
                    }
                }
            }
        }
    }

    private fun clearErrors() {
        binding.tilName.error = null
        binding.tilEmail.error = null
        binding.tilPhone.error = null
        binding.tilPassword.error = null
        binding.tilConfirmPassword.error = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}