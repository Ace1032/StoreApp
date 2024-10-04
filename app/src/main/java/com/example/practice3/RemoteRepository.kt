package com.example.practice3

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class RemoteRepository {

    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()


    suspend fun authnticate() {
        auth.signInWithEmailAndPassword(
            "", ""
        ).addOnCompleteListener { task ->
            if(task.isSuccessful){
                val user = auth.currentUser
                Log.d("RemoteRepository", "User: ${user?.email}")
            }else{
                Log.d("RemoteRepository", "Error: ${task.exception?.message}")
            }

        }


    }

    suspend fun addProduct(product: Product) {
        try{
            firestore.collection("products").add(product).await()
        }catch (e: Exception){
            e.printStackTrace()
        }
    }

    suspend fun getProducts():List<Product>{
        return try{
            val snapshot = firestore.collection("products").get().await()
            snapshot.toObjects(Product::class.java)
        }catch (e: Exception){
            e.printStackTrace()
            return emptyList()
        }
    }

    suspend fun insertAll(list: List<LocalDatabase.Product>){
        authnticate()



            for(p in list){
                val product = hashMapOf(
                    "name" to p.name,
                    "description" to p.description,
                    "price" to p.price
                )
                try{
                    firestore.collection("products").add(product)
                        .addOnSuccessListener {

                            Log.d("RemoteRepository", "Product added with ID: ${it.id}")
                            println("Product added with ID: ${it.id}")
                        }
                        .addOnFailureListener{ e ->
                            Log.w("RemoteRepository", "Error adding product", e)
                            println("Error adding product: $e")
                        }

                }catch (e: Exception){
                    e.printStackTrace()
                }


            }


    }


}