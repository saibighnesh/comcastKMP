package com.consulting.comcastkmp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.jetbrains.compose.ui.tooling.preview.Preview
import com.consulting.comcastkmp.data.AnimalsRepository
import com.consulting.comcastkmp.ui.AnimalsScreen

@Composable
@Preview
fun App() { AppTheme { AppContent() } }

@Composable
fun AppContent() {
    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.primaryContainer)
            .safeContentPadding()
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        val repo = AnimalsRepository(apiKey = "XB2xaIrruYztlhnd+h3yCw==bvpvbo1A33T3ptEn")
        AnimalsScreen(repository = repo, isLandscape = isLandscape())
    }
}