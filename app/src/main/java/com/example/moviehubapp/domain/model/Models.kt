package com.example.moviehubapp.domain.model

// ── Modelo de película para lista ─────────────────────────────────────────────
data class Movie(
    val id: Int,
    val title: String,
    val overview: String,
    val posterPath: String?,
    val backdropPath: String?,
    val releaseDate: String,
    val voteAverage: Double,
    val voteCount: Int,
    val genreIds: List<Int>,
    val popularity: Double,
    val originalLanguage: String
)

// ── Modelo detallado de película ──────────────────────────────────────────────
data class MovieDetail(
    val id: Int,
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
    val genres: List<Genre>,
    val budget: Long,
    val revenue: Long,
    val originalLanguage: String,
    val popularity: Double,
    val homepage: String?
)

// ── Género ────────────────────────────────────────────────────────────────────
data class Genre(
    val id: Int,
    val name: String
)

// ── Estado genérico de UI ─────────────────────────────────────────────────────
sealed class Resource<out T> {
    data class Success<out T>(val data: T) : Resource<T>()
    data class Error(val message: String) : Resource<Nothing>()
    object Loading : Resource<Nothing>()
}