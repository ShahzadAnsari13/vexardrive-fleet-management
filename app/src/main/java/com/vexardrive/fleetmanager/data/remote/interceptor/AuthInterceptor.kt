package com.vexardrive.fleetmanager.data.remote.interceptor

import com.vexardrive.fleetmanager.data.local.datastore.UserPreferencesManager
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val userPreferencesManager: UserPreferencesManager
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        val token = runBlocking {
            userPreferencesManager.accessToken.first()
        }

        val request = if (!token.isNullOrBlank()) {

            chain.request()
                .newBuilder()
                .header(
                    "Authorization",
                    "Bearer $token"
                )
                .build()

        } else {
            chain.request()
        }

        return chain.proceed(request)
    }
}