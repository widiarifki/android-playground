package id.widiarifki.compose.feature

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
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
    shoppingItems: List<ShoppingItem>,
    onAddItem: (ShoppingItem) -> Unit,
    onToggleTickItem: (ShoppingItem) -> Unit,
    onDeleteItem: (ShoppingItem) -> Unit
) {
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
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp)
            ) {
                items(
                    items = shoppingItems,
                    key = { item -> item.id }
                ) { item ->
                    ShoppingItemCard(item, onToggleTickItem, onDeleteItem)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }

        // Form input new item
        InputItemContainer(onAddItem)
    }
}

@ExperimentalMaterialApi
@Composable
fun ShoppingItemCard(
    shoppingItem: ShoppingItem,
    onToggleTickItem: (ShoppingItem) -> Unit,
    onDeleteItem: (ShoppingItem) -> Unit
) {
    val swipeToDismissState = rememberDismissState(
        confirmStateChange = {
            if (it == DismissValue.DismissedToEnd) onDeleteItem(shoppingItem)
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
                // Text
                Text(
                    text = shoppingItem.name,
                    style = textStyle,
                    modifier = Modifier.weight(1f)
                )

                // Done checkbox
                Checkbox(
                    checked = shoppingItem.isTicked,
                    onCheckedChange = {
                        onToggleTickItem(shoppingItem)
                    }
                )
            }
        }
    }
}

@Composable
fun InputItemContainer(
    onAddItem: (ShoppingItem) -> Unit
) {
    var itemName by remember { mutableStateOf("") }
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
            onValueChange = { newValue -> itemName = newValue },
            value = itemName
        )
        Spacer(modifier = Modifier.width(8.dp))
        Button(
            enabled = itemName.isNotBlank(),
            onClick = {
                onAddItem(ShoppingItem(name = itemName))
                itemName = ""
                focusManager.clearFocus()
            },
            modifier = Modifier.fillMaxHeight()
        ) {
            Text(text = "Tambah")
        }
    }
}

@ExperimentalMaterialApi
@Preview(showBackground = true)
@Composable
fun PreviewShoppingListScreen() {

    val previewItems = List(10) { index ->
        ShoppingItem(name = "Item Preview $index")
    }

    MaterialTheme {
        ShoppingListScreen(
            previewItems, {}, {}, {}
        )
    }
}