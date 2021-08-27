package id.widiarifki.compose.feature

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable

class ShoppingListActivity : ComponentActivity() {

    private val viewModel by viewModels<ShoppingListViewModel>()

    @ExperimentalMaterialApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ShoppingListView(viewModel)
        }
    }
}

@ExperimentalMaterialApi
@Composable
fun ShoppingListView(viewModel: ShoppingListViewModel) {

    // observeAsState() can only be called from Composable function
    // to observing LiveData and represent its value via State
    val shoppingItemsState = viewModel.shoppingItems.observeAsState(listOf())

    val shoppingItems by rememberSaveable { shoppingItemsState }

    MaterialTheme {
        ShoppingListScreen(
            isLoading = viewModel.isLoading,
            shoppingItems = shoppingItems,
            onAddItem = viewModel::addItem,
            onToggleTickItem = viewModel::toggleTickItem,
            onDeleteItem = viewModel::deleteItem
        )
    }
}