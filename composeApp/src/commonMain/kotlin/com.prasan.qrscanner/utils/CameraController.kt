package com.prasan.qrscanner.utils

expect class CameraController {
    fun startCamera()
    fun scanBarcode(onBarcodeScanned: (String) -> Unit)
}