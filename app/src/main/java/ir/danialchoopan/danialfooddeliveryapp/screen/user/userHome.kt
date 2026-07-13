package ir.danialchoopan.danialfooddeliveryapp.screen.user

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import ir.danialchoopan.danialfooddeliveryapp.data.RequestEndPoints
import ir.danialchoopan.danialfooddeliveryapp.data.home.HomeRestaurantOderRequestGroup
import ir.danialchoopan.danialfooddeliveryapp.data.user.UserAuthRequestGroup
import ir.danialchoopan.danialfooddeliveryapp.formatPrice
import ir.danialchoopan.danialfooddeliveryapp.models.home.Restaurant
import ir.danialchoopan.danialfooddeliveryapp.models.home.order.OrderListUsersAllItem
import ir.danialchoopan.danialfooddeliveryapp.screen.functions.*
import ir.danialchoopan.danialfooddeliveryapp.ui.theme.*
import ir.danialchoopan.danialfooddeliveryapp.viewmodel.AuthUserSellerViewModel
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
        containerColor = BackgroundColor,
        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
                tonalElevation = 8.dp
            ) {
                screens.forEachIndexed { index, screen ->
                    NavigationBarItem(
                        selected = selectedIndex == index,
                        onClick = { selectedIndex = index },
                        icon = {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(
                                        if (selectedIndex == index) PrimaryColor.copy(alpha = 0.12f)
                                        else Color.Transparent
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    when (screen) {
                                        "Restaurants" -> Icons.Default.Home
                                        "Orders" -> Icons.Default.List
                                        "Profile" -> Icons.Default.Person
                                        else -> Icons.Default.Home
                                    },
                                    contentDescription = null,
                                    tint = if (selectedIndex == index) PrimaryColor else TextSecondary
                                )
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
                                style = MaterialTheme.typography.bodySmall,
                                color = if (selectedIndex == index) PrimaryColor else TextSecondary
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = PrimaryColor,
                            unselectedIconColor = TextSecondary,
                            indicatorColor = PrimaryColor.copy(alpha = 0.12f)
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when (selectedIndex) {
                0 -> RestaurantsScreen(navController)
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
        containerColor = BackgroundColor,
        topBar = {
            GradientTopBar(
                title = "رستوران‌های $usercity"
            )
        }
    ) { _ ->

        if (loading) {
            LoadingProgressbar {

            }
        } else {
            if (listRestaurant.isEmpty()) {
                EmptyState(
                    message = if (errorMessage.isNotEmpty()) errorMessage
                    else "رستورانی در شهر شما قابل ارائه سرویس نمی باشد!"
                )
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
                        ModernCard(
                            modifier = Modifier.padding(8.dp),
                            onClick = {
                                navController.navigate("home/restaurant/${restaurant.id}")
                            }
                        ) {
                            Column {
                                GlideImage(
                                    model = RequestEndPoints.rootDomain + "/" + restaurant.image,
                                    contentDescription = "",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(150.dp),
                                )

                                Column(
                                    modifier = Modifier.padding(12.dp)
                                ) {
                                    Text(
                                        text = restaurant.name,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontFamily = vazirFontFamily,
                                        color = TextPrimary,
                                        modifier = Modifier.padding(bottom = 4.dp)
                                    )
                                    Text(
                                        text = restaurant.category,
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontFamily = vazirFontFamily,
                                        color = TextSecondary
                                    )
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.End,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(top = 8.dp)
                                    ) {
                                        PillBadge(
                                            text = if (restaurant.open) "باز است" else "بسته است",
                                            backgroundColor = if (restaurant.open) SuccessColor else ErrorColor,
                                            textColor = Color.White
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
        containerColor = BackgroundColor,
        topBar = {
            GradientTopBar(
                title = "سفارش‌ها"
            )
        }
    ) { innerPadding ->
        if (loading) {
            LoadingProgressbar {

            }
        } else {
            if (listOrders.isEmpty()) {
                EmptyState(message = "شما سفارش در حال انجام ندارید!")
            }
            Box(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
                contentAlignment = Alignment.TopCenter
            ) {
                LazyColumn {
                    items(listOrders) { order ->
                        ModernCard(
                            modifier = Modifier
                                .padding(8.dp)
                                .fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        "نام رستوران : " + order.seller_name,
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontFamily = vazirFontFamily,
                                        color = TextPrimary
                                    )
                                    StatusChip(status = order.status)
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    "زمان سفارش: ${order.orderDate}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontFamily = vazirFontFamily,
                                    color = TextSecondary
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                GradientButton(
                                    text = "جزئیات سفارش",
                                    onClick = {
                                        navController.navigate("user/order/" + order.id)
                                    }
                                )
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

    val homeRestaurantOderRequestGroup = HomeRestaurantOderRequestGroup(m_context)
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
        userTotal = totalOrder
        userSuccessOrder = success
        userFailedOrder = failed

    }
    Scaffold(
        containerColor = BackgroundColor,
        topBar = {
            GradientTopBar(
                title = "پروفایل کاربری"
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
                    .padding(16.dp)
            ) {
                // Profile Header Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.Transparent)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(GradientCard)
                            .padding(20.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(0.75f),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(60.dp)
                                        .clip(CircleShape)
                                        .background(Color.White.copy(alpha = 0.3f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Person,
                                        contentDescription = "",
                                        modifier = Modifier.size(36.dp),
                                        tint = Color.White
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = userData.name,
                                        fontSize = 18.sp,
                                        color = Color.White,
                                        fontFamily = vazirFontFamily
                                    )
                                    Text(
                                        text = userData.phone,
                                        fontSize = 14.sp,
                                        color = Color.White.copy(alpha = 0.8f)
                                    )
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
                                    tint = Color.White,
                                    contentDescription = "",
                                    modifier = Modifier.size(28.dp)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Edit Profile Button
                OutlinedButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp)),
                    onClick = {
                        navController.navigate("home/user/edit")
                    },
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = PrimaryColor
                    ),
                    border = ButtonDefaults.outlinedButtonBorder.copy(
                        brush = androidx.compose.ui.graphics.SolidColor(PrimaryColor)
                    )
                ) {
                    Text(
                        text = "ویرایش اطلاعات کاربری",
                        fontSize = 16.sp,
                        fontFamily = vazirFontFamily
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Order Stats Card
                ModernCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(0.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "آمار سفارش‌ها",
                            style = MaterialTheme.typography.titleMedium,
                            fontFamily = vazirFontFamily,
                            color = TextPrimary,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = formatPrice(userTotal),
                                    style = MaterialTheme.typography.titleMedium,
                                    color = PrimaryColor,
                                    fontFamily = vazirFontFamily
                                )
                                Text(
                                    text = "مجموع قیمت (تومان)",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextSecondary,
                                    fontFamily = vazirFontFamily
                                )
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = userSuccessOrder,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = SuccessColor,
                                    fontFamily = vazirFontFamily
                                )
                                Text(
                                    text = "موفق",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextSecondary,
                                    fontFamily = vazirFontFamily
                                )
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = userFailedOrder,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = ErrorColor,
                                    fontFamily = vazirFontFamily
                                )
                                Text(
                                    text = "لغو شده",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextSecondary,
                                    fontFamily = vazirFontFamily
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Action Buttons
                ModernCard(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        OutlinedButton(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp, vertical = 4.dp),
                            onClick = {
                                navController.navigate("user/orders/all")
                            },
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = PrimaryColor
                            ),
                            border = ButtonDefaults.outlinedButtonBorder.copy(
                                brush = androidx.compose.ui.graphics.SolidColor(BorderColor)
                            )
                        ) {
                            Text(
                                text = "تاریخچه سفارش های کاربر",
                                fontSize = 16.sp,
                                fontFamily = vazirFontFamily
                            )
                        }

                        OutlinedButton(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp, vertical = 4.dp),
                            onClick = {
                                val dialIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:+989991112233"))
                                m_context.startActivity(dialIntent)
                            },
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = PrimaryColor
                            ),
                            border = ButtonDefaults.outlinedButtonBorder.copy(
                                brush = androidx.compose.ui.graphics.SolidColor(BorderColor)
                            )
                        ) {
                            Text(
                                text = "تماس با ما",
                                fontSize = 16.sp,
                                fontFamily = vazirFontFamily
                            )
                        }

                        OutlinedButton(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp, vertical = 4.dp),
                            onClick = {
                                navController.navigate("home/about/us")
                            },
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = PrimaryColor
                            ),
                            border = ButtonDefaults.outlinedButtonBorder.copy(
                                brush = androidx.compose.ui.graphics.SolidColor(BorderColor)
                            )
                        ) {
                            Text(
                                text = "درباره ما",
                                fontSize = 16.sp,
                                fontFamily = vazirFontFamily
                            )
                        }
                    }
                }
            }
        }
    }
}
