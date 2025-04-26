package org.contoso.birthdayapp.ui.components

import android.net.Uri
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import org.contoso.birthdayapp.R

@Composable
fun VideoPlayer(modifier: Modifier = Modifier) {
    val currentContext = LocalContext.current
    val videoUri = Uri.parse("android.resource://${currentContext.packageName}/${R.raw.hey_are_you_okay}")

    val exoPlayer = remember {
        ExoPlayer.Builder(currentContext).build().apply {
            val mediaItem = MediaItem.fromUri(videoUri)
            setMediaItem(mediaItem)
            prepare()
            playWhenReady = true
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            exoPlayer.release()
        }
    }

    AndroidView(
        factory = { context ->
            PlayerView(context).apply {
                player = exoPlayer
            }
        },
        modifier = modifier.fillMaxSize()
    )
}