package com.mmisoft.roommvvm

import android.app.Application
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ScreenSetup()
        }
    }
}

@Composable
fun TitleRow(head1: String, head2: String, head3: String) {
    Row(
        modifier = Modifier
            .background(MaterialTheme.colors.primary)
            .fillMaxWidth()
            .padding(horizontal = 6.dp, vertical = 5.dp)
    ) {
        Text(head1, color = Color.White,
            modifier = Modifier
                .weight(0.05f),
            fontSize = 20.sp)
        Text(head2, color = Color.White,
            modifier = Modifier
                .weight(0.32f),
            fontSize = 20.sp)
        Text(head3, color = Color.White,
            modifier = Modifier.weight(0.23f),
            fontSize = 20.sp)
    }
}

@Composable
fun ProductRow(
    id: Int,
    name: String,
    quantity: Int,
    viewModel: MainViewModel
) {

    var newName by remember { mutableStateOf("")}
    var newNumber by remember { mutableStateOf("")}

    var expand by remember { mutableStateOf(false) }
    var changeName by remember { mutableStateOf(false) }
    var changeQty by remember { mutableStateOf(false) }
    var delete by remember { mutableStateOf(false) }

    val context: Context = LocalContext.current
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp, horizontal = 5.dp),
        elevation = 2.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 15.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 5.dp)
                    .clickable {
                        expand = !expand
                    }
                ,
            ) {
                Text(id.toString(), modifier = Modifier
                    .weight(0.034f),
                    fontSize = 18.sp)
                Text(name, modifier = Modifier.weight(0.2f),
                    fontSize = 18.sp)
                Text(quantity.toString(), modifier = Modifier.weight(0.1f),
                    fontSize = 18.sp)
            }

            AnimatedVisibility(visible = expand) {
                Row (
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .padding(vertical = 5.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                ){
                    Button(onClick = {
                        changeName = true
                        changeQty = false
                        expand = false
                    }) {
                        Text(text = "Change Name", maxLines = 1)
                    }

                    Button(onClick = {
                        changeQty = true
                        changeName = false
                        expand = false
                    }) {
                        Text(text = "Change Quantity", maxLines = 1)
                    }

                    Button(onClick = {
                        delete = true
                    }) {
                        Text(text = "Delete Item", maxLines = 1)
                    }
                }
            }

            if(delete){
                AlertDialog(
                    onDismissRequest = { delete = false },
                    title = {
                        Text(text = "Delete Confirmation")},
                    text = { Text(text = "Are you sure that you want to delete the item with the name: $name with the id: $id and the quantity: $quantity?")},
                    confirmButton = {
                        Button(
                            onClick = { viewModel.deleteProduct(id)
                                delete = false
                                expand = false
                            }) {
                            Text(text = "Yes")
                        }
                    },
                    dismissButton = {
                        Button(
                            onClick = {
                            delete = false
                            expand = false
                        }) {
                            Text(text = "Cancel")
                        }
                    }
                )


            }

            AnimatedVisibility(visible = changeName) {
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    TextField(
                        label = {Text("New Name")},
                        modifier = Modifier.fillMaxWidth(0.5f),
                        value = newName,
                        onValueChange = { text : String ->
                            newName = text
                        }
                    )
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            if(newName != "") viewModel.updateName(id, newName)
                            changeName = false
                        }) {
                        Text(text = "Change Name", maxLines = 1)
                    }
                }
            }

            AnimatedVisibility(visible = changeQty) {
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    TextField(
                        label = {Text("New Quantity")},
                        modifier = Modifier.fillMaxWidth(0.5f),
                        value = newNumber,
                        onValueChange = { text : String ->
                            newNumber = text
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)

                    )
                    Button(
                        onClick = {
                            if(newNumber != "") viewModel.updateQty(id, newNumber.toInt())
                            changeQty = false
                        },) {
                        Text(text = "Change Quantity", maxLines = 1)
                    }
                }
            }
    }

    }

}




@Composable
fun CustomTextField(
    title: String,
    textState: String,
    onTextChange: (String) -> Unit,
    keyboardType: KeyboardType
) {
    OutlinedTextField(
        value = textState,
        onValueChange = { onTextChange(it) },
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType
        ),
        singleLine = true,
        label = { Text(title)},
        modifier = Modifier.padding(10.dp),
        textStyle = TextStyle(fontWeight = FontWeight.Bold,
            fontSize = 30.sp)
    )
}

@Composable
fun ScreenSetup(
    viewModel: MainViewModel =
        MainViewModel(LocalContext.current.applicationContext as Application)
) {
    val allProducts by viewModel.allProducts.observeAsState(listOf())
    val searchResult by viewModel.searchResult.observeAsState(listOf())

        MainScreen(
            allProducts = allProducts,
            searchResult = searchResult,
            viewModel = viewModel
        )


}

@Composable
fun MainScreen(
    allProducts: List<Product>,
    searchResult: List<Product>,
    viewModel: MainViewModel
) {
    var productName by remember { mutableStateOf("") }
    var productQuantity by remember { mutableStateOf("") }
    var searchName by remember { mutableStateOf("") }
    var searching by remember { mutableStateOf(false) }
    var adding by remember { mutableStateOf(false) }

    val onSearchTextChange = { text : String ->
        searchName = text
    }

    val onProductTextChange = { text : String ->
        productName = text
    }

    val onQuantityTextChange = { text : String ->
        productQuantity = text
    }

    val context = LocalContext.current


    Scaffold(
        floatingActionButtonPosition = FabPosition.End,
       floatingActionButton = {
           FloatingActionButton(
            onClick = {
                adding = !adding
            }) {
               Icon(
                   Icons.Filled.Add,
                   contentDescription = "Add Button",
                   tint = Color.White
               )
        }}
    , content = {


            Column(
                horizontalAlignment = CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth(),
            ) {



                Row(
                    horizontalArrangement = Arrangement.spacedBy(5.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    TextField(
                        label = { Text("Search Product") },
                        value = searchName,
                        onValueChange = onSearchTextChange,
                        modifier = Modifier.fillMaxWidth(0.7f)
                    )

                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Button(
                            onClick = {
                                viewModel.findByName("$searchName%")
                                searching = true
                            }
                        ) {
                            Text("Search")
                        }
                        if(searching) {
                            Button(
                                onClick = {
                                    searching = false
                                    productName = ""
                                }
                            ) {
                                Text("Clear")
                            }
                        }
                    }

                }

                AnimatedVisibility(visible = adding) {
                    Column (
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,

                        ) {

                        CustomTextField(
                            title = "Product Name",
                            textState = productName,
                            onTextChange = onProductTextChange,
                            keyboardType = KeyboardType.Text
                        )
                        CustomTextField(
                            title = "Product Quantity",
                            textState = productQuantity,
                            onTextChange = onQuantityTextChange,
                            keyboardType = KeyboardType.Number
                        )

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.CenterHorizontally),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {

                            Button(onClick = {
                                productName = ""
                                productQuantity = ""
                                adding = false
                            }) {
                                Text(text = "Cancel")
                            }

                            Button(onClick = {
                                try {
                                    viewModel.insertProduct(Product(productName,
                                        productQuantity.toInt()))
                                }catch (e: NumberFormatException){
                                    showToast(context, "Please input a Number inside the Quantity Field")
                                }
                                productName = ""
                                productQuantity = ""
                                adding = false
                            }) {
                                Text(text = "Add Product")
                            }
                        }
                    }
                }

                LazyColumn(
                    Modifier
                        .fillMaxWidth()
                ) {
                    val list = if (searching) searchResult else allProducts

                    item {
                        TitleRow(head1 = "ID", head2 = "Product", head3 = "Quantity")
                    }

                    items(list) { product ->
                        ProductRow(
                            id = product.id,
                            name = product.productName,
                            quantity = product.quantity,
                            viewModel = viewModel)
                    }
                }
            }
        }
    )

}

fun showToast(context: Context, message: String){
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}



