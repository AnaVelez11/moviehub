package com.example.moviehubapp.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviehubapp.domain.model.Genre
import com.example.moviehubapp.domain.model.Movie
import com.example.moviehubapp.domain.model.Resource
import com.example.moviehubapp.domain.repository.MovieRepository
import com.example.moviehubapp.util.ConnectivityObserver
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MovieListUiState(
    val movies: List<Movie> = emptyList(),
    val genres: List<Genre> = emptyList(),
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val error: String? = null,
    val searchQuery: String = "",
    val selectedGenreId: Int? = null,
    val selectedGenreName: String = "Todos",
    val currentPage: Int = 1,
    val canLoadMore: Boolean = true
)

@HiltViewModel
class MovieListViewModel @Inject constructor(
    private val repository: MovieRepository,
    connectivityObserver: ConnectivityObserver
) : ViewModel() {

    private val _uiState = MutableStateFlow(MovieListUiState())
    val uiState: StateFlow<MovieListUiState> = _uiState

    val isOnline: StateFlow<Boolean> = connectivityObserver.isOnline
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)

    private var searchJob: Job? = null

    init {
        loadGenres()
        loadMovies(reset = true)
    }

    // ── Cargar géneros ────────────────────────────────────────────────────────

    private fun loadGenres() {
        viewModelScope.launch {
            when (val result = repository.getGenres()) {
                is Resource.Success -> _uiState.value = _uiState.value.copy(genres = result.data)
                else -> Unit
            }
        }
    }

    // ── Cargar películas (popular / search / genre) ───────────────────────────

    fun loadMovies(reset: Boolean = false) {
        val state = _uiState.value
        if (state.isLoading || state.isLoadingMore) return
        if (!reset && !state.canLoadMore) return

        val page = if (reset) 1 else state.currentPage + 1

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = reset,
                isLoadingMore = !reset,
                error = null
            )

            val result = when {
                state.searchQuery.isNotBlank() ->
                    repository.searchMovies(state.searchQuery, page)
                state.selectedGenreId != null ->
                    repository.discoverMoviesByGenre(state.selectedGenreId, page)
                else ->
                    repository.getPopularMovies(page)
            }

            when (result) {
                is Resource.Success -> {
                    val newMovies = if (reset) result.data
                    else state.movies + result.data
                    _uiState.value = _uiState.value.copy(
                        movies = newMovies,
                        isLoading = false,
                        isLoadingMore = false,
                        currentPage = page,
                        canLoadMore = result.data.isNotEmpty()
                    )
                }
                is Resource.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isLoadingMore = false,
                        error = result.message
                    )
                }
                is Resource.Loading -> Unit
            }
        }
    }

    // ── Búsqueda con debounce ─────────────────────────────────────────────────

    fun onSearchQueryChange(query: String) {
        _uiState.value = _uiState.value.copy(
            searchQuery = query,
            selectedGenreId = null,
            selectedGenreName = "Todos"
        )
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(500)
            loadMovies(reset = true)
        }
    }

    // ── Filtro por género ─────────────────────────────────────────────────────

    fun onGenreSelected(genre: Genre?) {
        _uiState.value = _uiState.value.copy(
            selectedGenreId = genre?.id,
            selectedGenreName = genre?.name ?: "Todos",
            searchQuery = ""
        )
        loadMovies(reset = true)
    }

    // ── Scroll infinito ───────────────────────────────────────────────────────

    fun loadMoreIfNeeded(lastVisibleIndex: Int) {
        val state = _uiState.value
        val threshold = state.movies.size - 3
        if (lastVisibleIndex >= threshold) {
            loadMovies(reset = false)
        }
    }
}