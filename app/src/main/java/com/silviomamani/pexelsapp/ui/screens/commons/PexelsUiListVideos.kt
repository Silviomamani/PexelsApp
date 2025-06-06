package com.silviomamani.pexelsapp.ui.screens.commons

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.silviomamani.pexelsapp.photos.Videos

@Composable
fun PexelsUIListVideos(
    pexelsList: List<Videos>,
    modifier: Modifier = Modifier,
    onClick: (Int) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier.padding(8.dp)
    ) {
        items(pexelsList) { video ->
            PexelsVideoItem(video = video, onClick = { onClick(video.id) })
        }
    }
}