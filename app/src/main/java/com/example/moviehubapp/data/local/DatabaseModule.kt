package com.example.moviehubapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.moviehubapp.data.local.dao.GenreDao
import com.example.moviehubapp.data.local.dao.MovieDao
import com.example.moviehubapp.data.local.dao.MovieDetailDao
import com.example.moviehubapp.data.local.entity.GenreEntity
import com.example.moviehubapp.data.local.entity.MovieDetailEntity
import com.example.moviehubapp.data.local.entity.MovieEntity

@Database(
    entities = [MovieEntity::class, GenreEntity::class, MovieDetailEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
    abstract fun genreDao(): GenreDao
    abstract fun movieDetailDao(): MovieDetailDao
}