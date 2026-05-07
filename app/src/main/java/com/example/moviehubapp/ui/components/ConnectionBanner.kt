package com.example.moviehubapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.Box
import com.example.moviehubapp.ui.theme.OfflineRed
import com.example.moviehubapp.ui.theme.OnlineGreen

@Composable
fun ConnectionBanner(isOnline: Boolean) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                if (isOnline) OnlineGreen
                else OfflineRed
            )
            .padding(12.dp)
    ) {
        Text(
            text = if (isOnline)
                "🟢 ONLINE"
            else
                "🔴 OFFLINE",
            color = Color.White
        )
    }
}