package com.gk.cvscodechallenge.di

import android.content.Context
import androidx.room.Room
import com.gk.cvscodechallenge.db.CVSCodeChallengeDB
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DBModule {
    @Singleton
    @Provides
    fun provideCVSCodeChallengeDB(@ApplicationContext context: Context) : CVSCodeChallengeDB {
        return Room
            .databaseBuilder(context, CVSCodeChallengeDB::class.java, "CVSCodeChallengeDB")
            .build()
    }

    @Singleton
    @Provides
    fun provideSearchTermDao(db: CVSCodeChallengeDB) = db.searchTermDao()
}