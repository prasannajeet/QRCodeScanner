package com.prasan.qrscanner.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun QRScanner() {
    val scaffoldState = rememberScaffoldState()
    val scope= rememberCoroutineScope()
    Scaffold (
        modifier = Modifier.fillMaxSize(),
        scaffoldState
    ){
        Column(modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(modifier = Modifier.width(8.dp))
            CameraPreview(
                visible = true,
                onCameraError = {
                    scope.launch {
                        scaffoldState.snackbarHostState.showSnackbar(
                            "Camera Error"
                        )
                    }
                },
                onCodeScanner = {qrCode ->
                    scope.launch {
                        scaffoldState.snackbarHostState.showSnackbar(
                            "Code Result =>${qrCode}"
                        )
                    }
                }
            )
        }
    }
}