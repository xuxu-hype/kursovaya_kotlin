package com.example.kursovayakotlin.presentation.orders

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
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
            }
            uiState.order?.let { order ->
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "Order #${order.id}", style = MaterialTheme.typography.titleMedium)
                    Text(text = "Status: ${order.status}")
                    Text(text = "Address: ${order.deliveryAddress}")
                    Text(text = "Total: ${formatMoney(order.totalCents)}")
                    Text(text = "Created: ${order.createdAt}")
                }
                LazyColumn(modifier = Modifier.fillMaxWidth()) {
                    items(order.items, key = { it.id }) { item ->
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(text = item.nameSnapshot, style = MaterialTheme.typography.titleSmall)
                            Text(text = "${item.quantity} x ${formatMoney(item.priceCents)}")
                            Text(text = "Line total: ${formatMoney(item.lineTotalCents)}")
                        }
                        HorizontalDivider()
                    }
                }
            }
        }
    }
}

private fun formatMoney(cents: Int): String =
    "$" + "%.2f".format(cents / 100.0)
