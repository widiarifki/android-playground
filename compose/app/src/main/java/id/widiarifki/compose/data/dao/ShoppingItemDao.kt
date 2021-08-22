package id.widiarifki.compose.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import id.widiarifki.compose.data.Constant.TBL_SHOPPING_ITEM
import id.widiarifki.compose.data.entity.ShoppingItem

@Dao
interface ShoppingItemDao {

    @Query("SELECT * FROM $TBL_SHOPPING_ITEM")
    fun getAll(): LiveData<List<ShoppingItem>>

    @Insert
    suspend fun insert(item: ShoppingItem)

    @Update
    suspend fun update(item: ShoppingItem)

    @Delete
    suspend fun delete(item: ShoppingItem)
}