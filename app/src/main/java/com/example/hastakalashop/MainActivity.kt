package com.example.hastakalashop

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            var currentScreen by remember { mutableStateOf("splash") }
            var selectedProduct by remember { mutableStateOf<Product?>(null) }

            val cart = remember { mutableStateListOf<Product>() }

            when (currentScreen) {

                "splash" -> SplashScreen {
                    currentScreen = "login"
                }

                "login" -> LoginScreen {
                    currentScreen = "home"
                }

                "details" -> ProductDetailScreen(
                    product = selectedProduct!!,
                    onBack = {
                        currentScreen = "home"
                    },
                    onAddToCart = {
                        cart.add(it)
                    }
                )

                "cart" -> CartScreen(
                    cart = cart,
                    onBack = {
                        currentScreen = "home"
                    }
                )

                "home" -> HomeScreen(
                    cart = cart,
                    onProductClick = {
                        selectedProduct = it
                        currentScreen = "details"
                    },
                    onCartClick = {
                        currentScreen = "cart"
                    }
                )
            }
        }
    }
}

//////////////////////////////////////////////////////////
// FONT
//////////////////////////////////////////////////////////

val limelightFont = FontFamily(
    Font(R.font.limelight_regular)
)

//////////////////////////////////////////////////////////
// DATA CLASS
//////////////////////////////////////////////////////////

data class Product(
    val name: String,
    val description: String,
    val price: Int,
    val category: String,
    val images: List<Int>
)

//////////////////////////////////////////////////////////
// SPLASH SCREEN
//////////////////////////////////////////////////////////

@Composable
fun SplashScreen(onFinish: () -> Unit) {

    var startAnimation by remember { mutableStateOf(false) }

    val scale = animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0.5f,
        animationSpec = tween(1000),
        label = ""
    )

    val alpha = animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(1000),
        label = ""
    )

    LaunchedEffect(true) {

        startAnimation = true
        delay(2000)
        onFinish()
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo",
            modifier = Modifier
                .size(220.dp)
                .scale(scale.value)
                .alpha(alpha.value)
        )
    }
}

//////////////////////////////////////////////////////////
// LOGIN SCREEN
//////////////////////////////////////////////////////////

@Composable
fun LoginScreen(onLoginSuccess: () -> Unit) {

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    Box(modifier = Modifier.fillMaxSize()) {

        Image(
            painter = painterResource(id = R.drawable.login_bg),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                text = "HastaKala Shop",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontFamily = limelightFont,
                    letterSpacing = 2.sp
                ),
                color = Color.White
            )

            Spacer(modifier = Modifier.height(30.dp))

            OutlinedTextField(
                value = username,
                onValueChange = {
                    username = it
                },
                label = {
                    Text("Email")
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(15.dp))

            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                },
                label = {
                    Text("Password")
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(25.dp))

            Button(
                onClick = {

                    if (
                        username.isNotEmpty() &&
                        password.length >= 6
                    ) {

                        onLoginSuccess()

                    } else {

                        errorMessage =
                            "Enter valid email and password"
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {

                Text("Login")
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = errorMessage,
                color = Color.Red
            )
        }
    }
}

//////////////////////////////////////////////////////////
// HOME SCREEN
//////////////////////////////////////////////////////////

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    cart: List<Product>,
    onProductClick: (Product) -> Unit,
    onCartClick: () -> Unit
) {

    var searchText by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("All") }

    var showGreeting by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        delay(2000)
        showGreeting = false
    }

    val products = listOf(

        Product(
            "Terracotta Pot",
            "Beautiful handmade clay pot",
            500,
            "Pottery",
            listOf(R.drawable.pot, R.drawable.clay2, R.drawable.pot3)
        ),

        Product(
            "Clay Vase",
            "Traditional handcrafted vase",
            800,
            "Pottery",
            listOf(R.drawable.clay, R.drawable.pot2)
        ),

        Product(
            "Canvas Art",
            "Handcrafted painting",
            2000,
            "Paintings",
            listOf(R.drawable.painting, R.drawable.painting2)
        ),

        Product(
            "Decor Plate",
            "Traditional wall decor",
            1200,
            "Decor",
            listOf(R.drawable.pot, R.drawable.clay2)
        ),

        Product(
            "Clay Mug",
            "Eco-friendly handmade mug",
            450,
            "Pottery",
            listOf(R.drawable.clay, R.drawable.pot2)
        ),

        Product(
            "Mini Pot",
            "Small decorative clay pot",
            300,
            "Pottery",
            listOf(R.drawable.pot3, R.drawable.pot)
        )
    )

    val filteredProducts = products.filter {

        (selectedCategory == "All" || it.category == selectedCategory)
                &&
                it.name.contains(searchText, ignoreCase = true)
    }

    Scaffold(

        topBar = {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
            ) {

                AnimatedVisibility(
                    visible = showGreeting
                ) {

                    Row(

                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFF6D4C41))
                            .padding(horizontal = 16.dp, vertical = 18.dp),

                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically

                    ) {

                        Column {

                            Text(
                                text = "Good Evening 👋",
                                color = Color.White,
                                style = MaterialTheme.typography.bodyLarge
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = "HastaKala",
                                color = Color.White,
                                fontFamily = limelightFont,
                                style = MaterialTheme.typography.headlineMedium
                            )
                        }

                        Button(
                            onClick = onCartClick,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.White
                            )
                        ) {

                            Text(
                                text = "🛒 ${cart.size}",
                                color = Color.Black
                            )
                        }
                    }
                }

                Row(

                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .padding(horizontal = 16.dp, vertical = 14.dp),

                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically

                ) {

                    Text(
                        text = "HastaKala Shop",
                        style = MaterialTheme.typography.titleLarge,
                        fontFamily = limelightFont,
                        color = Color(0xFF3E2723)
                    )

                    Button(
                        onClick = onCartClick
                    ) {

                        Text("🛒 ${cart.size}")
                    }
                }
            }
        }

    ) { padding ->

        LazyVerticalGrid(

            columns = GridCells.Fixed(2),

            modifier = Modifier
                .padding(padding)
                .padding(horizontal = 12.dp),

            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)

        ) {

            item(span = { GridItemSpan(2) }) {

                Column {

                    OutlinedTextField(
                        value = searchText,
                        onValueChange = {
                            searchText = it
                        },
                        placeholder = {
                            Text("Search Handmade Products")
                        },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {

                        items(
                            listOf(
                                "All",
                                "Pottery",
                                "Paintings",
                                "Decor"
                            )
                        ) { category ->

                            AssistChip(
                                onClick = {
                                    selectedCategory = category
                                },
                                label = {
                                    Text(category)
                                }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = "Popular Products",
                        style = MaterialTheme.typography.headlineSmall,
                        color = Color(0xFF3E2723)
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Handpicked handmade items for you",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                }
            }

            items(filteredProducts) { product ->

                ProductCard(product, onProductClick)
            }
        }
    }
}

//////////////////////////////////////////////////////////
// PRODUCT CARD
//////////////////////////////////////////////////////////

@Composable
fun ProductCard(
    product: Product,
    onClick: (Product) -> Unit
) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(10.dp)
    ) {

        Column {

            Image(
                painter = painterResource(id = product.images[0]),
                contentDescription = product.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier.padding(10.dp)
            ) {

                Text(
                    text = product.name,
                    fontFamily = limelightFont,
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "4.5 ★",
                    color = Color(0xFFFF9800)
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "₹${product.price}",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = product.category,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = {
                        onClick(product)
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Text("View")
                }
            }
        }
    }
}

//////////////////////////////////////////////////////////
// PRODUCT DETAIL SCREEN
//////////////////////////////////////////////////////////

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    product: Product,
    onBack: () -> Unit,
    onAddToCart: (Product) -> Unit
) {

    Scaffold(

        topBar = {

            TopAppBar(
                title = {
                    Text(
                        product.name,
                        fontFamily = limelightFont
                    )
                }
            )
        }

    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(20.dp)
        ) {

            LazyRow {

                items(product.images) { img ->

                    Image(
                        painter = painterResource(id = img),
                        contentDescription = null,
                        modifier = Modifier
                            .size(250.dp)
                            .padding(8.dp),
                        contentScale = ContentScale.Crop
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = product.name,
                fontFamily = limelightFont,
                style = MaterialTheme.typography.headlineSmall
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "₹${product.price}",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(product.description)

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    onAddToCart(product)
                },
                modifier = Modifier.fillMaxWidth()
            ) {

                Text("Add to Cart 🛒")
            }

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedButton(
                onClick = onBack,
                modifier = Modifier.fillMaxWidth()
            ) {

                Text("Back")
            }
        }
    }
}

//////////////////////////////////////////////////////////
// CART SCREEN
//////////////////////////////////////////////////////////

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    cart: MutableList<Product>,
    onBack: () -> Unit
) {

    val subtotal = cart.sumOf { it.price }
    val deliveryFee = 100
    val gst = (subtotal * 0.18).toInt()
    val total = subtotal + deliveryFee + gst

    Scaffold(

        topBar = {

            TopAppBar(
                title = {
                    Text(
                        "My Cart",
                        fontFamily = limelightFont
                    )
                }
            )
        }

    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {

            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {

                items(cart) { product ->

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 10.dp)
                    ) {

                        Row(
                            modifier = Modifier.padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {

                            Column {

                                Text(
                                    product.name,
                                    fontFamily = limelightFont
                                )

                                Text("₹${product.price}")
                            }

                            Button(
                                onClick = {
                                    cart.remove(product)
                                }
                            ) {

                                Text("Remove")
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Card(
                modifier = Modifier.fillMaxWidth()
            ) {

                Column(
                    modifier = Modifier.padding(16.dp)
                ) {

                    Text(
                        "Price Details",
                        style = MaterialTheme.typography.titleLarge,
                        fontFamily = limelightFont
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {

                        Text("Subtotal")
                        Text("₹$subtotal")
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {

                        Text("Delivery Fee")
                        Text("₹$deliveryFee")
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {

                        Text("GST")
                        Text("₹$gst")
                    }

                    Divider(
                        modifier = Modifier.padding(vertical = 10.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {

                        Text(
                            "Total",
                            style = MaterialTheme.typography.titleMedium
                        )

                        Text(
                            "₹$total",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = { },
                modifier = Modifier.fillMaxWidth()
            ) {

                Text("Checkout")
            }

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedButton(
                onClick = onBack,
                modifier = Modifier.fillMaxWidth()
            ) {

                Text("Back")
            }
        }
    }
}