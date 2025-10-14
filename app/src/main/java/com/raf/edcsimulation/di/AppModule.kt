package com.raf.edcsimulation.di

import android.content.Context
import com.raf.edcsimulation.BuildConfig
import com.raf.edcsimulation.auth.data.local.AuthDataStore
import com.raf.edcsimulation.auth.data.remote.AuthApiService
import com.raf.edcsimulation.auth.data.repository.AuthRepositoryImpl
import com.raf.edcsimulation.auth.domain.usecase.LoginUseCase
import com.raf.edcsimulation.auth.domain.usecase.LogoutUseCase
import com.raf.edcsimulation.auth.domain.usecase.RegisterUseCase
import com.raf.edcsimulation.core.domain.repository.AuthRepository
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
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL) // Add BASE_URL in locale.properties
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideAuthService(retrofit: Retrofit): AuthApiService {
        return retrofit.create(AuthApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideAuthDataStore(@ApplicationContext context: Context): AuthDataStore {
        return AuthDataStore(context)
    }

    @Provides
    @Singleton
    fun provideAuthRepository(
        authDataStore: AuthDataStore,
        authApiService: AuthApiService,
    ): AuthRepository = AuthRepositoryImpl(
        authDataStore = authDataStore,
        authApiService = authApiService,
    )

    /**
     * Auth Use Cases
     */
    @Provides
    @Singleton
    fun provideLoginUseCase(authRepository: AuthRepository): LoginUseCase {
        return LoginUseCase(authRepository)
    }

    @Provides
    @Singleton
    fun provideRegisterUseCase(authRepository: AuthRepository): RegisterUseCase {
        return RegisterUseCase(authRepository)
    }

    @Provides
    @Singleton
    fun provideLogoutUseCase(authRepository: AuthRepository): LogoutUseCase {
        return LogoutUseCase(authRepository)
    }

}