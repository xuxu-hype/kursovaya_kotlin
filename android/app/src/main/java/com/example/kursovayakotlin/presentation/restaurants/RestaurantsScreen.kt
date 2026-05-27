package com.example.kursovayakotlin.presentation.restaurants

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RestaurantsScreen(
    onRestaurantClick: (String) -> Unit,
    viewModel: RestaurantsViewModel = hiltViewModel(),
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value

    Scaffold(
        topBar = { TopAppBar(title = { Text("Restaurants") }) },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.padding(16.dp))
            }
            uiState.errorMessage?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(horizontal = 16.dp),
                )
                Button(
                    onClick = viewModel::refresh,
                    modifier = Modifier.padding(16.dp),
                ) {
                    Text("Retry")
                }
            }
            if (!uiState.isLoading && uiState.restaurants.isEmpty()) {
                Text("No restaurants yet.", modifier = Modifier.padding(16.dp))
            }
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(uiState.restaurants, key = { it.id }) { restaurant ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onRestaurantClick(restaurant.id) }
                            .padding(16.dp),
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = restaurant.name,
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.weight(1f),
                            )
                            Text(if (restaurant.isOpen) "Open" else "Closed")
                        }
                        restaurant.description?.let {
                            Text(text = it, modifier = Modifier.padding(top = 4.dp))
                        }
                        restaurant.address?.let {
                            Text(text = it, modifier = Modifier.padding(top = 4.dp))
                        }
                        Text(text = "Rating: ${restaurant.rating}", modifier = Modifier.padding(top = 4.dp))
                    }
                    HorizontalDivider()
                }
            }
        }
    }
}
