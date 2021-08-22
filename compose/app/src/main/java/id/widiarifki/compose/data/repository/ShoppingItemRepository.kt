package id.widiarifki.compose.data.repository

import id.widiarifki.compose.data.AppDatabase
import id.widiarifki.compose.data.dao.ShoppingItemDao
import id.widiarifki.compose.data.entity.ShoppingItem

class ShoppingItemRepository(
    private val shoppingItemDao: ShoppingItemDao = AppDatabase.getInstance().shoppingItemDao()
) {

    val allShoppingItems = shoppingItemDao.getAll()

    suspend fun insert(item: ShoppingItem) {
        shoppingItemDao.insert(item)
    }

    suspend fun delete(item: ShoppingItem) {
        shoppingItemDao.delete(item)
    }

    suspend fun update(item: ShoppingItem) {
        shoppingItemDao.update(item)
    }
}