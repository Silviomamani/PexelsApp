package com.silviomamani.pexelsapp.ui.screens.commons

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.ui.unit.dp
import com.silviomamani.pexelsapp.photos.Fotos

@Composable
fun PexelsUIList(
    pexelsList: List<PexelsItem>,
    modifier: Modifier = Modifier,
    onClick: (Int, Boolean) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier.padding(8.dp)
    ) {
        items(pexelsList) { item ->
            when (item) {
                is PexelsItem.PhotoItem -> {
                    PexelsUiItem(fotos = item.foto, onClick = { onClick(item.foto.id, false) })
                }
                is PexelsItem.VideoItem -> {
                    PexelsVideoItem(video = item.video, onClick = { onClick(item.video.id, true) })
                }
            }
        }
    }
}