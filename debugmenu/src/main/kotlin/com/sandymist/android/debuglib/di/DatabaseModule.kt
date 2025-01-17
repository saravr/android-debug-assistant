package com.sandymist.android.debuglib.di

import android.content.Context
import com.sandymist.android.debuglib.db.DebugLibDatabase
import com.sandymist.android.debuglib.db.DebugLibDatabaseProvider
import com.sandymist.android.debuglib.db.NetworkLogDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)  // For Hilt - this ensures the provider is available app-wide
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(context: Context): DebugLibDatabase {
        return DebugLibDatabaseProvider.getDatabase(context)
    }

    @Provides
    fun provideFeedDao(database: DebugLibDatabase): NetworkLogDao =
        database.networkLogDao()
}
