package com.vexardrive.fleetmanager.data.remote.interceptor

import com.vexardrive.fleetmanager.data.local.datastore.UserPreferencesManager
import com.vexardrive.fleetmanager.data.remote.api.AuthApi
import com.vexardrive.fleetmanager.data.remote.dto.auth.RefreshTokenRequestDto
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject

class RefreshTokenAuthenticator @Inject constructor(
    private val userPreferencesManager: UserPreferencesManager,
    private val authApi: dagger.Lazy<AuthApi>
) : Authenticator {

    override fun authenticate(
        route: Route?,
        response: Response
    ): Request? {

        synchronized(this) {

            // Prevent infinite retry
            if (responseCount(response) >= 2) {
                return null
            }

            val authHeader =
                response.request.header("Authorization")

            if (
                authHeader == null ||
                !authHeader.startsWith("Bearer ")
            ) {
                return null
            }

            val refreshToken = runBlocking {
                userPreferencesManager.refreshToken.first()
            } ?: return null

            val refreshResponse = runBlocking {

                authApi.get().refreshToken(
                    RefreshTokenRequestDto(
                        refreshToken = refreshToken
                    )
                )
            }

            if (!refreshResponse.isSuccessful) {

                runBlocking {
                    userPreferencesManager.clearSession()
                }

                return null
            }

            val newAccessToken =
                refreshResponse.body()
                    ?.data
                    ?.accessToken
                    ?: return null

            runBlocking {
                userPreferencesManager.saveAccessToken(
                    newAccessToken
                )
            }

            return response.request
                .newBuilder()
                .header(
                    "Authorization",
                    "Bearer $newAccessToken"
                )
                .build()
        }
    }

    private fun responseCount(
        response: Response
    ): Int {

        var count = 1
        var priorResponse = response.priorResponse

        while (priorResponse != null) {
            count++
            priorResponse = priorResponse.priorResponse
        }

        return count
    }
}