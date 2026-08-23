package com.vexardrive.fleetmanager.presentation.auth.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import com.vexardrive.fleetmanager.databinding.FragmentLoginBinding
import com.vexardrive.fleetmanager.databinding.FragmentRegisterBinding
import com.vexardrive.fleetmanager.presentation.auth.event.AuthEvent
import com.vexardrive.fleetmanager.presentation.auth.state.AuthState
import com.vexardrive.fleetmanager.presentation.auth.viewmodel.AuthViewModel
import com.vexardrive.fleetmanager.presentation.driver.DriverActivity
import com.vexardrive.fleetmanager.presentation.manager.ManagerActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlin.jvm.java

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AuthViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(
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

        binding.btnLogin.setOnClickListener {
            login()
        }

        binding.tvRegister.setOnClickListener {
            findNavController().navigate(
                R.id.action_loginFragment_to_registerFragment
            )
        }
    }
    private fun login() {

        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString()

        binding.tilEmail.error = null
        binding.tilPassword.error = null

        if (email.isEmpty()) {
            binding.tilEmail.error = "Email is required"
            return
        }

        if (password.isEmpty()) {
            binding.tilPassword.error = "Password is required"
            return
        }

        viewModel.login(
            email = email,
            password = password
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
                            binding.btnLogin.isEnabled = true
                            binding.btnLogin.text = "LOGIN"
                        }

                        AuthState.Loading -> {
                            binding.btnLogin.isEnabled = false
                            binding.btnLogin.text = "Signing in..."
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

                        is AuthEvent.LoginSuccess -> {
                            binding.btnLogin.text = "LOGIN"
                            when (event.response.role) {

                                "FLEET_MANAGER" -> {
                                    startActivity(
                                        Intent(
                                            requireContext(),
                                            ManagerActivity::class.java
                                        )
                                    )
                                }

                                "DRIVER" -> {
                                    startActivity(
                                        Intent(
                                            requireContext(),
                                            DriverActivity::class.java
                                        )
                                    )
                                }

                                else -> {
                                    Toast.makeText(
                                        requireContext(),
                                        "Invalid user role",
                                        Toast.LENGTH_LONG
                                    ).show()
                                    return@collect
                                }
                            }

                            requireActivity().finish()
                        }

                        is AuthEvent.Error -> {
                            binding.btnLogin.text = "LOGIN"
                            Toast.makeText(
                                requireContext(),
                                event.message,
                                Toast.LENGTH_LONG
                            ).show()
                        }

                        is AuthEvent.RegisterSuccess -> {
                            // Not handled here
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