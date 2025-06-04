package com.silviomamani.pexelsapp.ui.screens.pexelsvideodetail

import android.content.Intent
import android.media.browse.MediaBrowser
import android.net.Uri
import android.widget.Toast
import android.widget.VideoView
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import coil.compose.AsyncImage
import androidx.media3.ui.PlayerView
@Composable
fun PexelsVideoDetailScreen(
    videoId: Int,
    vm: PexelsVideoDetailViewModel = viewModel()
) {
    val state = vm.uiState
    val context = LocalContext.current

    LaunchedEffect(videoId) {
        vm.fetchVideoById(videoId)
    }

    when {
        state.isLoading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        state.error != null -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Error: ${state.error}")
            }
        }

        state.video != null -> {
            val video = state.video
            val videoFile = video.videoFiles.firstOrNull { it.link.isNotEmpty() }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFEAF6E9)) // verde claro
                    .padding(horizontal = 16.dp, vertical = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Atrás", modifier = Modifier.clickable {
                        // manejar navegación hacia atrás si lo deseas
                    })
                    Icon(Icons.Default.FavoriteBorder, contentDescription = "Favorito")
                }

                Spacer(modifier = Modifier.height(24.dp))

                if (videoFile != null) {
                    val exoPlayer = remember(videoFile.link) {
                        ExoPlayer.Builder(context).build().apply {
                            val mediaItem = MediaItem.fromUri(videoFile.link)
                            setMediaItem(mediaItem)
                            prepare()
                            playWhenReady = true
                        }
                    }

                    AndroidView(
                        factory = {
                            PlayerView(it).apply {
                                player = exoPlayer
                                useController = true
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(240.dp)
                            .clip(RoundedCornerShape(12.dp))
                    )

                    DisposableEffect(Unit) {
                        onDispose {
                            exoPlayer.release()
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Info del video
                    Text(
                        text = video.user.name,
                        style = MaterialTheme.typography.titleMedium
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(text = "Duración: ${video.duration} segundos")
                    Text(text = "Resolución: ${video.width}x${video.height}")

                    Spacer(modifier = Modifier.height(24.dp))

                    // Botón descargar
                    Button(
                        onClick = {
                            val uri = Uri.parse(videoFile.link)
                            val intent = Intent(Intent.ACTION_VIEW, uri)
                            context.startActivity(intent)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9ACD32))
                    ) {
                        Text("Descargar", color = Color.White)
                    }
                } else {
                    Text("No se encontró un archivo de video disponible.")
                }
            }
        }
    }
}
