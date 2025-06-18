package com.silviomamani.pexelsapp.ui.screens.favorites.componentsui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.silviomamani.pexelsapp.photos.Fotos

@Composable
fun FavoritesPhotoGrid(
    photos: List<Fotos>,
    onPhotoClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalItemSpacing = 8.dp,
        contentPadding = PaddingValues(16.dp),
        modifier = modifier
    ) {
        items(photos) { photo ->
            FavoritePhotoItem(
                photo = photo,
                onClick = { onPhotoClick(photo.id) }
            )
        }
    }
}
