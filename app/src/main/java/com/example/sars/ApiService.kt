package com.example.sars

import android.content.Context
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {

    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): AuthResponse

    @POST("api/auth/register")
    suspend fun register(@Body request: RegisterRequest): AuthResponse

    @GET("api/auth/profile")
    suspend fun getProfile(): ProfileResponse

    @GET("api/directory/")
    suspend fun getDirectoryEntries(): DirectoryListResponse

    @POST("api/reports/")
    suspend fun createReport(@Body report: AnimalReport): AnimalReport

    @GET("api/reports/nearby")
    suspend fun getNearbyReports(): List<AnimalReport>

    @GET("api/reports/heatmap")
    suspend fun getHeatmapData(): List<AnimalReport>

    companion object {
        private const val BASE_URL = "https://pawnder-backend.vercel.app/"

        fun create(context: Context): ApiService {
            val logger = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }

            val authInterceptor = Interceptor { chain ->
                val token = TokenManager.getToken(context)
                val request = chain.request().newBuilder()
                if (token != null) {
                    request.addHeader("Authorization", "Bearer $token")
                }
                chain.proceed(request.build())
            }

            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .addInterceptor(authInterceptor)
                .build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiService::class.java)
        }
    }
}
