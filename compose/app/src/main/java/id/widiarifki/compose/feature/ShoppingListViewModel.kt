package id.widiarifki.compose.feature

import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.widiarifki.compose.data.entity.ShoppingItem
import id.widiarifki.compose.data.repository.ShoppingItemRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ShoppingListViewModel(
    private val repository: ShoppingItemRepository = ShoppingItemRepository()
) : ViewModel() {

    // state variables
    val shoppingItems: LiveData<List<ShoppingItem>> = repository.allShoppingItems
    var currentEditItemState by mutableStateOf<ShoppingItem?>(null)

    // event: add item
    fun addItem(item: ShoppingItem) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insert(item)
        }
    }

    // event: delete item
    fun deleteItem(item: ShoppingItem) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.delete(item)
        }
        if (item == currentEditItemState) currentEditItemState = null
    }

    // event: tick/un-tick item
    fun toggleTickItem(item: ShoppingItem) {
        val updatedItem = item.copy(isTicked = !item.isTicked)
        viewModelScope.launch(Dispatchers.IO) {
            repository.update(updatedItem)
        }
    }

    // event: on item selected
    fun onEditItemSelected(item: ShoppingItem) {
        Log.d("TESS", "${shoppingItems.value?.indexOf(item) ?: -1}")
        currentEditItemState = item
        Log.d("TESS", "${currentEditItemState?.name}")
    }

    // event: on item updated
    fun onEditItemChange(item: ShoppingItem) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.update(item)
        }
    }
}