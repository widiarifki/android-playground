package id.widiarifki.compose.feature

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import id.widiarifki.compose.data.entity.ShoppingItem

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

    val shoppingItems: List<ShoppingItem> = viewModel.shoppingItems
        .observeAsState(listOf())
        .value

    MaterialTheme {
        ShoppingListScreen(
            shoppingItems = shoppingItems,
            currentlyEditing = viewModel.currentEditItemState,
            onAddItem = viewModel::addItem,
            onToggleTickItem = viewModel::toggleTickItem,
            onDeleteItem = viewModel::deleteItem,
            onEditItemSelected = viewModel::onEditItemSelected,
            onEditItemChange = viewModel::onEditItemChange
        )
    }
}