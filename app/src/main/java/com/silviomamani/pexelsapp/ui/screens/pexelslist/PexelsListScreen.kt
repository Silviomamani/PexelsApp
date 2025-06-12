package com.silviomamani.pexelsapp.ui.screens.pexelslist

import android.util.Log
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.silviomamani.pexelsapp.photos.Fotos
import com.silviomamani.pexelsapp.photos.Videos
import com.silviomamani.pexelsapp.ui.screens.Screens
//import com.silviomamani.pexelsapp.getPexelsList
import com.silviomamani.pexelsapp.ui.screens.commons.PexelsUIList
import com.silviomamani.pexelsapp.ui.screens.commons.SearchType
import com.silviomamani.pexelsapp.ui.theme.PexelsAppTheme

@Composable
fun PexelsListScreen(
    modifier: Modifier = Modifier,
    vm: PexelsListScreenViewModel = viewModel(),
    navController: NavHostController,
    onLogoutClick: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
    ) {
        // Título de búsqueda
        Text(
            text = "Buscar ${vm.uiState.searchType.name.lowercase().replaceFirstChar { it.uppercase() }}",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Controles de búsqueda
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box {
                Button(onClick = { expanded = true }) {
                    Text(vm.uiState.searchType.name)
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    DropdownMenuItem(text = { Text("FOTOS") }, onClick = {
                        vm.onSearchTypeChange(SearchType.PHOTOS)
                        expanded = false
                    })
                    DropdownMenuItem(text = { Text("VIDEOS") }, onClick = {
                        vm.onSearchTypeChange(SearchType.VIDEOS)
                        expanded = false
                    })
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            TextField(
                value = vm.uiState.searchQuery,
                onValueChange = { vm.searchChange(it) },
                modifier = Modifier.weight(1f),
                label = { Text("Buscar ${vm.uiState.searchType.name.lowercase()}") },
                singleLine = true
            )

            Spacer(modifier = Modifier.width(12.dp))

            Button(onClick = { vm.fetchResults() }) {
                Text("Buscar")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Contenido principal
        if (vm.uiState.searchQuery.isEmpty() || (!vm.uiState.hasSearched && vm.combinedList.isEmpty())) {
            // Mostrar sugerencias cuando no hay búsqueda o no se ha buscado
            SuggestionsSection(
                searchType = vm.uiState.searchType,
                onSuggestionClick = { suggestion ->
                    vm.searchChange(suggestion)
                    vm.fetchResults()
                }
            )
        } else {
            // Mostrar resultados de búsqueda
            PexelsUIList(
                pexelsList = vm.combinedList,
                modifier = Modifier.fillMaxSize(),
                onClick = { id, isVideo ->
                    if (isVideo) {
                        navController.navigate(Screens.PexelsVideosDetail.route + "/$id")
                    } else {
                        navController.navigate(Screens.PexelsDetail.route + "/$id")
                    }
                }
            )
        }
    }
}

@Composable
fun SuggestionsSection(
    searchType: SearchType,
    onSuggestionClick: (String) -> Unit
) {
    Column {
        Text(
            text = "Sugerencias",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Grid de sugerencias como chips
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(getSuggestions(searchType)) { suggestion ->
                SuggestionChip(
                    text = suggestion,
                    onClick = { onSuggestionClick(suggestion) }
                )
            }
        }
    }
}
