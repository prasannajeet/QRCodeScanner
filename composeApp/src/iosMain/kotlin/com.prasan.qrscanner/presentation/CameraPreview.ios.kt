package com.prasan.qrscanner.presentation

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.cinterop.ExperimentalForeignApi
import platform.AVFoundation.*
import platform.UIKit.UIView

@Composable
actual fun CameraPreview(visible: Boolean, onCameraError: () -> Unit, onCodeScanner: (String) -> Unit) {
    Text("Camera Preview")
    Spacer(modifier = Modifier.height(16.dp))
    var previewLayer by remember { mutableStateOf<AVCaptureVideoPreviewLayer?>(null) }

    LaunchedEffect(key1 = Unit) {

        checkCameraPermission {
            if (!it) {
                onCameraError()
            } else {
                setupCameraPreview { layer ->
                    previewLayer = layer
                }
            }
        }
    }

    previewLayer?.let { layer ->
        DisposableEffect(key1 = layer) {
            val view = UIView().apply { this.layer.addSublayer(layer) }
            onDispose {
                view.layer.removeFromSuperlayer()
            }
        }
    }
}

private fun checkCameraPermission(callback: (Boolean) -> Unit) {
    val authorizationStatus = AVCaptureDevice.authorizationStatusForMediaType(AVMediaTypeVideo)
    when (authorizationStatus) {
        AVAuthorizationStatusAuthorized -> callback(true)
        AVAuthorizationStatusNotDetermined -> {
            AVCaptureDevice.requestAccessForMediaType(AVMediaTypeVideo) { granted ->
                // Handle permission request result (update state or UI if needed)
                callback(granted)
            }
        }
        else -> callback(false)
    }
}

@OptIn(ExperimentalForeignApi::class)
private fun setupCameraPreview(completion: (AVCaptureVideoPreviewLayer) -> Unit) {
    val captureSession = AVCaptureSession()
    val device = AVCaptureDevice.defaultDeviceWithMediaType(AVMediaTypeVideo)

    device?.let {
        val input = try {
            AVCaptureDeviceInput(device = it, error = null)
        } catch (e: Exception) {
            return
        }
        if (captureSession.canAddInput(input)) {
            captureSession.addInput(input)
        }

        val previewLayer = AVCaptureVideoPreviewLayer(session = captureSession).apply {
            videoGravity = AVLayerVideoGravityResizeAspectFill
        }
        completion(previewLayer)

        captureSession.startRunning()
    }
}