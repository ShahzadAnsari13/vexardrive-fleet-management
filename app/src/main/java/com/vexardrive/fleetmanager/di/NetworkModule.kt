package com.vexardrive.fleetmanager.di

import com.vexardrive.fleetmanager.data.remote.api.AuthApi
import com.vexardrive.fleetmanager.data.remote.api.manager.AssignmentApi
import com.vexardrive.fleetmanager.data.remote.api.manager.DashboardApi
import com.vexardrive.fleetmanager.data.remote.api.manager.DriverApi
import com.vexardrive.fleetmanager.data.remote.api.manager.VehicleApi
import com.vexardrive.fleetmanager.data.remote.interceptor.AuthInterceptor
import com.vexardrive.fleetmanager.data.remote.interceptor.RefreshTokenAuthenticator
import com.vexardrive.fleetmanager.data.remote.retrofit.LoggingInterceptorProvider
import com.vexardrive.fleetmanager.data.remote.retrofit.OkHttpProvider
import com.vexardrive.fleetmanager.data.remote.retrofit.RetrofitProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import javax.inject.Singleton
import kotlin.jvm.java

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {
    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return LoggingInterceptorProvider.provide()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        authInterceptor: AuthInterceptor,
        refreshTokenAuthenticator: RefreshTokenAuthenticator
    ): OkHttpClient {
        return OkHttpProvider.provide(loggingInterceptor,authInterceptor,refreshTokenAuthenticator)
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient
    ): Retrofit {
        return RetrofitProvider.provide(okHttpClient)
    }

    @Provides
    @Singleton
    fun provideAuthApi(retrofit: Retrofit): AuthApi {
        return retrofit.create(AuthApi::class.java)
    }

    @Provides
    @Singleton
    fun provideDashboardApi(
        retrofit: Retrofit
    ): DashboardApi {
        return retrofit.create(DashboardApi::class.java)
    }

    @Provides
    @Singleton
    fun provideVehicleApi(retrofit: Retrofit): VehicleApi {
        return retrofit.create(VehicleApi::class.java)
    }
    @Provides
    @Singleton
    fun provideDriverApi(
        retrofit: Retrofit
    ): DriverApi {
        return retrofit.create(DriverApi::class.java)
    }
    @Provides
    @Singleton
    fun provideAssignmentApi(
        retrofit: Retrofit
    ): AssignmentApi {
        return retrofit.create(AssignmentApi::class.java)
    }
}