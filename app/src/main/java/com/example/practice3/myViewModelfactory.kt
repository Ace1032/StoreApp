package com.example.practice3

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class myViewModelfactory(val database: LocalDatabase.ProductDatabase , val repository: RemoteRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(MyViewModel::class.java)){
            Log.d("MyViewModel", "ViewModel created")
            return MyViewModel(database, repository) as T
        }
            throw IllegalArgumentException("Unknown ViewModel class")

    }
}

class MyViewModel(database: LocalDatabase.ProductDatabase, repository: RemoteRepository) : ViewModel() {
        private val _products = MutableLiveData<List<Product>>()
        val products : LiveData<List<Product>> = _products

        private val _product = MutableLiveData<Product>()
        val product : LiveData<Product> = _product


        private val database = database
        private val repository = repository

init{
    Log.d("MyViewModel", "ViewModel created")
    viewModelScope.launch{
        database.productDao().deleteAllProducts()
        //insertData()
        fatchData()
    }

}


    override fun onCleared() {
        super.onCleared()
        Log.d("MyViewModel", "ViewModel destroyed")
        viewModelScope.launch {
            database.productDao().deleteAllProducts()

        }

    }


    fun updateProduct( name :String, description : String, price: Double){
        if(price >=0.0 && name.isNotEmpty() && description.isNotEmpty()){

            viewModelScope.launch{
                database.productDao().updateProduct(name, description, price)
                fatchData()

            }
        }

    }
    fun clearDatabase(){
        viewModelScope.launch {
            Log.d("ClearingProcess", "viewmodel clearDatabase")
            database.productDao().deleteAllProducts()
        }
    }

    private fun fatchData() {
        viewModelScope.launch{
            val set = HashSet<Product>()
            try {
                val proctList = database.productDao().getAllProducts()
                Log.d("fatchData", "proctList: ${proctList.size}")
                for(product in proctList){
                    Log.d("fatchData", "product: ${product.name}")
                    set.add(Product(product.name,product.description,product.price))
                }
                Log.d("fatchData", "set: ${set.size}")
                _products.value = set.toList()
            }catch (e: Exception){
                e.printStackTrace()
            }

        }
    }
    fun searchProduct(name : String){
        viewModelScope.launch{
            val products : List<LocalDatabase.Product> = database.productDao().getProductByName(name)
            Log.d("searchProduct", "product: ${products.size}")
            val set = HashSet<Product>()
            for(product in products){
                set.add(Product(product.name,product.description,product.price))
            }
            _products.value= set.toList()

        }
    }
    fun addProduct(name : String, description : String, price: Double){
        viewModelScope.launch{
            if(price >= 0.0 && name.isNotEmpty() && description.isNotEmpty() &&
                database.productDao().getProduct(name, description, price) == null){
                    database.productDao().insertProduct(LocalDatabase.Product(name = name, description = description, price = price))
            }
            fatchData()
        }

    }

    private fun insertData() {
        var list = listOf<LocalDatabase.Product>()
        viewModelScope.launch{
            list = listOf(
                LocalDatabase.Product(name = "Eggplant", description = "best quality", price = 20.0),
                LocalDatabase.Product(name = "Tomato", description = "best quality", price = 30.0),
                LocalDatabase.Product(name = "Potato", description = "best quality", price = 50.0),
                LocalDatabase.Product(name = "Onion", description = "best quality", price = 40.0),
                LocalDatabase.Product(name = "Cabbage", description = "best quality", price = 60.0),
                LocalDatabase.Product(name = "Carrot", description = "best quality", price = 70.0),
                LocalDatabase.Product(name = "Garlic", description = "best quality", price = 80.0),
                LocalDatabase.Product(name = "Spinach", description = "best quality", price = 90.0),
                LocalDatabase.Product(name = "Lettuce", description = "best quality", price = 100.0),
                LocalDatabase.Product(name = "Broccoli", description = "best quality", price = 110.0),
                LocalDatabase.Product(name = "Cauliflower", description = "best quality", price = 120.0),
                LocalDatabase.Product(name = "Zucchini", description = "best quality", price = 130.0),
                LocalDatabase.Product(name = "Asparagus", description = "best quality", price = 140.0),
                LocalDatabase.Product(name = "Kale", description = "best quality", price = 150.0),
                LocalDatabase.Product(name = "Celery", description = "best quality", price = 160.0),
                LocalDatabase.Product(name = "Peppers", description = "best quality", price = 170.0),
                LocalDatabase.Product(name = "Potatoes", description = "best quality", price = 180.0),
            )
            for(product in list){
                if(database.productDao().getProduct(product.name, product.description, product.price) != null) {
                    Log.d("fatchData", "product already exists")
                }else{
                    database.productDao().insertProduct(product)
                }
            }
            repository.insertAll(list)
        }








    }

}
