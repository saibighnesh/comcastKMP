package com.consulting.comcastkmp

import android.os.Build
import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration

class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
}

actual fun getPlatform(): Platform = AndroidPlatform()

@Composable
actual fun isLandscape(): Boolean {
    val cfg = LocalConfiguration.current
    return cfg.orientation == Configuration.ORIENTATION_LANDSCAPE
}