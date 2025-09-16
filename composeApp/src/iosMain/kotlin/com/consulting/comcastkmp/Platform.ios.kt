package com.consulting.comcastkmp

import platform.UIKit.UIDevice
import platform.UIKit.UIApplication
import platform.UIKit.UIInterfaceOrientationLandscapeLeft
import platform.UIKit.UIInterfaceOrientationLandscapeRight

class IOSPlatform: Platform {
    override val name: String = UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
}

actual fun getPlatform(): Platform = IOSPlatform()

@kotlinx.cinterop.ExperimentalForeignApi
@androidx.compose.runtime.Composable
actual fun isLandscape(): Boolean {
    val windowScene = UIApplication.sharedApplication.keyWindow?.windowScene
    val orientation = windowScene?.interfaceOrientation
    return orientation == UIInterfaceOrientationLandscapeLeft || orientation == UIInterfaceOrientationLandscapeRight
}