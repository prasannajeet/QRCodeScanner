import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.prasan.qrscanner.presentation.QRScanner
import java.awt.image.BufferedImage
import javax.imageio.ImageIO
import java.io.ByteArrayInputStream

fun main() = application {
    val cameraManager = DesktopCameraManager()
    val qrCodeDecoder = DesktopQRCodeDecoder()

    Window(onCloseRequest = ::exitApplication) {
        var scanResult by remember { mutableStateOf<String?>(null) }
        val fxPanel = remember { Pane() }
        val imageView = remember { ImageView() }
        fxPanel.children.add(imageView)

        QRScanner(cameraManager, qrCodeDecoder,
            onScanResult = { scanResult = it },
            onFrameUpdate = { imageData ->
                Platform.runLater {
                    val bis = ByteArrayInputStream(imageData)
                    val bImage2 = ImageIO.read(bis)
                    val fxImage = SwingFXUtils.toFXImage(bImage2 as BufferedImage, null)
                    imageView.image = fxImage
                }
            }
        )

        Column(Modifier.fillMaxSize()) {

            fxPanel
            scanResult?.let {
                Text("Scan Result: $it")
            }
        }

        //Initialize frame and add frameLabel
        DisposableEffect(Unit){
            val stage = Stage()
            stage.scene = Scene(fxPanel)
            stage.title = "QR Scanner"
            stage.show()

            onDispose {
                stage.close()
            }
        }
    }
}