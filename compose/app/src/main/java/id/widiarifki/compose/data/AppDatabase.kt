package id.widiarifki.compose.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import id.widiarifki.compose.data.Constant.DB_NAME
import id.widiarifki.compose.data.Constant.DB_VERSION
import id.widiarifki.compose.data.dao.ShoppingItemDao
import id.widiarifki.compose.data.entity.ShoppingItem
import java.lang.RuntimeException

@Database(entities = [ShoppingItem::class], version = DB_VERSION)
abstract class AppDatabase : RoomDatabase() {

    abstract fun shoppingItemDao(): ShoppingItemDao

    companion object {
        private var INSTANCE: AppDatabase? = null

        fun initialize(context: Context) {
            INSTANCE = synchronized(this) {
                Room.databaseBuilder(context, AppDatabase::class.java, DB_NAME)
                    .build()
            }
        }

        fun getInstance() : AppDatabase {
            return INSTANCE ?: run {
                throw RuntimeException("Please initialize app database in Application().onCreate() by calling AppDatabase.initialize()")
            }
        }
    }
}