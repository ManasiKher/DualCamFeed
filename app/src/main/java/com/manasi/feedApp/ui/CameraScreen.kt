package com.manasi.feedApp.ui

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.video.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Executor

@Composable
fun CameraScreen(navController: NavController) {
    val context = LocalContext.current
    val lifecycleOwner = LocalContext.current as LifecycleOwner
    val cameraExecutor = remember { ContextCompat.getMainExecutor(context) }

    Column(Modifier
        .fillMaxSize()
        .padding(12.dp)) {

        Text("Back Camera", style = MaterialTheme.typography.titleMedium)
        CameraPreview(CameraSelector.DEFAULT_BACK_CAMERA, lifecycleOwner)

        Spacer(modifier = Modifier.height(16.dp))

        Text("Front Camera", style = MaterialTheme.typography.titleMedium)
        CameraPreview(CameraSelector.DEFAULT_FRONT_CAMERA, lifecycleOwner)

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                recordVideo(context, lifecycleOwner, cameraExecutor) { uri ->
                    Toast.makeText(context, "Saved: $uri", Toast.LENGTH_SHORT).show()
                    navController.navigate("gallery")
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Record 15s Dual Video")
        }
    }
}

fun recordVideo(
    context: Context,
    lifecycleOwner: LifecycleOwner,
    cameraExecutor: Executor,
    onComplete: (String) -> Unit
) {
    val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
    cameraProviderFuture.addListener({
        val cameraProvider = cameraProviderFuture.get()

        val recorder = Recorder.Builder()
            .setQualitySelector(QualitySelector.from(Quality.HD))
            .build()
        val videoCapture = VideoCapture.withOutput(recorder)

        val name = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US)
            .format(System.currentTimeMillis())

        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, name)
            put(MediaStore.MediaColumns.MIME_TYPE, "video/mp4")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(MediaStore.Video.Media.RELATIVE_PATH, "Movies/DualCameraApp")
            }
        }

        val outputOptions = MediaStoreOutputOptions.Builder(
            context.contentResolver,
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI
        ).setContentValues(contentValues).build()

        try {
            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(
                lifecycleOwner,
                CameraSelector.DEFAULT_BACK_CAMERA,
                videoCapture
            )

            val mediaRecording = if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.RECORD_AUDIO
                ) == android.content.pm.PackageManager.PERMISSION_GRANTED
            ) {
                videoCapture.output
                    .prepareRecording(context, outputOptions)
                    .withAudioEnabled()
            } else {
                videoCapture.output
                    .prepareRecording(context, outputOptions)
            }

            val recording = mediaRecording.start(cameraExecutor) { event ->
                if (event is VideoRecordEvent.Finalize) {
                    if (event.hasError()) {
                        Log.e("Record", "Error: ${event.error}")
                    } else {
                        onComplete(event.outputResults.outputUri.toString())
                    }
                }
            }



            Timer().schedule(object : TimerTask() {
                override fun run() {
                    recording.stop()
                }
            }, 15_000)

        } catch (e: Exception) {
            Log.e("recordVideo", "Recording failed", e)
        }
    }, cameraExecutor)
}
@Composable
fun CameraPreview(cameraSelector: CameraSelector, lifecycleOwner: LifecycleOwner) {
    val context = LocalContext.current
    val previewView = remember { androidx.camera.view.PreviewView(context) }

    LaunchedEffect(cameraSelector) {
        val cameraProvider = ProcessCameraProvider.getInstance(context).get()
        val preview = Preview.Builder().build().also {
            it.setSurfaceProvider(previewView.surfaceProvider)
        }

        try {
            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(
                lifecycleOwner,
                cameraSelector,
                preview
            )
        } catch (e: Exception) {
            Log.e("CameraPreview", "Binding failed", e)
        }
    }

    AndroidView(factory = { previewView },
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp))
}

