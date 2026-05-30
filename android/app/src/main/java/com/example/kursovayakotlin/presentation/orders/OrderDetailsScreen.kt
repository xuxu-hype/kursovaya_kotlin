package com.example.kursovayakotlin.presentation.orders

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderDetailsScreen(
    onBackClick: () -> Unit,
    viewModel: OrderDetailsViewModel = hiltViewModel(),
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Order Details") },
                navigationIcon = {
                    TextButton(onClick = onBackClick) {
                        Text("Back")
                    }
                },
            )
        },
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
                    modifier = Modifier.padding(16.dp),
                )
                Button(
                    onClick = viewModel::loadOrder,
                    modifier = Modifier.padding(horizontal = 16.dp),
                ) {
                    Text("Retry")
                }
            }
            uiState.order?.let { order ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = "Order #${order.id}", style = MaterialTheme.typography.titleMedium)
                        Text(text = "Status: ${order.status}", modifier = Modifier.padding(top = 8.dp))
                        Text(text = "Address: ${order.deliveryAddress}", modifier = Modifier.padding(top = 4.dp))
                        Text(text = "Total: ${formatMoney(order.totalCents)}", modifier = Modifier.padding(top = 4.dp))
                        Text(text = "Created: ${order.createdAt}", modifier = Modifier.padding(top = 4.dp))
                    }
                }
                Text(
                    text = "Items",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                )
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    if (order.items.isEmpty()) {
                        item {
                            Text(
                                text = "No items found for this order.",
                                modifier = Modifier.padding(16.dp),
                            )
                        }
                    }
                    items(order.items, key = { it.id }) { item ->
                        Card(modifier = Modifier.fillMaxWidth()) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(text = item.nameSnapshot, style = MaterialTheme.typography.titleSmall)
                                Text(
                                    text = "Quantity: ${item.quantity}",
                                    modifier = Modifier.padding(top = 8.dp),
                                )
                                Text(
                                    text = "Price: ${formatMoney(item.priceCents)}",
                                    modifier = Modifier.padding(top = 4.dp),
                                )
                                Text(
                                    text = "Line total: ${formatMoney(item.lineTotalCents)}",
                                    modifier = Modifier.padding(top = 4.dp),
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun formatMoney(cents: Int): String =
    "$" + "%.2f".format(cents / 100.0)
