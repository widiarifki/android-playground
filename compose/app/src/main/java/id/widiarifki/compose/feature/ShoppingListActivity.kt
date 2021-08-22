package id.widiarifki.compose.feature

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable

class ShoppingListActivity : ComponentActivity() {

    private val viewModel by viewModels<ShoppingListViewModel>()

    @ExperimentalMaterialApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ShoppingListActivity(viewModel)
        }
    }
}

@ExperimentalMaterialApi
@Composable
fun ShoppingListActivity(viewModel: ShoppingListViewModel) {
    MaterialTheme {
        ShoppingListScreen(
            viewModel = viewModel
            /*shoppingItems = viewModel.shoppingItems,
            onAddItem = viewModel::addItem,
            onTickItem = viewModel::tickItem,
            onDeleteItem = viewModel::deleteItem*/
        )
    }
}