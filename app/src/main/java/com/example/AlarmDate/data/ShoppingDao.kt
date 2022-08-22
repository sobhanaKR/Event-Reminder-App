package com.example.AlarmDate.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ShoppingDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(item: ShoppingItem)
    @Delete
    suspend fun delete(item: ShoppingItem)

    @Query("SELECT * FROM SHOPPING_ITEMS")
     fun getAllShoppingItems(): LiveData<List<ShoppingItem>>
}