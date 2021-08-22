package id.widiarifki.compose.feature

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import id.widiarifki.compose.data.entity.ShoppingItem

@ExperimentalMaterialApi
@Composable
fun ShoppingListScreen(
    viewModel: ShoppingListViewModel
    /*shoppingItems: List<ShoppingItem>,
    onAddItem: (ShoppingItem) -> Unit,
    onTickItem: (ShoppingItem) -> Unit,
    onDeleteItem: (ShoppingItem) -> Unit*/
) {
    val shoppingItems: List<ShoppingItem> = viewModel.liveShoppingItems
        .observeAsState(listOf())
        .value

    Column(modifier = Modifier.fillMaxHeight()) {
        if (shoppingItems.isEmpty()) {

            // Empty state
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Yah, belum ada item belanja!",
                    style = MaterialTheme.typography.h5,
                    textAlign = TextAlign.Center
                )
            }
        } else {

            // List
            ShoppingList(
                shoppingItems = shoppingItems,
                viewModel = viewModel,
                modifier = Modifier.weight(1f)
            )
        }

        // Form input new item
        InputItemContainer(viewModel::addItem)
    }
}

@ExperimentalMaterialApi
@Composable
fun ShoppingList(
    shoppingItems: List<ShoppingItem>,
    viewModel: ShoppingListViewModel,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier.padding(8.dp)) {
        items(
            items = shoppingItems,
            key = { item -> item.id }
        ) { item ->
            ShoppingItemCard(item, viewModel::toggleTickItem, viewModel::deleteItem)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@ExperimentalMaterialApi
@Composable
fun ShoppingItemCard(
    shoppingItem: ShoppingItem,
    eventToggleTickItem: (ShoppingItem) -> Unit,
    eventDeleteItem: (ShoppingItem) -> Unit
) {
    val swipeToDismissState = rememberDismissState(
        confirmStateChange = {
            if (it == DismissValue.DismissedToEnd) eventDeleteItem(shoppingItem)
            it == DismissValue.DismissedToEnd
        }
    )

    var textStyle = MaterialTheme.typography.body1
    if (shoppingItem.isTicked) {
        textStyle = textStyle.copy(textDecoration = TextDecoration.LineThrough)
    }

    SwipeToDismiss(
        state = swipeToDismissState,
        background = {
            Box(
                modifier = Modifier.fillMaxHeight(),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = "Swipe left to cancel delete",
                    style = MaterialTheme.typography.body2
                )
            }
        }
    ) {
        Card {
            Row(
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .padding(10.dp)
            ) {
                Text(
                    text = shoppingItem.name,
                    style = textStyle,
                    modifier = Modifier.weight(1f)
                )
                Checkbox(
                    checked = shoppingItem.isTicked,
                    onCheckedChange = {
                        eventToggleTickItem(shoppingItem)
                    }
                )
            }
        }
    }
}

@Composable
fun InputItemContainer(eventAddItem: (ShoppingItem) -> Unit) {
    val newItemName = remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current
    Row(
        modifier = Modifier
            .fillMaxWidth(1f)
            .height(IntrinsicSize.Min)
            .padding(8.dp)
    ) {
        TextField(
            label = { Text("Item belanja baru") },
            singleLine = true,
            onValueChange = { newItemName.value = it },
            value = newItemName.value,
            modifier = Modifier.weight(1f)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Button(
            enabled = newItemName.value.isNotBlank(),
            onClick = {
                eventAddItem(ShoppingItem(name = newItemName.value))
                newItemName.value = ""
                focusManager.clearFocus()
            },
            modifier = Modifier.fillMaxHeight()
        ) {
            Text(text = "Tambah")
        }
    }
}

@ExperimentalMaterialApi
@Preview(showBackground = true) // It means this function will not reflect output on app runtime
@Composable
fun PreviewShoppingListScreen() {
    val previewItems = List(10) { index ->
        ShoppingItem(name = "Item Preview $index")
    }
    val viewModel = ShoppingListViewModel()

    MaterialTheme {
        Column(modifier = Modifier.fillMaxHeight()) {
            if (previewItems.isEmpty()) {

                // Empty state
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Yah, belum ada item belanja!",
                        style = MaterialTheme.typography.h5,
                        textAlign = TextAlign.Center
                    )
                }
            } else {

                // List
                ShoppingList(
                    shoppingItems = previewItems,
                    viewModel = viewModel,
                    modifier = Modifier.weight(1f)
                )
            }

            // Form input new item
            InputItemContainer(viewModel::addItem)
        }
    }
}