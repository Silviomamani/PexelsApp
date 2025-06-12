package com.silviomamani.pexelsapp.ui.screens.pexelslist


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

import androidx.compose.ui.graphics.Color

import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

import androidx.navigation.NavHostController

import com.silviomamani.pexelsapp.ui.screens.Screens

import com.silviomamani.pexelsapp.ui.screens.commons.PexelsUIList
import com.silviomamani.pexelsapp.ui.screens.commons.SearchType


@Composable
fun PexelsListScreen(
    modifier: Modifier = Modifier,
    vm: PexelsListScreenViewModel = viewModel(),
    navController: NavHostController,
) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE8F5E8))
    ) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = Color(0xFFE8F5E8),
            shadowElevation = 4.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Volver",
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { navController.popBackStack() },
                    tint = Color.Black
                )

                Spacer(modifier = Modifier.width(16.dp))

                Text(
                    text = "Buscar ${vm.uiState.searchType.name.lowercase().replaceFirstChar { it.uppercase() }}",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.Black,
                    modifier = Modifier.weight(1f)
                )

            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                Button(
                    onClick = { vm.onSearchTypeChange(SearchType.PHOTOS) },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (vm.uiState.searchType == SearchType.PHOTOS)
                            MaterialTheme.colorScheme.primary
                        else
                            Color.White,
                        contentColor = if (vm.uiState.searchType == SearchType.PHOTOS)
                            Color.White
                        else
                            Color.Black
                    ),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Text("FOTOS")
                }

                Button(
                    onClick = { vm.onSearchTypeChange(SearchType.VIDEOS) },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (vm.uiState.searchType == SearchType.VIDEOS)
                            MaterialTheme.colorScheme.primary
                        else
                            Color.White,
                        contentColor = if (vm.uiState.searchType == SearchType.VIDEOS)
                            Color.White
                        else
                            Color.Black
                    ),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Text("VIDEOS")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))


            TextField(
                value = vm.uiState.searchQuery,
                onValueChange = { vm.searchChange(it) },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Buscar ${vm.uiState.searchType.name.lowercase()}") },
                singleLine = true,
                shape = RoundedCornerShape(25.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )

            Spacer(modifier = Modifier.height(12.dp))


            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = { vm.fetchResults() },
                    modifier = Modifier.widthIn(min = 100.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    ),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Text(
                        text = "Buscar",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                Spacer(modifier = Modifier.weight(1f))
            }
        }


        if (vm.uiState.searchQuery.isEmpty() || (!vm.uiState.hasSearched && vm.combinedList.isEmpty())) {
            SuggestionsSection(
                searchType = vm.uiState.searchType,
                onSuggestionClick = { suggestion ->
                    vm.searchChange(suggestion)
                    vm.fetchResults()
                },
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        } else {

            PexelsUIList(
                pexelsList = vm.combinedList,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
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