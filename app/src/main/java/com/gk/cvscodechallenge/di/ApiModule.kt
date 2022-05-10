package com.gk.cvscodechallenge.di

import com.gk.cvscodechallenge.api.FlickrApi
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton

private val json = Json { isLenient = true }

@Module
@InstallIn(SingletonComponent::class)
class ApiModule {
    @OptIn(ExperimentalSerializationApi::class)
    @Singleton
    @Provides
    fun provideFlickrApi(
        @Named("flickr_base_url") baseUrl: String
    ): FlickrApi = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(OkHttpClient.Builder().build())
        .addConverterFactory(json.asConverterFactory(MediaType.get("application/json")))
        .build()
        .create(FlickrApi::class.java)
}