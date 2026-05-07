package com.example.moviehubapp.ui.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.moviehubapp.domain.model.Genre
import com.example.moviehubapp.ui.theme.AccentGold
import com.example.moviehubapp.ui.theme.DarkCard
import com.example.moviehubapp.ui.theme.TextSecondary
import androidx.compose.ui.graphics.Color

@Composable
fun GenreFilterChips(
    genres: List<Genre>,
    selectedGenreId: Int?,
    onGenreSelected: (Genre?) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Chip "Todos"
        FilterChip(
            selected = selectedGenreId == null,
            onClick = { onGenreSelected(null) },
            label = { Text("Todos") },
            colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = AccentGold,
                selectedLabelColor = Color(0xFF1A1200),
                containerColor = DarkCard,
                labelColor = TextSecondary
            )
        )

        genres.forEach { genre ->
            FilterChip(
                selected = selectedGenreId == genre.id,
                onClick = { onGenreSelected(genre) },
                label = { Text(genre.name) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = AccentGold,
                    selectedLabelColor = Color(0xFF1A1200),
                    containerColor = DarkCard,
                    labelColor = TextSecondary
                )
            )
        }
    }
}