package com.silviomamani.pexelsapp.ui.screens.pexelsvideodetail

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.silviomamani.pexelsapp.photos.Videos

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PexelsVideoUIItemDetail(
    video: Videos,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val videoFile = video.videoFiles.firstOrNull { it.link.isNotEmpty() }

    Scaffold(
        containerColor = Color(0xFFEAF6E9),
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Atrás",
                            tint = Color(0xFF4A6741)
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { /* lógica de favoritos */ }) {
                        Icon(
                            Icons.Default.FavoriteBorder,
                            contentDescription = "Favorito",
                            tint = Color(0xFF4A6741)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFEAF6E9)
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(Color(0xFFEAF6E9))
                .padding(horizontal = 16.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (videoFile != null) {
                // Video Player
                val exoPlayer = remember(videoFile.link) {
                    ExoPlayer.Builder(context).build().apply {
                        val mediaItem = MediaItem.fromUri(videoFile.link)
                        setMediaItem(mediaItem)
                        prepare()
                        playWhenReady = true
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp)
                        .clip(RoundedCornerShape(12.dp))
                ) {
                    AndroidView(
                        factory = {
                            PlayerView(it).apply {
                                player = exoPlayer
                                useController = true
                            }
                        },
                        modifier = Modifier.fillMaxSize()
                    )
                }

                DisposableEffect(Unit) {
                    onDispose {
                        exoPlayer.release()
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Información del video
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White.copy(alpha = 0.8f)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = video.user.name,
                            style = MaterialTheme.typography.titleMedium,
                            color = Color(0xFF4A6741),
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Duración: ${video.duration}s",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color(0xFF6B7B63)
                            )
                            Text(
                                text = "${video.width}x${video.height}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color(0xFF6B7B63)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Botón de descarga
                Button(
                    onClick = {
                        val uri = Uri.parse(videoFile.link)
                        val intent = Intent(Intent.ACTION_VIEW, uri)
                        context.startActivity(intent)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF7BA839),
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(16.dp),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                ) {
                    Text(
                        "Descargar",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            } else {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "No se encontró un archivo de video disponible.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color(0xFF4A6741)
                    )
                }
            }
        }
    }
}