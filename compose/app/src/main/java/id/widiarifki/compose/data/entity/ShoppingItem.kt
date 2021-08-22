package id.widiarifki.compose.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import id.widiarifki.compose.data.Constant.TBL_SHOPPING_ITEM

@Entity(tableName = TBL_SHOPPING_ITEM)
data class ShoppingItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "name")
    var name: String,
    @ColumnInfo(name = "is_ticked")
    var isTicked: Boolean = false
)