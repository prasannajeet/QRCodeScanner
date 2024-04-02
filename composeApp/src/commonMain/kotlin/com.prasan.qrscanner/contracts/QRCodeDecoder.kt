package com.prasan.qrscanner.contracts

interface QRCodeDecoder {
    suspend fun decode(imageData: ByteArray): String?
}