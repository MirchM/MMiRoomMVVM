package com.mmisoft.roommvvm

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class MainViewModel(application: Application) {

    val allProducts: LiveData<List<Product>>
    private val repository: ProductRepository
    val searchResult: MutableLiveData<List<Product>>

    init {
        val productDb = ProductRoomDatabase.getInstance(application)
        val productDao = productDb.productDao()
        repository = ProductRepository(productDao)

        allProducts = repository.allProducts
        searchResult = repository.searchResult
    }

    fun insertProduct(product: Product) {
        repository.insertProduct(product)
    }

    fun deleteProduct(id: Int) {
        repository.deleteProduct(id)
    }

    fun updateName(id: Int, newName: String){
        repository.updateName(id, newName)
    }

    fun updateQty(id: Int, newQty: Int){
        repository.updateQty(id, newQty)
    }

    fun findByName(word: String){
        repository.findByName(word)
    }
}