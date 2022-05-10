package com.gk.cvscodechallenge.di

import com.gk.cvscodechallenge.BuildConfig
import com.gk.cvscodechallenge.repository.FlickrRepository
import com.gk.cvscodechallenge.repository.FlickrRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    @Named("flickr_base_url")
    fun provideFlickrBaseUrl(): String = BuildConfig.FLICKR_BASE_URL
}

@Module
@InstallIn(SingletonComponent::class)
abstract class AppBinderModule {
    @Singleton
    @Binds abstract fun bindFlickrRepository(impl: FlickrRepositoryImpl): FlickrRepository
}