package com.mmisoft.roommvvm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.*

class ProductRepository(private val productDao: ProductDao) {

    val allProducts: LiveData<List<Product>> = productDao.getAllProducts()

    var searchResult = MutableLiveData<List<Product>>()

    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    fun insertProduct(newproduct: Product) {
        coroutineScope.launch(Dispatchers.IO) {
            productDao.insertProduct(newproduct)
        }
    }

    fun deleteProduct(id: Int) {
        coroutineScope.launch(Dispatchers.IO) {
            productDao.deleteProduct(id)
        }
    }

    fun updateName(id: Int, newName: String){
        coroutineScope.launch(Dispatchers.IO){
            productDao.updateName(id, newName)
        }
    }

    fun updateQty(id: Int, newQty: Int){
        coroutineScope.launch(Dispatchers.IO){
            productDao.updateQty(id, newQty)
        }
    }

    fun findByName(word: String?): LiveData<List<Product>> {
        coroutineScope.launch(Dispatchers.IO) {
            val result = word?.let { productDao.findByName(it) }
            searchResult.postValue(result)
        }
        return searchResult
    }
}