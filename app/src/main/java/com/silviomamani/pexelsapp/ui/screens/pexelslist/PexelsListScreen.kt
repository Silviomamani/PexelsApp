package com.silviomamani.pexelsapp.ui.screens.pexelslist

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
//import com.silviomamani.pexelsapp.getPexelsList
import com.silviomamani.pexelsapp.ui.screens.commons.PexelsUIList
import com.silviomamani.pexelsapp.ui.theme.PexelsAppTheme

@Composable
fun PexelsListScreen(
                     modifier: Modifier = Modifier,
                     vm: PexelsListScreenViewModel = viewModel()
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
    ){
    Text(
        text = "Listado de Fotos",
        style = MaterialTheme.typography.titleLarge,
        modifier = modifier
    )
        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ){
            TextField(
                value = vm.uiState.searchQuery,
                modifier = Modifier.weight(1f),
                label = {Text("Buscar Fotos o Videos")},
                singleLine = true,
                onValueChange = {vm.searchChange(it)}
            )
            Spacer(modifier = Modifier.height(12.dp))
            Button(
                onClick = {vm.fetchFotos()}
            ) {
                Text("Buscar")
            }
        }



        Spacer(modifier = Modifier.height(12.dp))
        PexelsUIList(vm.uiState.pexelsList,modifier.fillMaxSize())
    }

}