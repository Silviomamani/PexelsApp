package com.silviomamani.pexelsapp.ui.screens.pexelslistvideo

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.silviomamani.pexelsapp.ui.screens.Screens
import com.silviomamani.pexelsapp.ui.screens.commons.PexelsUIListVideos

@Composable
fun PexelsListScreenVideo(
    modifier: Modifier = Modifier,
    vm: PexelsListScreenVideoViewModel = viewModel(),
    navController: NavHostController,
    onLogoutClick:  () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
    ) {
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
            text = "Listado de Videos",
            style = MaterialTheme.typography.titleLarge,
            modifier = modifier
        )
        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = vm.uiState.searchQuery,
                modifier = Modifier.weight(1f),
                label = { Text("Buscar Videos") },
                singleLine = true,
                onValueChange = { vm.searchChange(it) }
            )
            Spacer(modifier = Modifier.width(12.dp))
            Button(
                onClick = { vm.fetchVideos() }
            ) {
                Text("Buscar")
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        PexelsUIListVideos (vm.uiState.pexelsVideoList, modifier.fillMaxSize(), onClick = { id ->
            navController.navigate(Screens.PexelsVideosDetail.route + "/${id}")
        })
    }
}
