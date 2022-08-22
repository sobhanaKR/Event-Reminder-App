package com.example.AlarmDate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.AlarmDate.data.ShoppingItem
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ShoppingViewModel(
    private val repository: ShoppingRepository
) : ViewModel() {

    private val isLoading = MutableStateFlow(true)
    val isLoad = isLoading.asStateFlow()

    fun upsert(item: ShoppingItem) =
        GlobalScope.launch {
            repository.upsert(item)
        }

    init {
        viewModelScope.launch {
            delay(3000)
            isLoading.value = false
        }
    }
    fun delete(item: ShoppingItem) = GlobalScope.launch {
        repository.delete(item)
    }

    fun getAllShoppingItems() = repository.getAllShoppingItems()

}

