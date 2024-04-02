import com.prasan.qrscanner.contracts.CameraManager
import org.bytedeco.javacv.*
import java.nio.ByteBuffer

class DesktopCameraManager : CameraManager {
    private var camera: FrameGrabber? = null
    private val defaultCameraIndex = 0

    override suspend fun openCamera(): Boolean {
        return try {
            camera = OpenCVFrameGrabber(defaultCameraIndex)
            camera?.start()
            true
        } catch (e: Exception) {
            // Log the error appropriately
            false
        }
    }

    override suspend fun closeCamera() {
        camera?.stop()
        camera?.release()
        camera = null
    }

    override suspend fun captureImage(): ByteArray? {
        try {
            val frame = camera?.grab() ?: return null
            val mat = OpenCVFrameConverter.ToMat().convert(frame)

            val bufferSize = mat.size().area() * mat.channels()
            val buffer = ByteBuffer.allocate(bufferSize.toInt())
            mat.data().asBuffer().get(buffer.array())

            return buffer.array()
        } catch (e: Exception) {
            // Log the error appropriately
            return null
        }
    }
}
