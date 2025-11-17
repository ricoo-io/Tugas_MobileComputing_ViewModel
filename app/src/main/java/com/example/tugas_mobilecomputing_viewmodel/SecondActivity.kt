package com.example.tugas_mobilecomputing_viewmodel

import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.lifecycle.ViewModel


data class Item(
    val id: Int,
    var itemName: String,
    var itemQuantity: Int
)

class ItemViewModel : ViewModel() {

    var items = mutableStateListOf<Item>()
        private set

    private var itemCounter = 0

    fun addItem(): Int {
        val newItem = Item(
            id = ++itemCounter,
            itemName = "",
            itemQuantity = 0
        )
        items.add(newItem)
        return newItem.id
    }

    fun getItemById(id: Int): Item? {
        return items.find { it.id == id }
    }

    fun updateItem(updatedItem: Item) {
        val index = items.indexOfFirst { it.id == updatedItem.id }
        if (index >= 0) {
            items[index] = updatedItem
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SecondActivity(
    navController: NavController,
    username: String,
    viewModel: ItemViewModel
) {

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = { Text(text = "Welcome, $username") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    val newId = viewModel.addItem()
                    navController.navigate("add_edititem/$newId")
                }
            ) {
                Icon(Icons.Filled.Add, "Add item")
            }
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
        ) {

            Text("Your Items", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(12.dp))

            if (viewModel.items.isEmpty()) {

                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "No items yet.\nTap '+' to add one!",
                        color = Color.Gray,
                        textAlign = TextAlign.Center
                    )
                }

            } else {

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    viewModel.items.forEach { item ->

                        ElevatedCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .combinedClickable(
                                    onClick = { navController.navigate("add_edititem/${item.id}") },
                                    onLongClick = { }
                                ),
                            shape = RoundedCornerShape(14.dp)
                        ) {

                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            ) {

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = item.itemName,
                                        style = MaterialTheme.typography.titleLarge,
                                        color = MaterialTheme.colorScheme.onBackground
                                    )

                                    Text(
                                        text = "Qty: ${item.itemQuantity}",
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = MaterialTheme.colorScheme.onBackground
                                    )
                                }
                            }
                        }
                    }
                }

            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditItem(
    navController: NavController,
    itemId: Int,
    viewModel: ItemViewModel
) {

    val item = viewModel.getItemById(itemId) ?: return

    var itemName by remember { mutableStateOf(TextFieldValue(item.itemName)) }
    var itemQuantity by remember { mutableStateOf(item.itemQuantity.toString()) }

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = { Text(if (item.itemName.isEmpty()) "Add Item" else "Edit Item") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            modifier = Modifier.graphicsLayer(scaleX = -1f),
                            imageVector = Icons.Default.ExitToApp,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top
        ) {


            Spacer(Modifier.height(18.dp))

            OutlinedTextField(
                value = itemName,
                onValueChange = { itemName = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Item Name") },
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = itemQuantity,
                onValueChange = { itemQuantity = it.filter { ch -> ch.isDigit() } },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Item Quantity") },
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(Modifier.height(32.dp))

            Button(
                onClick = {
                    viewModel.updateItem(
                        item.copy(
                            itemName = itemName.text,
                            itemQuantity = itemQuantity.toIntOrNull() ?: 0
                        )
                    )
                    navController.popBackStack()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 30.dp)
                    .height(42.dp),
                shape = RoundedCornerShape(14.dp)
            ) {
                Text("Save", fontSize = 18.sp)
            }
        }
    }
}