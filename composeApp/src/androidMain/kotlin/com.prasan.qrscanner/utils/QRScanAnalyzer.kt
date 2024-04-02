package com.prasan.qrscanner.utils

import android.util.Log
import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage

@OptIn(ExperimentalGetImage::class)
class QRScanAnalyzer (val onCodeScanned: (String) -> Unit) : ImageAnalysis.Analyzer {

    private val options = BarcodeScannerOptions.Builder().build()

    private val scanner = BarcodeScanning.getClient(options)

    override fun analyze(imageProxy: ImageProxy) {
        imageProxy.image?.let {
            val imageValue = InputImage.fromMediaImage(it, imageProxy.imageInfo.rotationDegrees)
            scanner.process(imageValue)
                .addOnSuccessListener { barcodes ->
                    barcodes.forEach { barcode ->
                        Log.d("QRScanAnalyzer", "Barcode value: ${barcode.rawValue}")
                        onCodeScanned(barcode.rawValue ?: throw NullPointerException("Barcode value is null"))
                    }
                }
                .addOnFailureListener { failure ->
                    failure.printStackTrace()
                }
                .addOnCompleteListener {
                    imageProxy.close()
                }
        }
    }
}
