package com.raf.edcsimulation.di

import android.content.Context
import com.raf.edcsimulation.BuildConfig
import com.raf.edcsimulation.auth.data.local.AuthDataStore
import com.raf.edcsimulation.auth.data.remote.AuthApiService
import com.raf.edcsimulation.auth.data.repository.AuthRepositoryImpl
import com.raf.edcsimulation.auth.domain.repository.AuthRepository
import com.raf.edcsimulation.auth.domain.usecase.LoginUseCase
import com.raf.edcsimulation.auth.domain.usecase.RegisterUseCase
import com.raf.edcsimulation.core.domain.contracts.AppSettingsProvider
import com.raf.edcsimulation.core.domain.contracts.AuthTokenProvider
import com.raf.edcsimulation.core.domain.usecase.GetAppSettingsUseCase
import com.raf.edcsimulation.core.domain.usecase.GetTokenSessionUseCase
import com.raf.edcsimulation.core.domain.usecase.LogoutUseCase
import com.raf.edcsimulation.core.domain.usecase.SetAppSettingsUseCase
import com.raf.settings.data.local.SettingsDataStore
import com.raf.settings.data.repository.SettingsRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    /**
     * Retrofit Configuration
     * NOTE: Add BASE_URL in locale.properties
     */
    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
            else HttpLoggingInterceptor.Level.NONE
        }
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL) // Add BASE_URL in locale.properties
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    /**
     * Auth
     */
    @Provides
    @Singleton
    fun provideAuthService(retrofit: Retrofit): AuthApiService =
        retrofit.create(AuthApiService::class.java)

    @Provides
    @Singleton
    fun provideAuthDataStore(@ApplicationContext context: Context) = AuthDataStore(context)

    @Provides
    @Singleton
    fun provideAuthRepository(
        authDataStore: AuthDataStore,
        authApiService: AuthApiService,
    ): AuthRepository = AuthRepositoryImpl(
        authDataStore = authDataStore,
        authApiService = authApiService,
    )

    @Provides
    @Singleton
    fun provideAuthTokenProvider(repository: AuthRepositoryImpl): AuthTokenProvider = repository

    /**
     * Settings
     */
    @Provides
    @Singleton
    fun provideSettingsDataStore(@ApplicationContext context: Context) =
        SettingsDataStore(context)

    @Provides
    @Singleton
    fun provideAppSettingsProvider(settingsRepository: SettingsRepositoryImpl): AppSettingsProvider =
        settingsRepository

    @Provides
    @Singleton
    fun provideSettingsRepository(settingsDataStore: SettingsDataStore) =
        SettingsRepositoryImpl(settingsDataStore)

    /**
     * Auth Use Cases
     */
    @Provides
    @Singleton
    fun provideLoginUseCase(authRepository: AuthRepository) =
        LoginUseCase(authRepository)

    @Provides
    @Singleton
    fun provideRegisterUseCase(authRepository: AuthRepository) =
        RegisterUseCase(authRepository)

    /**
     * Core Use Cases
     */
    @Provides
    @Singleton
    fun provideGetTokenSessionUseCase(authTokenProvider: AuthTokenProvider) =
        GetTokenSessionUseCase(authTokenProvider)

    @Provides
    @Singleton
    fun provideLogoutUseCase(authTokenProvider: AuthTokenProvider) =
        LogoutUseCase(authTokenProvider)

    @Provides
    @Singleton
    fun provideGetAppSettingsUseCase(appSettingsProvider: AppSettingsProvider) =
        GetAppSettingsUseCase(appSettingsProvider)

    @Provides
    @Singleton
    fun provideSetAppSettingsUseCase(appSettingsProvider: AppSettingsProvider) =
        SetAppSettingsUseCase(appSettingsProvider)
}