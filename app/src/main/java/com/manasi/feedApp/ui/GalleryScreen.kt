package com.manasi.feedApp.ui

import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import coil.compose.rememberAsyncImagePainter

@Composable
fun GalleryScreen() {
    val context = LocalContext.current
    val videos = remember { getVideoList(context) }
    var selectedVideoUri by remember { mutableStateOf<Uri?>(null) }

    Column(Modifier.fillMaxSize().padding(8.dp)) {
        Text("Saved Videos", style = MaterialTheme.typography.headlineSmall)

        Spacer(modifier = Modifier.height(8.dp))

        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 150.dp),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(8.dp)
        ) {
            items(videos) { videoUri ->
                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .clickable { selectedVideoUri = videoUri }
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(videoUri),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }

        // Inline video player
        selectedVideoUri?.let { uri ->
            Spacer(modifier = Modifier.height(12.dp))
            Text("Now Playing", style = MaterialTheme.typography.titleMedium)
            VideoPlayer(uri)
        }
    }
}

@Composable
fun VideoPlayer(videoUri: Uri) {
    val context = LocalContext.current
    val player = remember {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri(videoUri))
            prepare()
            playWhenReady = true
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            player.release()
        }
    }

    AndroidView(
        factory = {
            PlayerView(context).apply {
                useController = true
                this.player = player
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
    )
}

fun getVideoList(context: Context): List<Uri> {
    val videoList = mutableListOf<Uri>()
    val projection = arrayOf(MediaStore.Video.Media._ID)
    val selection = "${MediaStore.Video.Media.RELATIVE_PATH} LIKE ?"
    val selectionArgs = arrayOf("%Movies/DualCameraApp%")
    val sortOrder = "${MediaStore.Video.Media.DATE_ADDED} DESC"

    val query = context.contentResolver.query(
        MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
        projection,
        selection,
        selectionArgs,
        sortOrder
    )

    query?.use { cursor ->
        val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID)
        while (cursor.moveToNext()) {
            val id = cursor.getLong(idColumn)
            val contentUri = ContentUris.withAppendedId(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI, id
            )
            videoList.add(contentUri)
        }
    }

    return videoList
}


