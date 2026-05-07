package com.example.moviehubapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movies")
data class MovieEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val overview: String,
    val posterPath: String?,
    val backdropPath: String?,
    val releaseDate: String,
    val voteAverage: Double,
    val voteCount: Int,
    // genre_ids almacenados como String separado por comas, ej: "28,12,878"
    val genreIds: String,
    val popularity: Double,
    val originalLanguage: String
)

@Entity(tableName = "genres")
data class GenreEntity(
    @PrimaryKey val id: Int,
    val name: String
)

@Entity(tableName = "movie_details")
data class MovieDetailEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val tagline: String?,
    val overview: String,
    val posterPath: String?,
    val backdropPath: String?,
    val releaseDate: String,
    val voteAverage: Double,
    val voteCount: Int,
    val runtime: Int?,
    val status: String,
    // géneros serializados como "id1:name1,id2:name2"
    val genres: String,
    val budget: Long,
    val revenue: Long,
    val originalLanguage: String,
    val popularity: Double,
    val homepage: String?
)