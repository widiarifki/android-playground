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
    isLoading: Boolean,
    shoppingItems: List<ShoppingItem>,
    onAddItem: (ShoppingItem) -> Unit,
    onToggleTickItem: (ShoppingItem) -> Unit,
    onDeleteItem: (ShoppingItem) -> Unit
) {

    // Content container
    Column(modifier = Modifier.fillMaxHeight()) {

        val expandHeight = Modifier.weight(1f)
        when {
            isLoading -> {
                LoadingIndicator(expandHeight)
            }
            !isLoading && shoppingItems.isEmpty() -> {
                EmptyState(expandHeight)
            }
            !isLoading && shoppingItems.isNotEmpty() -> {
                ShoppingList(
                    shoppingItems = shoppingItems,
                    onToggleTickItem = onToggleTickItem,
                    onDeleteItem = onDeleteItem,
                    modifier = expandHeight
                )
            }
        }

        if (!isLoading) FormInputContainer(onAddItem)
    }
}

@Composable
fun LoadingIndicator(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(color = MaterialTheme.colors.primary)
    }
}

@Composable
fun EmptyState(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        Text(
            text = "Yah, belum ada item belanja!",
            style = MaterialTheme.typography.h5,
            textAlign = TextAlign.Center
        )
    }
}

@ExperimentalMaterialApi
@Composable
fun ShoppingList(
    shoppingItems: List<ShoppingItem>,
    onToggleTickItem: (ShoppingItem) -> Unit,
    onDeleteItem: (ShoppingItem) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier.padding(8.dp)) {
        items(
            items = shoppingItems,
            key = { item -> item.id }
        ) { item ->
            // Item card
            ShoppingItemCard(item, onToggleTickItem, onDeleteItem)
            // Space between card
            Spacer(modifier = Modifier.height(8.dp))
        }
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
        confirmStateChange = { dismissValue ->
            val isItemDismissed = dismissValue == DismissValue.DismissedToEnd

            // delete only if item is swiped until the end/right side of screen
            if (isItemDismissed) onDeleteItem(shoppingItem)

            // return Boolean value where we considered item is dismissed
            isItemDismissed
        }
    )

    var textStyle = MaterialTheme.typography.body1
    if (shoppingItem.isTicked) {
        textStyle = textStyle.copy(textDecoration = TextDecoration.LineThrough)
    }

    SwipeToDismiss(
        state = swipeToDismissState,
        background = {
            // View showing in the back of the swiped card
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
fun FormInputContainer(
    onAddItem: (ShoppingItem) -> Unit
) {
    var newItem by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    Row(
        modifier = Modifier
            .fillMaxWidth(1f)
            .height(IntrinsicSize.Min)
            .padding(8.dp)
    ) {
        // Field input
        TextField(
            label = { Text("Item belanja baru") },
            singleLine = true,
            onValueChange = { newItem = it },
            value = newItem
        )

        // Margin / spacer
        Spacer(modifier = Modifier.width(8.dp))

        // Button add item
        Button(
            enabled = newItem.isNotBlank(),
            onClick = {
                onAddItem(ShoppingItem(name = newItem))
                newItem = ""
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
        ShoppingItem(id = index, name = "Item Preview $index")
    }

    MaterialTheme {
        ShoppingListScreen(
            isLoading = true,
            shoppingItems = previewItems,
            onAddItem = {},
            onToggleTickItem = {},
            onDeleteItem = {}
        )
    }
}