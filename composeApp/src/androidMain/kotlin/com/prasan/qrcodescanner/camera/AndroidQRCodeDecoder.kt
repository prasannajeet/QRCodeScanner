package com.prasan.qrcodescanner.camera

import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import com.prasan.qrscanner.contracts.QRCodeDecoder
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume


class AndroidQRCodeDecoder : QRCodeDecoder {

    private val scanner = BarcodeScanning.getClient(
        BarcodeScannerOptions.Builder()
            .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
            .build()
    )

    override suspend fun decode(imageData: ByteArray): String? {
        val inputImage = InputImage.fromByteArray(imageData, 0, 0, 0, InputImage.IMAGE_FORMAT_NV21) // Adjust if needed
        return suspendCancellableCoroutine { cont ->
            scanner.process(inputImage)
                .addOnSuccessListener { barcodes ->
                    cont.resume(barcodes.firstOrNull()?.rawValue)
                }
                .addOnFailureListener {
                    cont.resume(null) 
                }
        }
    }
}
