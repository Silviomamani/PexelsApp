package com.silviomamani.pexelsapp.ui.screens.favorites.componentsui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.silviomamani.pexelsapp.photos.Fotos

@Composable
fun FavoritePhotoItem(
    photo: Fotos,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box {
            Image(
                painter = rememberAsyncImagePainter(photo.src.medium),
                contentDescription = photo.alt,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(0.75f),
                contentScale = ContentScale.Crop
            )
        }
    }
}