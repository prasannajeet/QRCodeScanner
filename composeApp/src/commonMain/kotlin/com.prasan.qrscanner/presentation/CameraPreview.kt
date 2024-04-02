package com.prasan.qrscanner.presentation

import androidx.compose.runtime.Composable

@Composable
expect fun CameraPreview(
    visible: Boolean,
    onCameraError: () -> Unit,
    onCodeScanner: (String) -> Unit,
)