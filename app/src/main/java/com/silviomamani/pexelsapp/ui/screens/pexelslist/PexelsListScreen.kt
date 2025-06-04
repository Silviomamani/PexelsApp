package com.silviomamani.pexelsapp.ui.screens.pexelslist

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
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
        // Usuario y logout
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Usuario: ${vm.uiState.username}",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.weight(1f)
            )
            Button(onClick = onLogoutClick) {
                Text("Logout")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Buscar ${vm.uiState.searchType.name.lowercase().replaceFirstChar { it.uppercase() }}",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Búsqueda con selector
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Selector (a la izquierda del buscador)
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

            // Input de búsqueda
            TextField(
                value = vm.uiState.searchQuery,
                onValueChange = { vm.searchChange(it) },
                modifier = Modifier.weight(1f),
                label = { Text("Buscar ${vm.uiState.searchType.name.lowercase()}") },
                singleLine = true
            )

            Spacer(modifier = Modifier.width(12.dp))

            // Botón de búsqueda
            Button(onClick = { vm.fetchResults() }) {
                Text("Buscar")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Lista combinada
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