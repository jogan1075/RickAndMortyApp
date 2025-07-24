package com.jmc.home.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.jmc.home.presentation.HomeScreenEvents
import com.jmc.home.presentation.HomeViewModel

@Composable
fun HomeScreenView(viewModel: HomeViewModel = hiltViewModel(),navController: NavController) {
    val state = viewModel.state

    val listState = rememberLazyListState()

    LaunchedEffect(listState) {
        snapshotFlow {
            listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index
        }.collect { index ->
            if (index != null &&
                index >= state.characters.size - 3 &&
                !state.endReached &&
                !state.isPaginating
            ) {
                viewModel.onEvents(HomeScreenEvents.LoadNextPage)
            }
        }
    }

    when {
        state.isLoading && state.characters.isEmpty() -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        state.error != null && state.characters.isEmpty() -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "Error: ${state.error}")
            }
        }

        else -> {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                topBar = {},
                bottomBar = {})
            { paddingValues ->
                Column(modifier = Modifier.padding(paddingValues)) {
                    OutlinedTextField(
                        value = state.query,
                        onValueChange = { query ->
                            viewModel.onEvents(HomeScreenEvents.OnSearchQueryChange(query))
                        },
                        placeholder = { Text(text = "Search...") },
                        maxLines = 1,
                        singleLine = true,
                        trailingIcon = {
                            Image(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search Icon",
                                colorFilter = ColorFilter.tint(Color.Gray)
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    )

                    LazyColumn(
                        state = listState,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(state.characters.size) { index ->
                            val character = state.characters[index]
                            ListContent(
                                results = character,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp)
                                    .clickable {
                                        navController.navigate("detail/${character.id}")
                                    }
                            )
                            Divider()
                        }

                        if (state.isLoading && state.characters.isNotEmpty()) {
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator()
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
