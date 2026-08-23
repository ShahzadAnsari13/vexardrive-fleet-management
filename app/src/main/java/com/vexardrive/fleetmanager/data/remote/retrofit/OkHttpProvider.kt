package com.vexardrive.fleetmanager.data.remote.retrofit

import com.vexardrive.fleetmanager.data.remote.interceptor.AuthInterceptor
import com.vexardrive.fleetmanager.data.remote.interceptor.RefreshTokenAuthenticator
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

object OkHttpProvider {

    fun provide(
        loggingInterceptor: HttpLoggingInterceptor,
        authInterceptor: AuthInterceptor,
        refreshTokenAuthenticator: RefreshTokenAuthenticator
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(authInterceptor)
            .authenticator(refreshTokenAuthenticator)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }
}