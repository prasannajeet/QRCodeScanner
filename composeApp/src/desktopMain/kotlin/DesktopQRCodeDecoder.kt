import com.google.zxing.BinaryBitmap
import com.google.zxing.MultiFormatReader
import com.google.zxing.NotFoundException
import com.google.zxing.RGBLuminanceSource
import com.google.zxing.common.HybridBinarizer
import com.prasan.qrscanner.contracts.QRCodeDecoder
import org.bytedeco.javacpp.BytePointer
import org.bytedeco.javacv.FFmpegFrameGrabber
import org.bytedeco.javacv.Frame
import org.bytedeco.javacv.OpenCVFrameConverter
import org.bytedeco.opencv.global.opencv_imgcodecs
import org.bytedeco.opencv.opencv_core.Mat
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import java.nio.ByteBuffer

class DesktopQRCodeDecoder : QRCodeDecoder {

    override suspend fun decode(imageData: ByteArray): String? {
        val mat = OpenCVFrameConverter.ToMat().convert(convertImageToFrame(imageData))
        val bufferedImage = matToBufferedImage(mat)

        val luminanceSource = RGBLuminanceSource(
            bufferedImage.width,
            bufferedImage.height,
            bufferedImage.getRGB(0, 0, bufferedImage.width, bufferedImage.height, null, 0, bufferedImage.width)
        )
        val binaryBitmap = BinaryBitmap(HybridBinarizer(luminanceSource))

        return try {
            val result = MultiFormatReader().decode(binaryBitmap)
            result.text
        } catch (e: NotFoundException) {
            null
        }
    }

    // Helper function to convert OpenCV Mat to BufferedImage
    private fun matToBufferedImage(mat: Mat): BufferedImage {
        val imageType = if (mat.channels() == 1) BufferedImage.TYPE_BYTE_GRAY else BufferedImage.TYPE_3BYTE_BGR
        val bufferSize = mat.channels() * mat.cols() * mat.rows()
        val buffer = ByteArray(bufferSize)
        mat.data().get(buffer)
        val resultImage = BufferedImage(mat.cols(), mat.rows(), imageType)
        resultImage.raster.setDataElements(0, 0, mat.cols(), mat.rows(), buffer)
        return resultImage
    }

    private fun convertByteArrayToFrame(imageData: ByteArray): Frame {
        // Convert ByteArray to ByteBuffer
        val byteBuffer = ByteBuffer.wrap(imageData)

        // Convert ByteBuffer to BytePointer
        val bytePointer = BytePointer(byteBuffer)

        // Convert BytePointer to Mat
        val mat = Mat(bytePointer)

        // Convert Mat to BufferedImage
        val converterToBufferedImage = OpenCVFrameConverter.ToMat()
        return converterToBufferedImage.convert(mat)
    }

    fun convertImageToFrame(image: ByteArray): Frame {
        // 1. Create an OpenCV Mat from the image data
        val mat = OpenCVFrameConverter.ToMat().convert(convertByteArrayToFrame(image))

        // 2. Create a temporary buffer to hold the encoded image
        val byteBuffer = ByteBuffer.wrap(image)
        opencv_imgcodecs.imencode(".jpg", mat, byteBuffer) // Encode as JPG

        // 3. Create a FrameGrabber from the image buffer
        val grabber = FFmpegFrameGrabber(ByteArrayInputStream(byteBuffer.array()))
        grabber.start()

        // 4. Grab and return the first frame
        val frame = grabber.grab()
        grabber.stop()
        return frame
    }
}