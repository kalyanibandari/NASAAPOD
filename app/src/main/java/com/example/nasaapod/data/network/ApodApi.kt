package com.example.nasaapod.data.network

import com.example.nasaapod.BuildConfig
import com.example.nasaapod.data.db.ApodEntity
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Get calls for Nebula Api
 */
interface ApodApi {
    @GET("apod")
    suspend fun getApod(@Query("api_key") api_key: String = BuildConfig.APOD_API_KEY): Response<ApodEntity>

    @GET("apod")
    suspend fun getSpecificApod(@Query("api_key") api_key: String = BuildConfig.APOD_API_KEY, @Query("date") date: String): Response<ApodEntity>

}