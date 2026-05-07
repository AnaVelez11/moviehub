package com.example.moviehubapp.data.repository


import com.example.moviehubapp.data.local.entity.GenreEntity
import com.example.moviehubapp.data.local.entity.MovieDetailEntity
import com.example.moviehubapp.data.local.entity.MovieEntity
import com.example.moviehubapp.data.remote.dto.GenreDto
import com.example.moviehubapp.data.remote.dto.MovieDetailDto
import com.example.moviehubapp.data.remote.dto.MovieDto
import com.example.moviehubapp.domain.model.Genre
import com.example.moviehubapp.domain.model.Movie
import com.example.moviehubapp.domain.model.MovieDetail

// ── DTO -> Domain ─────────────────────────────────────────────────────────────

fun MovieDto.toDomain(): Movie = Movie(
    id = id,
    title = title,
    overview = overview,
    posterPath = posterPath,
    backdropPath = backdropPath,
    releaseDate = releaseDate,
    voteAverage = voteAverage,
    voteCount = voteCount,
    genreIds = genreIds,
    popularity = popularity,
    originalLanguage = originalLanguage
)

fun MovieDetailDto.toDomain(): MovieDetail = MovieDetail(
    id = id,
    title = title,
    tagline = tagline,
    overview = overview,
    posterPath = posterPath,
    backdropPath = backdropPath,
    releaseDate = releaseDate,
    voteAverage = voteAverage,
    voteCount = voteCount,
    runtime = runtime,
    status = status,
    genres = genres.map { Genre(it.id, it.name) },
    budget = budget,
    revenue = revenue,
    originalLanguage = originalLanguage,
    popularity = popularity,
    homepage = homepage
)

fun GenreDto.toDomain(): Genre = Genre(id = id, name = name)

// ── DTO -> Entity ─────────────────────────────────────────────────────────────

fun MovieDto.toEntity(): MovieEntity = MovieEntity(
    id = id,
    title = title,
    overview = overview,
    posterPath = posterPath,
    backdropPath = backdropPath,
    releaseDate = releaseDate,
    voteAverage = voteAverage,
    voteCount = voteCount,
    genreIds = genreIds.joinToString(","),
    popularity = popularity,
    originalLanguage = originalLanguage
)

fun MovieDetailDto.toEntity(): MovieDetailEntity = MovieDetailEntity(
    id = id,
    title = title,
    tagline = tagline,
    overview = overview,
    posterPath = posterPath,
    backdropPath = backdropPath,
    releaseDate = releaseDate,
    voteAverage = voteAverage,
    voteCount = voteCount,
    runtime = runtime,
    status = status,
    genres = genres.joinToString(",") { "${it.id}:${it.name}" },
    budget = budget,
    revenue = revenue,
    originalLanguage = originalLanguage,
    popularity = popularity,
    homepage = homepage
)

fun GenreDto.toEntity(): GenreEntity = GenreEntity(id = id, name = name)

// ── Entity -> Domain ──────────────────────────────────────────────────────────

fun MovieEntity.toDomain(): Movie = Movie(
    id = id,
    title = title,
    overview = overview,
    posterPath = posterPath,
    backdropPath = backdropPath,
    releaseDate = releaseDate,
    voteAverage = voteAverage,
    voteCount = voteCount,
    genreIds = if (genreIds.isBlank()) emptyList()
    else genreIds.split(",").mapNotNull { it.trim().toIntOrNull() },
    popularity = popularity,
    originalLanguage = originalLanguage
)

fun MovieDetailEntity.toDomain(): MovieDetail = MovieDetail(
    id = id,
    title = title,
    tagline = tagline,
    overview = overview,
    posterPath = posterPath,
    backdropPath = backdropPath,
    releaseDate = releaseDate,
    voteAverage = voteAverage,
    voteCount = voteCount,
    runtime = runtime,
    status = status,
    genres = if (genres.isBlank()) emptyList()
    else genres.split(",").mapNotNull { part ->
        val split = part.split(":")
        if (split.size == 2) Genre(split[0].toIntOrNull() ?: return@mapNotNull null, split[1])
        else null
    },
    budget = budget,
    revenue = revenue,
    originalLanguage = originalLanguage,
    popularity = popularity,
    homepage = homepage
)

fun GenreEntity.toDomain(): Genre = Genre(id = id, name = name)