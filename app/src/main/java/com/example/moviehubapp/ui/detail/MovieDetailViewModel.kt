package com.example.moviehubapp.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviehubapp.domain.model.MovieDetail
import com.example.moviehubapp.domain.model.Resource
import com.example.moviehubapp.domain.repository.MovieRepository
import com.example.moviehubapp.util.ConnectivityObserver
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MovieDetailUiState(
    val movie: MovieDetail? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class MovieDetailViewModel @Inject constructor(
    private val repository: MovieRepository,
    connectivityObserver: ConnectivityObserver
) : ViewModel() {

    private val _uiState = MutableStateFlow(MovieDetailUiState())
    val uiState: StateFlow<MovieDetailUiState> = _uiState

    val isOnline: StateFlow<Boolean> = connectivityObserver.isOnline
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)

    fun loadMovieDetail(movieId: Int) {
        viewModelScope.launch {
            _uiState.value = MovieDetailUiState(isLoading = true)
            when (val result = repository.getMovieDetail(movieId)) {
                is Resource.Success -> _uiState.value = MovieDetailUiState(movie = result.data)
                is Resource.Error -> _uiState.value = MovieDetailUiState(error = result.message)
                is Resource.Loading -> Unit
            }
        }
    }
}