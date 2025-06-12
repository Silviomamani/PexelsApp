package com.silviomamani.pexelsapp.ui.screens.commons

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.silviomamani.pexelsapp.photos.Videos


@Composable
fun VideoCard(
    video: Videos,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp)
    ) {
        Box {
            AsyncImage(
                model = video.image,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentScale = ContentScale.Crop
            )
            Icon(
                imageVector = Icons.Default.PlayCircle,
                contentDescription = "Play",
                tint = Color.White,
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(48.dp)
            )
        }
    }
}