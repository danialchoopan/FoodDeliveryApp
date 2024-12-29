package ir.nimaali.nimafooddeliveryapp.screen.user

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.provider.CalendarContract.Colors
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import ir.nimaali.nimafooddeliveryapp.data.RequestEndPoints
import ir.nimaali.nimafooddeliveryapp.data.home.HomeRestaurantOderRequestGroup
import ir.nimaali.nimafooddeliveryapp.data.user.UserAuthRequestGroup
import ir.nimaali.nimafooddeliveryapp.formatPrice
import ir.nimaali.nimafooddeliveryapp.models.home.Restaurant
import ir.nimaali.nimafooddeliveryapp.models.home.order.OrderListUsersAllItem
import ir.nimaali.nimafooddeliveryapp.screen.functions.LoadingProgressbar
import ir.nimaali.nimafooddeliveryapp.ui.theme.BackgroundColor
import ir.nimaali.nimafooddeliveryapp.ui.theme.PrimaryColor
import ir.nimaali.nimafooddeliveryapp.ui.theme.SurfaceColor
import ir.nimaali.nimafooddeliveryapp.ui.theme.vazirFontFamily
import ir.nimaali.nimafooddeliveryapp.viewmodel.AuthUserSellerViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserHomeScreen(navController: NavController) {
    val screens = listOf(
        "Restaurants",
        "Orders",
        "Profile"
    )
    var selectedIndex by remember { mutableStateOf(0) }

    Scaffold(
        bottomBar = {
            NavigationBar {
                screens.forEachIndexed { index, screen ->
                    NavigationBarItem(
                        selected = selectedIndex == index,
                        onClick = { selectedIndex = index },
                        icon = {
                            when (screen) {
                                "Restaurants" -> Icon(Icons.Default.Home, contentDescription = null)
                                "Orders" -> Icon(Icons.Default.List, contentDescription = null)
                                "Profile" -> Icon(Icons.Default.Person, contentDescription = null)
                            }
                        },
                        label = {
                            Text(
                                when (screen) {
                                    "Restaurants" -> "رستوران‌ها"
                                    "Orders" -> "سفارش‌ها"
                                    "Profile" -> "پروفایل"
                                    else -> ""
                                },
                                fontFamily = vazirFontFamily,
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when (selectedIndex) {
                0 -> RestaurantsScreen(navController) // Ensure no Scaffold is used inside these screens
                1 -> OrdersScreen(navController)
                2 -> ProfileScreen(navController)
            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)
@Composable
fun RestaurantsScreen(navController: NavController) {

    var errorMessage by remember { mutableStateOf("") }
    var listRestaurant by remember {
        mutableStateOf(emptyList<Restaurant>())
    }
    var loading by remember {
        mutableStateOf(true)
    }


    val m_context = LocalContext.current
    val authUserSellerViewModel: AuthUserSellerViewModel = viewModel()

    val usercity = authUserSellerViewModel.getUserLoginData(m_context).city_name

    val homePageRestaurantRequest = HomeRestaurantOderRequestGroup(m_context)

    homePageRestaurantRequest.homePageRestaurant { success, homePageRestaurant ->
        if (success) {
            loading = false
            listRestaurant = homePageRestaurant.restaurants
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "رستوران‌های $usercity", style = MaterialTheme.typography.headlineSmall,
                        fontFamily = vazirFontFamily,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        color = Color.White
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF4CAF50)),
            )
        }
    ) { _ ->

        if (loading) {
            LoadingProgressbar {

            }
        } else {
            if (listRestaurant.isEmpty()) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxSize()
                ) {
                    if (errorMessage.isNotEmpty()) {
                        Text(errorMessage, color = MaterialTheme.colorScheme.error)
                    }
                    if (listRestaurant.isEmpty()) {
                        Text(
                            "رستورانی در شهر شما قابل ارائه سرویس نمی باشد!",
                            color = MaterialTheme.colorScheme.error
                        )

                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    item {
                        Spacer(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(70.dp)
                        )
                    }
                    items(listRestaurant) { restaurant ->
                        Card(
                            modifier = Modifier
                                .padding(8.dp)
                                .clickable {
                                    navController.navigate("home/restaurant/${restaurant.id}")
                                },
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {
                            Column {
                                // Restaurant Image

                                GlideImage(
                                    model = RequestEndPoints.rootDomain + "/" + restaurant.image,
                                    contentDescription = "",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(150.dp),
                                )

                                // Restaurant Details
                                Column(
                                    modifier = Modifier.padding(8.dp)
                                ) {
                                    Text(
                                        text = restaurant.name,
                                        style = MaterialTheme.typography.titleMedium,
                                        modifier = Modifier.padding(bottom = 4.dp)
                                    )
                                    Text(
                                        text = restaurant.category,
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontFamily = vazirFontFamily
                                    )
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.End,
                                        modifier = Modifier
                                            .fillMaxWidth() // Ensures the Row takes up the full width of the parent
                                            .padding(top = 8.dp)
                                    ) {
                                        Icon(
                                            imageVector = if (restaurant.open) Icons.Default.CheckCircle else Icons.Default.Close,
                                            contentDescription = null,
                                            tint = if (restaurant.open) Color.Green else Color.Red
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = if (restaurant.open) "باز است" else "بسته است",
                                            color = if (restaurant.open) Color.DarkGray else Color.Red,
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontFamily = vazirFontFamily
                                        )
                                    }

                                }
                            }
                        }

                    }
                }
            }

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrdersScreen(navController: NavController) {
    val m_context = LocalContext.current
    val homeRestaurantOderRequestGroup = HomeRestaurantOderRequestGroup(m_context)

    var listOrders by remember {
        mutableStateOf(emptyList<OrderListUsersAllItem>())
    }

    var loading by remember {
        mutableStateOf(true)
    }

    homeRestaurantOderRequestGroup.getShowOrdersUser {
        listOrders = it
        loading = false
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "سفارش‌ها", style = MaterialTheme.typography.headlineSmall,
                        fontFamily = vazirFontFamily,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        color = Color.White
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF4CAF50))
            )
        }
    ) { innerPadding ->
        if (loading) {
            LoadingProgressbar {

            }
        } else {
            if (listOrders.isEmpty()) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("شما سفارش در حال انجام ندارید!")
                }
            }
            Box(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
                contentAlignment = Alignment.TopCenter
            ) {
                LazyColumn {
                    items(listOrders) { order ->
                        Card(
                            modifier = Modifier
                                .padding(8.dp)
                                .fillMaxWidth(),
                            elevation = CardDefaults.cardElevation(4.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    "وضعیت سفارش: ${order.status} ",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = if (order.status == "تایید شده") Color.DarkGray else Color.Red
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    " نام رستوران : " + order.seller_name,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontFamily = vazirFontFamily
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    "زمان سفارش: ${order.orderDate}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontFamily = vazirFontFamily
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Spacer(modifier = Modifier.height(8.dp))
                                Row(
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    Button(
                                        onClick = {
                                            navController.navigate("user/order/" + order.id)
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = Color.Blue)
                                    ) {
                                        Text(
                                            "جزئیات سفارش", fontFamily = vazirFontFamily
                                        )
                                    }
                                }
                            }
                        }
                    }

                }

            }

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController,
) {
    val authUserSellerViewModel: AuthUserSellerViewModel = viewModel()
    val m_context = LocalContext.current
    var onGoingProgress by remember {
        mutableStateOf(true)
    }

    val userSharedPreferences =
        m_context.getSharedPreferences("app_data", Context.MODE_PRIVATE)
    val userData = authUserSellerViewModel.getUserLoginData(m_context)

    val homeRestaurantOderRequestGroup=HomeRestaurantOderRequestGroup(m_context)
    var userTotal by remember {
        mutableStateOf("")
    }
    var userSuccessOrder by remember {
        mutableStateOf("")
    }
    var userFailedOrder by remember {
        mutableStateOf("")
    }
    homeRestaurantOderRequestGroup.getUserOrderData { success, failed, totalOrder ->
        userTotal=totalOrder
        userSuccessOrder=success
        userFailedOrder=failed

    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "پروفایل کاربری", style = MaterialTheme.typography.headlineSmall,
                        fontFamily = vazirFontFamily,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        color = Color.White
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF4CAF50)),
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp)
                        .border(1.dp, Color.Gray)
                        .background(Color.White)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(0.8f)
                                .padding(vertical = 5.dp, horizontal = 20.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "",
                                modifier = Modifier.size(70.dp)
                            )
                            Column {
                                Text(text = userData.name, fontSize = 18.sp)
                                Text(text = userData.phone, fontSize = 14.sp)
                            }
                        }

                        IconButton(onClick = {
                            onGoingProgress = true
                            userSharedPreferences.edit().let {
                                it.clear()
                                it.apply()
                                onGoingProgress = false
                            }
                            navController.navigate("user_login") {
                                popUpTo(0)
                            }
                        }) {
                            Icon(
                                imageVector = Icons.Default.ExitToApp,
                                tint = Color.Red,
                                contentDescription = "",
                                modifier = Modifier.size(50.dp)
                            )
                        }
                    }

                    Row(
                        Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedButton(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp), onClick = {
                                navController.navigate(
                                    "home/user/edit"
                                )
                            }) {
                            Text(
                                text = "ویرایش اطلاعات کاربری",
                                fontSize = 18.sp,
                                fontFamily = vazirFontFamily
                            )
                        }
                    }
                }

                // نمایش اطلاعات مربوط به سفارش‌ها
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                        .border(1.dp, Color.Gray)
                        .background(Color.White)
                ) {
                    Spacer(modifier = Modifier.fillMaxWidth().height(8.dp))
                    // نمایش مجموع قیمت سفارش‌ها
                    Text(
                        text = "مجموع قیمت سفارش‌ها: " + formatPrice(userTotal) + " تومان ",
                        fontSize = 16.sp,
                        fontFamily = vazirFontFamily,
                        modifier = Modifier.padding(10.dp)
                    )

                    // نمایش تعداد سفارش‌های موفق
                    Text(
                        text = "تعداد سفارش‌های موفق: "+ userSuccessOrder,
                        fontSize = 16.sp,
                        fontFamily = vazirFontFamily,
                        modifier = Modifier.padding(10.dp)
                    )

                    // نمایش تعداد سفارش‌های لغو شده
                    Text(
                        text = "تعداد سفارش‌های لغو شده: "+ userFailedOrder,
                        fontSize = 16.sp,
                        fontFamily = vazirFontFamily,
                        modifier = Modifier.padding(10.dp)
                    )

                    Spacer(modifier = Modifier.fillMaxWidth().height(16.dp))

                    OutlinedButton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        onClick = {
                            navController.navigate("user/orders/all")
                        }
                    ) {
                        Text(
                            text = "تاریخچه سفارش های کاربر",
                            fontSize = 18.sp,
                            fontFamily = vazirFontFamily
                        )
                    }

                    // دکمه "تماس با ما"
                    OutlinedButton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        onClick = {

                            val dialIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:+989991112233"))
                            m_context.startActivity(dialIntent)
                        }
                    ) {
                        Text(
                            text = "تماس با ما",
                            fontSize = 18.sp,
                            fontFamily = vazirFontFamily
                        )
                    }

                    // دکمه "درباره ما"
                    OutlinedButton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        onClick = {
                            // اضافه کردن عملکرد مورد نظر برای "درباره ما"
                            navController.navigate("home/about/us")
                        }
                    ) {
                        Text(
                            text = "درباره ما",
                            fontSize = 18.sp,
                            fontFamily = vazirFontFamily
                        )
                    }


                    Spacer(modifier = Modifier.fillMaxWidth().height(25.dp))
                }

            }
        }
    }
}


