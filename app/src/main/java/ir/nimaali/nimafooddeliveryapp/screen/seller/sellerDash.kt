package ir.nimaali.nimafooddeliveryapp.screen.seller


import android.content.Context
import android.content.SharedPreferences
import android.provider.CalendarContract.Colors
import android.view.MenuItem
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import ir.nimaali.nimafooddeliveryapp.ui.theme.BackgroundColor
import ir.nimaali.nimafooddeliveryapp.ui.theme.PrimaryColor
import ir.nimaali.nimafooddeliveryapp.ui.theme.SurfaceColor
import ir.nimaali.nimafooddeliveryapp.ui.theme.vazirFontFamily
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SellerDashboardScreen(navController: NavHostController) {
    var isStoreOpen by remember { mutableStateOf(true) }
    var showBottomSheet by remember { mutableStateOf(false) }

    val orders = listOf(
        mapOf(
            "customerName" to "علی رضایی",
            "orderStatus" to "در انتظار تایید",
            "foods" to listOf(
                mapOf("name" to "پیتزا", "quantity" to 2),
                mapOf("name" to "ساندویچ", "quantity" to 1)
            ),
            "address" to "خیابان ولیعصر، پلاک ۱۲۳",
            "isApproved" to false,
            "orderTime" to "۱۴:۳۰ - ۱۰ آذر ۱۴۰۲"
        ), mapOf(
            "customerName" to "مریم احمدی",
            "orderStatus" to "تایید شده",
            "foods" to listOf(
                mapOf("name" to "برگر", "quantity" to 1)
            ),
            "address" to "بلوار کشاورز، پلاک ۷۸",
            "isApproved" to true,
            "orderTime" to "۱۵:۱۵ - ۱۰ آذر ۱۴۰۲"
        )
    )

    Scaffold(topBar = {
        TopAppBar(title = {
            Text(
                "داشبورد فروشنده",
                style = MaterialTheme.typography.headlineSmall,
                fontFamily = vazirFontFamily,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                color = Color.White
            )

        },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF4CAF50)),
            navigationIcon = {
                IconButton(onClick = {
                    showBottomSheet=true
                }) {
                    Icon(
                        Icons.Default.AddCircle,
                        contentDescription = "افزودن",
                        modifier = Modifier.size(40.dp),
                        tint = Color.White
                    )
                }
            },
            actions = {
                IconButton(onClick = { /* Open Menu */ }) {
                    Icon(
                        Icons.Default.Refresh,
                        contentDescription = "بارگذاری دوباره",
                        modifier = Modifier.size(40.dp),
                        tint = Color.White
                    )
                }
            })
    }) { innerPadding ->
        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = { showBottomSheet = false },
                sheetState = rememberModalBottomSheetState(
                    skipPartiallyExpanded = true
                )
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                ) {
                    MenuItem(icon = Icons.Default.AddCircle, title = "لیست غذاها", onClick = {
                        navController.navigate("/seller/list/food")
                        showBottomSheet = false
                    })
                    MenuItem(icon = Icons.Default.AddCircle, title = "افزودن غذا", onClick = {
                        navController.navigate("/seller/add/food")
                        showBottomSheet = false
                    })
                    MenuItem(icon = Icons.Default.AddCircle, title = "تاریخچه سفارش", onClick = {
                        navController.navigate("/seller/order/history")
                        showBottomSheet = false
                    })
                    MenuItem(icon = Icons.Default.AddCircle, title = "درآمد", onClick = {
                        navController.navigate("/seller/payment")
                        showBottomSheet = false
                    })
                }
            }
        }

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = if (isStoreOpen) Icons.Default.CheckCircle else Icons.Default.Close,
                        contentDescription = null,
                        tint = if (isStoreOpen) Color.Green else Color.Red
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "وضعیت فروشگاه: ${if (isStoreOpen) "باز" else "بسته"}",
                        color = if (isStoreOpen) Color(0xFF013220) else Color.Red,
                        style = MaterialTheme.typography.titleMedium,
                        fontFamily = vazirFontFamily
                    )
                }
                Button(
                    onClick = { isStoreOpen = !isStoreOpen },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isStoreOpen) Color.Red else Color(0xFF013220)
                    )
                ) {
                    Text(
                        if (isStoreOpen) "بستن فروشگاه" else "باز کردن فروشگاه",
                        fontFamily = vazirFontFamily,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }

            if (orders.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
                ) {
                    Text(
                        "هیچ سفارشی موجود نیست",
                        style = MaterialTheme.typography.titleLarge,
                        fontFamily = vazirFontFamily
                    )
                }
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(orders) { order ->
                        val customerName = order["customerName"] as String
                        val orderStatus = order["orderStatus"] as String
                        val address = order["address"] as String
                        val orderTime = order["orderTime"] as String
                        val foods = order["foods"] as List<Map<String, Any>>
                        val isApproved = order["isApproved"] as Boolean

                        Card(
                            modifier = Modifier
                                .padding(8.dp)
                                .fillMaxWidth(),
                            elevation = CardDefaults.cardElevation(4.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    "نام سفارش‌دهنده: $customerName",
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontFamily = vazirFontFamily
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    "وضعیت سفارش: $orderStatus",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = if (isApproved) Color(0xFF013220) else Color.Red
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    "آدرس: $address",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontFamily = vazirFontFamily
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    "زمان سفارش: $orderTime",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontFamily = vazirFontFamily
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    "لیست غذاها:",
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontFamily = vazirFontFamily
                                )
                                foods.forEach { food ->
                                    val foodName = food["name"] as String
                                    val quantity = food["quantity"] as Int
                                    Text(
                                        "$foodName - $quantity عدد",
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontFamily = vazirFontFamily
                                    )
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Row(
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    if (!isApproved) {
                                        Button(
                                            onClick = { /* تایید سفارش */ },
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = Color(0xFF013220)
                                            )
                                        ) {
                                            Text(
                                                "تایید سفارش", fontFamily = vazirFontFamily
                                            )
                                        }

                                        Spacer(modifier = Modifier.width(16.dp))
                                    }
                                    Button(
                                        onClick = { /* مشاهده جزئیات */ },
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

@Composable
fun MenuItem(icon: ImageVector, title: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(34.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = title,
            fontSize = 18.sp,
            fontFamily = vazirFontFamily
        )
    }
}
