package com.example.practice3

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import com.google.firebase.FirebaseApp

class LocalDatabase {

    @Entity(tableName = "products")
    data class Product(
        @PrimaryKey(autoGenerate = true) val id: Int = 0,
        val name: String,
        val description: String = "",
        val price: Double
    )


    @Dao
    interface ProductDao {
        @Insert
        suspend fun insertProduct(product: Product)

        @Query("SELECT * FROM products")
        suspend fun getAllProducts(): List<Product>

        @Query("SELECT * FROM products WHERE LOWER(name ) LIKE'%'  || LOWER(:productName) || '%'")
        suspend fun getProductByName(productName: String): List<Product>

        @Query("SELECT * FROM products WHERE name= :productName AND description = :productDescription AND price = :productPrice")
        suspend fun getProduct(productName: String, productDescription: String, productPrice: Double): Product?

        @Query("UPDATE products SET description = :description, price = :price WHERE name = :name")
        suspend fun updateProduct(name: String, description: String, price: Double)


        @Query("DELETE FROM products")
        suspend fun deleteAllProducts(){
            Log.d("ClearingProcess", " ProductDao deleteAllProducts")
        }

    }


    @Database(entities = [Product::class], version = 2)
    abstract class ProductDatabase : RoomDatabase() {
        abstract fun productDao(): ProductDao
    }

}
    class App: Application(){

        val database: LocalDatabase.ProductDatabase by lazy{
            Room.databaseBuilder(
                applicationContext,
                LocalDatabase.ProductDatabase::class.java,
                "product_database"
            ).fallbackToDestructiveMigration().build()
        }

        val repository : RemoteRepository by lazy{
            RemoteRepository()
        }

        override fun onCreate() {
            super.onCreate()
            FirebaseApp.initializeApp(this)
        }

    }


