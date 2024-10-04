package com.example.practice3

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import kotlinx.coroutines.runBlocking

import androidx.compose.ui.graphics.Color

import androidx.compose.material.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*





class MainActivity : ComponentActivity() {
    private  lateinit var viewModel:MyViewModel
    private lateinit var database: LocalDatabase.ProductDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val app = application as App

        database = app.database
        val repository = app.repository


        if (!this::viewModel.isInitialized) {
            viewModel =
                ViewModelProvider(this, myViewModelfactory(database , repository) ).get(MyViewModel::class.java)
        }else{
            Log.d("MainActivity", "ViewModel already initialized")
        }


        setContent {
                setAppTheame(viewModel)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        runBlocking {
            Log.d("ClearingProcess", " main activity inside run blocking onDestroy")
            viewModel.clearDatabase()
        }
        Log.d("ClearingProcess", "onDestroy")


    }


    @Preview(showBackground = true)
    @Composable
    fun GreetingPreview() {

        setAppTheame(viewModel)
    }




    @Composable
    fun setAppTheame(viewModel: MyViewModel) {



        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            search(viewModel)
            listItem(viewModel)
            recyclerViewSetup(viewModel)
        }


    }

    @Composable
    private fun listItem(viewModel: MyViewModel) {
        val product by viewModel.product.observeAsState()
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center

        ){
            item{
                if(product != null) {
                    Text(text = "${product?.name}, ${product?.description}, ${product?.price.toString()}")
                }
            }
        }

    }

    @Composable
    private fun search(viewModel: MyViewModel) {

        var searchText by remember { mutableStateOf("") }
        var searchButton by remember { mutableStateOf(false) }
        var addProductButton by remember { mutableStateOf(false) }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center
        ){

            TextField(
                value = searchText,
                onValueChange = {
                    searchText = it
                },
            )




            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {


                Button(
                    onClick = {
                        searchButton = true
                            viewModel.searchProduct(searchText)
                    }
                ) {
                    Text(text = "Search")
                }


                Button(
                    onClick = {
                        addProductButton = true
                    }
                ) {
                    Text(text = "Add Item")
                }


            }//Column END






        }
        if(addProductButton){

            addProduct(
                onDismiss = {
                    addProductButton = false
                },
                onSave = { name, description, price ->
                    viewModel.updateProduct(name, description, price)
                    addProductButton = false
                }

            )
        }

    }

    @Composable
     fun addProduct(
        onDismiss: () -> Unit,
        onSave: (String, String, Double) -> Unit
    ) {
        var name by remember { mutableStateOf("") }
        var description by remember { mutableStateOf("") }
        var price by remember { mutableStateOf(0.0) }

        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text(text = "Add Product") },
            text = {
                Column {

                    TextField(
                        value = name,
                        onValueChange = {
                            name = it
                        }
                    )

                        Spacer(modifier = Modifier.height(16.dp))

                        TextField(
                            value = description,
                            onValueChange = {
                                description = it
                            }
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        TextField(
                            value = price.toString(),
                            onValueChange = {
                                price = it.toDouble()
                            },
                            keyboardOptions = KeyboardOptions.Default.copy(
                        )
                        )

                }
            },
            confirmButton = {
                TextButton(onClick = {
                    val priceDouble = price.toDouble()
                    val name = name
                    val description = description
                    onSave(
                        name,
                        description,
                        priceDouble
                    )
                }) {
                    viewModel.addProduct(name, description, price)
                    Text(text = "Add")
                }
            },
            dismissButton = {
                TextButton(onClick = onDismiss) {
                    Text(text = "Cancel")
                }
            }
        )
    }








    @Composable
    private fun recyclerViewSetup(viewModel: MyViewModel) {

       val products by viewModel.products.observeAsState(emptyList())
        Log.d("recyclerViewSetup", "products: $products.size")

        LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(30.dp)
            ){
                items(products.size){ it ->
                   // Text(text = "${products?.get(it)?.name}:   " +
                   //         "${products?.get(it)?.description},  $${products?.get(it)?.price.toString()}"
                  //  , fontSize = 20.sp)
                    productScreen(products[it])
                    Spacer(modifier = Modifier.height(10.dp))

                }
            }
        }



    @Composable
    private fun productScreen(product: Product) {
        var isDialogOpen by remember {mutableStateOf(false)}


        Card(
           modifier = Modifier
               .fillMaxWidth()
               .padding(16.dp)
               .wrapContentHeight()
               .clickable {
                   isDialogOpen = true
               },
            elevation = CardDefaults.cardElevation()

        ){
            Column(
                modifier = Modifier.padding(16.dp)
            ){
              Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween
              ) {
                  Text(text = product.name, fontSize = 20.sp, style= MaterialTheme.typography.titleMedium)
                  Text(text = "  $${product.price.toString()}", fontSize = 20.sp, style= MaterialTheme.typography.titleMedium)

              }
                Text(text = product.description, fontSize = 20.sp, style= MaterialTheme.typography.titleMedium)

            }
            Spacer(modifier = Modifier.height(10.dp))


        }


        if(isDialogOpen){
            EditProductDialog(
                product = product,
                onDismiss = {
                    isDialogOpen = false
                },
                onSave ={ newDescription, newPrice ->
                    println("updated product $newDescription, $newPrice")
                    isDialogOpen = false

                }

            )
        }



    }


    @Composable
    fun EditProductDialog( product: Product,
                           onDismiss:() -> Unit,
                           onSave:(String, Double) -> Unit
    ) {


        var editedPrice by remember  {mutableStateOf( product.price.toString())}
        var editedDescription by remember  {mutableStateOf( product.description)}

        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text(text = "${product.name}")},
            text = {
                Column{
                    TextField(
                        value = editedPrice,
                        onValueChange = { newPrice ->
                            editedPrice = newPrice
                        },
                        label = {
                            Text(text = "New Price")
                        },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Number

                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    TextField(
                        value = editedDescription,
                        onValueChange = { newDescription ->
                            editedDescription = newDescription
                        },
                        label = {
                            Text(text = "New Description")
                        }

                    )
                }

            },
            confirmButton ={
                Button(
                    onClick ={
                        Log.d("EditProductDialog", "editedDescription: $editedDescription, editedPrice: $editedPrice")

                        onSave(editedDescription, editedPrice.toDouble())
                        viewModel.updateProduct(product.name, editedDescription, editedPrice.toDouble())
                    }
                ){
                    Text(text = "Save")
                }
            },
            dismissButton = {
                Button(
                    onClick = onDismiss
                ) {
                    Text(text = "Cancel")

                }
            }
        )

    }//@Composable END



    }//CLASS END


