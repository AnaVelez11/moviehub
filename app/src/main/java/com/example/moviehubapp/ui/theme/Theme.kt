package com.example.moviehubapp.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val DarkBackground = Color(0xFF0A0A0F)
val DarkSurface = Color(0xFF13131A)
val DarkCard = Color(0xFF1C1C27)
val AccentGold = Color(0xFFFFB800)
val AccentGoldDim = Color(0xFFCC9200)
val TextPrimary = Color(0xFFF0F0F0)
val TextSecondary = Color(0xFF9E9E9E)
val OnlineGreen = Color(0xFF4CAF50)
val OfflineRed = Color(0xFFE53935)
val StarYellow = Color(0xFFFFD700)

private val MovieHubColorScheme = darkColorScheme(
    primary = AccentGold,
    onPrimary = Color(0xFF1A1200),
    primaryContainer = Color(0xFF3D2E00),
    secondary = Color(0xFF9E8A00),
    background = DarkBackground,
    surface = DarkSurface,
    surfaceVariant = DarkCard,
    onBackground = TextPrimary,
    onSurface = TextPrimary,
    onSurfaceVariant = TextSecondary,
    error = OfflineRed,
)

@Composable
fun MovieHubTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = MovieHubColorScheme,
        content = content
    )
}