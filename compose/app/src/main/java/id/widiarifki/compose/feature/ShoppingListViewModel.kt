package id.widiarifki.compose.feature

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

    // state: list of shopping item
    val liveShoppingItems: LiveData<List<ShoppingItem>> = repository.allShoppingItems

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
    }

    // event: tick/un-tick item
    fun toggleTickItem(item: ShoppingItem) {
        val updatedItem = item.copy(isTicked = !item.isTicked)
        viewModelScope.launch(Dispatchers.IO) {
            repository.update(updatedItem)
        }
    }
}