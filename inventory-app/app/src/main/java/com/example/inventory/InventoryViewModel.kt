package com.example.inventory

import androidx.lifecycle.*
import com.example.inventory.data.Item
import com.example.inventory.data.ItemDao
import kotlinx.coroutines.launch

class InventoryViewModel(private val itemDao: ItemDao) : ViewModel() {

    val allItems: LiveData<List<Item>> = itemDao.getItems().asLiveData()

    private fun insertItem(item: Item) {
        viewModelScope.launch { itemDao.insert(item) }
    }

    private fun getNewItemEntry(itemName: String, itemPrice: String, itemCount: String): Item {
        return Item(
            itemName = itemName,
            itemPrice = itemPrice.toDouble(),
            quantityInStock = itemCount.toInt()
        )
    }

    fun addNewItem(itemName: String, itemPrice: String, itemCount: String) {
        val newItem = getNewItemEntry(itemName, itemPrice, itemCount)
        insertItem(newItem)
    }

    fun retrieveItem(id: Int): LiveData<Item> {
        return itemDao.getItem(id).asLiveData()
    }

    private fun updateItem(item: Item) {
        viewModelScope.launch { itemDao.update(item) }
    }

    fun sellItem(item: Item) {
        if (item.quantityInStock > 0) {
            // The copy() function is provided by default to all the instances of data classes.
            // This function is used to copy an object for changing some of its properties,
            // but keeping the rest of the properties unchanged.
            val newItem = item.copy(quantityInStock = item.quantityInStock.minus(1))
            updateItem(newItem)
        }
    }

    fun deleteItem(item: Item) {
        viewModelScope.launch { itemDao.delete(item) }
    }

    fun updateItem(
        itemId: Int,
        itemName: String,
        itemPrice: String,
        itemCount: String
    ) {
        val updatedItem = getUpdatedItemEntity(itemId, itemName, itemPrice, itemCount)
        updateItem(updatedItem)
    }

    private fun getUpdatedItemEntity(
        itemId: Int,
        itemName: String,
        itemPrice: String,
        itemCount: String
    ): Item {
        return Item(
            id = itemId,
            itemName = itemName,
            itemPrice = itemPrice.toDouble(),
            quantityInStock = itemCount.toInt()
        )
    }

    fun isStockAvailable(item: Item): Boolean {
        return item.quantityInStock > 0
    }

    fun isEntryValid(itemName: String, itemPrice: String, itemCount: String): Boolean {
        if (itemName.isBlank() || itemPrice.isBlank() || itemCount.isBlank()) {
            return false
        }
        return true
    }
}

class InventoryViewModelFactory(
    private val itemDao: ItemDao
) : ViewModelProvider.Factory {
    /**
     * Takes any class type as an argument and returns a ViewModel object.
     * */
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        //  Check if the modelClass is the same as the InventoryViewModel class and return
        //  an instance of it. Otherwise, throw an exception.
        if (modelClass.isAssignableFrom(InventoryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return InventoryViewModel(itemDao) as T
        }
        throw  IllegalArgumentException("Unknown ViewModel class")
    }

}