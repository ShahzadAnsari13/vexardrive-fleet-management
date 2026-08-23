package com.vexardrive.fleetmanager.data.repository

import android.util.Log
import com.vexardrive.fleetmanager.data.remote.api.AuthApi
import com.vexardrive.fleetmanager.data.remote.dto.auth.AuthResponse
import com.vexardrive.fleetmanager.data.remote.dto.auth.LoginRequest
import com.vexardrive.fleetmanager.data.remote.dto.auth.LoginResponse
import com.vexardrive.fleetmanager.data.remote.dto.auth.RegisterRequest
import com.vexardrive.fleetmanager.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl@Inject constructor(
    private val authApi: AuthApi
) : AuthRepository {
    override suspend fun register(
        request: RegisterRequest
    ): Result<AuthResponse> {

        return try {

            val response = authApi.register(request)

            if (response.isSuccessful) {

                response.body()?.let {
                    Result.success(it)
                } ?: Result.failure(
                    Exception("Empty response from server")
                )

            } else {

                Result.failure(
                    Exception(
                        response.errorBody()?.string()
                            ?: "Registration failed"
                    )
                )
            }

        } catch (e: Exception) {

            Result.failure(e)
        }
    }
    override suspend fun login(
        request: LoginRequest
    ): Result<LoginResponse> {

        return try {
            val response = authApi.login(request)
            if (response.isSuccessful) {
                response.body()?.let {
                    Result.success(it)
                } ?: Result.failure(
                    Exception("Empty response from server")
                )
            } else {
                Result.failure(
                    Exception(
                        response.errorBody()?.string()
                            ?: "Login failed"
                    )
                )
            }

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}