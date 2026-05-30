package com.example.kursovayakotlin.presentation.cart

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    onOrderCreated: (String) -> Unit,
    viewModel: CartViewModel = hiltViewModel(),
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value

    LaunchedEffect(uiState.createdOrderId) {
        uiState.createdOrderId?.let { orderId ->
            onOrderCreated(orderId)
            viewModel.clearCreatedOrderId()
        }
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Cart") }) },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
        ) {
            if (uiState.items.isEmpty()) {
                Text("Cart is empty.", modifier = Modifier.padding(16.dp))
            }
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                items(uiState.items, key = { it.menuItemId }) { item ->
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                item.imageUrl
                                    ?.takeIf { it.isNotBlank() }
                                    ?.let { imageUrl ->
                                        AsyncImage(
                                            model = imageUrl,
                                            contentDescription = item.name,
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier
                                                .size(88.dp)
                                                .clip(RoundedCornerShape(10.dp)),
                                        )
                                    }
                                Column(
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(start = if (item.imageUrl.isNullOrBlank()) 0.dp else 12.dp),
                                ) {
                                    Text(text = item.name, style = MaterialTheme.typography.titleMedium)
                                    Text(text = formatMoney(item.priceCents), modifier = Modifier.padding(top = 6.dp))
                                }
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                ) {
                                    OutlinedButton(
                                        onClick = {
                                            viewModel.updateQuantity(item.menuItemId, item.quantity - 1)
                                        },
                                    ) {
                                        Text("-")
                                    }
                                    Text(
                                        text = item.quantity.toString(),
                                        style = MaterialTheme.typography.titleMedium,
                                    )
                                    OutlinedButton(
                                        onClick = {
                                            viewModel.updateQuantity(item.menuItemId, item.quantity + 1)
                                        },
                                    ) {
                                        Text("+")
                                    }
                                }
                            }
                            TextButton(onClick = { viewModel.removeItem(item.menuItemId) }) {
                                Text("Remove")
                            }
                        }
                    }
                }
            }
            uiState.errorMessage?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(horizontal = 16.dp),
                )
            }
            Text(
                text = "Total: ${formatMoney(uiState.totalCents)}",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(16.dp),
            )
            OutlinedTextField(
                value = uiState.deliveryAddress,
                onValueChange = viewModel::onDeliveryAddressChanged,
                label = { Text("Delivery address") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
            )
            Button(
                onClick = viewModel::createOrder,
                enabled = uiState.items.isNotEmpty() && uiState.deliveryAddress.isNotBlank() && !uiState.isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            ) {
                Text(if (uiState.isLoading) "Creating order..." else "Create order")
            }
            if (uiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.padding(horizontal = 16.dp))
            }
        }
    }
}

private fun formatMoney(cents: Int): String =
    "$" + "%.2f".format(cents / 100.0)
