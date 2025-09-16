package com.consulting.comcastkmp

import androidx.compose.runtime.Composable

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform

@Composable
expect fun isLandscape(): Boolean