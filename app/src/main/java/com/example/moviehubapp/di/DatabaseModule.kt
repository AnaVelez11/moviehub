package com.example.moviehubapp.di

import android.content.Context
import androidx.room.Room
import com.example.moviehubapp.data.local.AppDatabase
import com.example.moviehubapp.data.local.dao.GenreDao
import com.example.moviehubapp.data.local.dao.MovieDao
import com.example.moviehubapp.data.local.dao.MovieDetailDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "moviehub_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideMovieDao(database: AppDatabase): MovieDao = database.movieDao()

    @Provides
    @Singleton
    fun provideGenreDao(database: AppDatabase): GenreDao = database.genreDao()

    @Provides
    @Singleton
    fun provideMovieDetailDao(database: AppDatabase): MovieDetailDao = database.movieDetailDao()
}