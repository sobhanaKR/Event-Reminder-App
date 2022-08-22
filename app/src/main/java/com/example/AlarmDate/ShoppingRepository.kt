package com.example.AlarmDate

import com.example.AlarmDate.data.ShoppingDatabase
import com.example.AlarmDate.data.ShoppingItem

class ShoppingRepository(
    private val db: ShoppingDatabase
) {
    suspend fun upsert(item: ShoppingItem) = db.getShoppingDao().upsert(item)

    suspend fun delete(item: ShoppingItem) = db.getShoppingDao().delete(item)

     fun getAllShoppingItems() = db.getShoppingDao().getAllShoppingItems()
}