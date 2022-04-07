package com.mmisoft.roommvvm

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ProductDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertProduct(product: Product)

    @Query("SELECT * FROM products WHERE productName = :name")
    fun findProduct(name: String): List<Product>

    @Query("SELECT * FROM products WHERE productName LIKE :word")
    fun findByName(word:String):List<Product>

    @Query("DELETE FROM products WHERE productId = :id")
    fun deleteProduct(id: Int)

    @Query("UPDATE products SET productName = :newName WHERE productId = :id")
    fun updateName(id: Int, newName: String)

    @Query("UPDATE products SET quantity = :newQty WHERE productId = :id")
    fun updateQty(id: Int, newQty: Int)

    @Query("SELECT * FROM products")
    fun getAllProducts(): LiveData<List<Product>>
}