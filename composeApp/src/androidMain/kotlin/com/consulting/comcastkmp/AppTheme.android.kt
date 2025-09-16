package com.consulting.comcastkmp

import android.os.Build
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@Composable
actual fun AppTheme(content: @Composable () -> Unit) {
    val ctx = LocalContext.current
    val colors: ColorScheme = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        // Use dynamic colors on Android 12+
        dynamicLightColorScheme(ctx)
    } else {
        lightColorScheme()
    }
    MaterialTheme(colorScheme = colors, content = content)
}


