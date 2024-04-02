import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import com.prasan.qrscanner.presentation.QRScanner
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    MaterialTheme {
        QRScanner()
    }
}