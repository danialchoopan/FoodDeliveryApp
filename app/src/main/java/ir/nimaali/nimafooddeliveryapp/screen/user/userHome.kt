package ir.nimaali.nimafooddeliveryapp.screen.user

import android.content.Context
import android.content.SharedPreferences
import android.provider.CalendarContract.Colors
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import ir.nimaali.nimafooddeliveryapp.data.home.HomeRestaurantOderRequestGroup
import ir.nimaali.nimafooddeliveryapp.data.user.UserAuthRequestGroup
import ir.nimaali.nimafooddeliveryapp.models.home.Restaurant
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
                0 -> RestaurantsScreen() // Ensure no Scaffold is used inside these screens
                1 -> OrdersScreen()
                2 -> ProfileScreen(navController)
            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RestaurantsScreen() {

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
            loading=false
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
    ) { innerPadding ->
        if (loading) {
            LoadingProgressbar {

            }
        }else {
            if (errorMessage.isNotEmpty() or listRestaurant.isNotEmpty()) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
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
                        .padding(innerPadding)
                        .padding(16.dp)
                ) {
                    items(listRestaurant) { restaurant ->
                        Card(
                            modifier = Modifier
                                .padding(vertical = 8.dp)
                                .fillMaxWidth(),
                            elevation = CardDefaults.cardElevation(4.dp)
                        ) {

                            ListItem(
                                headlineContent = {
                                    Text(
                                        text = restaurant.name,
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                },
                                supportingContent = {
                                    Text(
                                        text = restaurant.category,
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontFamily = vazirFontFamily
                                    )
                                },
                                leadingContent = {
                                    Icon(
                                        imageVector = if (restaurant.open) Icons.Default.CheckCircle else Icons.Default.Close,
                                        contentDescription = null,
                                        tint = if (restaurant.open) Color.Green else Color.Red
                                    )
                                },
                                trailingContent = {
                                    Text(
                                        text = if (restaurant.open) "باز است" else "بسته است",
                                        color = if (restaurant.open) Color.Green else Color.Red,
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontFamily = vazirFontFamily
                                    )
                                },
                                modifier = Modifier.padding(8.dp)
                            )


                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrdersScreen() {
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
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("صفحه سفارش‌ها", style = MaterialTheme.typography.bodyLarge)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController
) {
    val authUserSellerViewModel: AuthUserSellerViewModel = viewModel()
    val m_context = LocalContext.current
    var onGoingProgress by remember {
        mutableStateOf(true)
    }
//
//    if (onGoingProgress) {
//        DialogBoxLoading()
//    }

    val userSharedPreferences =
        m_context.getSharedPreferences("app_data", Context.MODE_PRIVATE)
    val userData = authUserSellerViewModel.getUserLoginData(m_context)

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
            }
        }
    }
}


