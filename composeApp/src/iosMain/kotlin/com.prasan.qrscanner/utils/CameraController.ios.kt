package com.prasan.qrscanner.utils
//import com.prasan.qrscanner.cinterop.CameraControllerImpl

actual class CameraController {
    //private val controllerImpl = CameraControllerImpl()
    actual fun startCamera() {
    }

    actual fun scanBarcode(onBarcodeScanned: (String) -> Unit) {
    }
}