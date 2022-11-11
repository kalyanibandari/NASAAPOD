package com.example.nasaapod

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.nasaapod.data.db.ApodDatabse
import com.example.nasaapod.data.network.ApodApi
import com.example.nasaapod.data.network.RemoteDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(app: Application): ApodDatabse =
         Room.databaseBuilder(app, ApodDatabse::class.java, "apod_db").build()

    @Provides
    @Singleton
    fun provideApodApi(remoteDataSource: RemoteDataSource,
                       @ApplicationContext context: Context
    ) : ApodApi {
        return remoteDataSource.buildApi(ApodApi::class.java, context)
    }

}