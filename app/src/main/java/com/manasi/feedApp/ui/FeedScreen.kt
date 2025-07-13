package com.manasi.feedApp.ui

import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView

@Composable
fun FeedScreen() {
    val context = LocalContext.current
    val videoUrls = listOf(
        "https://www.w3schools.com/html/mov_bbb.mp4",
        "https://samplelib.com/lib/preview/mp4/sample-5s.mp4"
    )

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(videoUrls) { videoUrl ->
            VideoItem(videoUrl = videoUrl)
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun VideoItem(videoUrl: String) {
    val context = LocalContext.current
    val player = remember {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri(Uri.parse(videoUrl)))
            prepare()
            playWhenReady = true
            volume = 0f // muted autoplay
        }
    }

    DisposableEffect(Unit) {
        onDispose { player.release() }
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
