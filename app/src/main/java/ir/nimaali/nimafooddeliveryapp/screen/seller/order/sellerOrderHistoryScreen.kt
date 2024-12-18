package ir.nimaali.nimafooddeliveryapp.screen.seller.order


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


val orders = listOf(
    mapOf(
        "customerName" to "علی احمدی",
        "orderStatus" to "در انتظار تایید",
        "address" to "تهران، خیابان ولیعصر",
        "orderTime" to "2024-12-14 12:00",
        "foods" to listOf(
            mapOf("name" to "پیتزا", "quantity" to 2),
            mapOf("name" to "ساندویچ", "quantity" to 1)
        ),
        "isApproved" to false
    ),
    mapOf(
        "customerName" to "مریم محمدی",
        "orderStatus" to "تایید شده",
        "address" to "تهران، خیابان انقلاب",
        "orderTime" to "2024-12-14 14:30",
        "foods" to listOf(
            mapOf("name" to "برگر", "quantity" to 1),
            mapOf("name" to "سیب‌زمینی", "quantity" to 1)
        ),
        "isApproved" to true
    ), mapOf(
        "customerName" to "علی احمدی",
        "orderStatus" to "در انتظار تایید",
        "address" to "تهران، خیابان ولیعصر",
        "orderTime" to "2024-12-14 12:00",
        "foods" to listOf(
            mapOf("name" to "پیتزا", "quantity" to 2),
            mapOf("name" to "ساندویچ", "quantity" to 1)
        ),
        "isApproved" to false
    ),
    mapOf(
        "customerName" to "مریم محمدی",
        "orderStatus" to "تایید شده",
        "address" to "تهران، خیابان انقلاب",
        "orderTime" to "2024-12-14 14:30",
        "foods" to listOf(
            mapOf("name" to "برگر", "quantity" to 1),
            mapOf("name" to "سیب‌زمینی", "quantity" to 1)
        ),
        "isApproved" to true
    )
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SellerOrderHistoryScreen(navController: NavHostController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "تاریخچه سفارش",
                        style = MaterialTheme.typography.headlineSmall,
                        fontFamily = vazirFontFamily,
                        textAlign = TextAlign.Center,
                        color = Color.White,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF4CAF50)),
                actions = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = "برگشت",
                            modifier = Modifier.size(42.dp),
                            tint = Color.White
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        // اینجا از innerPadding برای فاصله‌دهی استفاده می‌کنیم
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding) // فاصله‌دهی به محتویات داخل Scaffold
        ) {
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
                        // نمایش نام سفارش‌دهنده
                        Text(
                            "نام سفارش‌دهنده: $customerName",
                            style = MaterialTheme.typography.bodyLarge,
                            fontFamily = vazirFontFamily
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        // نمایش وضعیت سفارش
                        Text(
                            "وضعیت سفارش: $orderStatus",
                            style = MaterialTheme.typography.bodyMedium,
                            color = if (isApproved) Color(0xFF013220) else Color.Red
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        // نمایش آدرس
                        Text(
                            "آدرس: $address",
                            style = MaterialTheme.typography.bodyMedium,
                            fontFamily = vazirFontFamily
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        // نمایش زمان سفارش
                        Text(
                            "زمان سفارش: $orderTime",
                            style = MaterialTheme.typography.bodyMedium,
                            fontFamily = vazirFontFamily
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        // لیست غذاها
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

                        // دکمه‌های تایید و جزئیات
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            if (!isApproved) {
                                Button(
                                    onClick = { /* تایید سفارش */ },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(0xFF013220)
                                    )
                                ) {
                                    Text("تایید سفارش", fontFamily = vazirFontFamily)
                                }

                                Spacer(modifier = Modifier.width(16.dp))
                            }

                            Button(
                                onClick = { /* مشاهده جزئیات */ },
                                colors = ButtonDefaults.buttonColors(containerColor = Color.Blue)
                            ) {
                                Text("جزئیات سفارش", fontFamily = vazirFontFamily)
                            }
                        }
                    }
                }
            }
        }
    }
}


