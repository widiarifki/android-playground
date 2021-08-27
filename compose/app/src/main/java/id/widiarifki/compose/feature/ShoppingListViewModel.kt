package id.widiarifki.compose.feature

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.widiarifki.compose.data.entity.ShoppingItem
import id.widiarifki.compose.data.repository.ShoppingItemRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ShoppingListViewModel(
    private val repository: ShoppingItemRepository = ShoppingItemRepository()
) : ViewModel() {

    // variables that hold state
    var isLoading by mutableStateOf(true)
    val shoppingItems: LiveData<List<ShoppingItem>> = getItems()

    private fun getItems(): LiveData<List<ShoppingItem>> {
        isLoading = true
        return Transformations.map(repository.allShoppingItems) { items ->
            isLoading = false
            items
        }
    }

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