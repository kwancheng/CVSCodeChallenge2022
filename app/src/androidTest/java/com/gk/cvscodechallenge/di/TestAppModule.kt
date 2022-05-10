package com.gk.cvscodechallenge.di

import com.gk.cvscodechallenge.repository.FlickrRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import io.mockk.mockk
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [AppModule::class, AppBinderModule::class]
)
class TestAppModule {
    @Singleton
    @Provides
    fun provideFlickrRepository(): FlickrRepository = mockk()
}