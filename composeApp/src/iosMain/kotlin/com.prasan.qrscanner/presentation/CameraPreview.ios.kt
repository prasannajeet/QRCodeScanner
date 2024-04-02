package com.prasan.qrscanner.presentation

import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.interop.UIKitView
import androidx.compose.ui.unit.dp
import kotlinx.cinterop.ExperimentalForeignApi
import platform.UIKit.UIView

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun CameraPreview(visible: Boolean, onCameraError: () -> Unit,  onCodeScanner: (String) -> Unit) {
    /*val cameraPreview: UIView = createCameraPreview()
    UIKitView(factory = { cameraPreview }, modifier = Modifier.size(300.dp),)*/

    Text("Not available on iOS")
}
