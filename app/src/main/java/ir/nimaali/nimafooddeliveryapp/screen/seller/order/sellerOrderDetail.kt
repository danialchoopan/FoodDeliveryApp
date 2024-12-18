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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SellerOrderDetailScreen(
    navController: NavHostController,
//    customerName: String,
//    orderStatus: String,
//    address: String,
//    orderTime: String,
//    foods: List<Map<String, Any>>,
    isApproved: Boolean
) {
    val foods = listOf(
        mapOf("name" to "پیتزا", "quantity" to 2),
        mapOf("name" to "برگر", "quantity" to 3),
        mapOf("name" to "سالاد", "quantity" to 1),
        mapOf("name" to "پاستا", "quantity" to 4)
    )
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "جزئیات سفارش",
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
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Text(
                "نام سفارش‌دهنده: fsafasfasfas",
                style = MaterialTheme.typography.bodyLarge,
                fontFamily = vazirFontFamily
            )
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                "وضعیت سفارش: fsafsafasfasfas",
                style = MaterialTheme.typography.bodyMedium,
                color = if (isApproved) Color(0xFF013220) else Color.Red
            )
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                "آدرس: hgerherherherherhreeh",
                style = MaterialTheme.typography.bodyMedium,
                fontFamily = vazirFontFamily
            )
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                "زمان سفارش: herherherherherhe",
                style = MaterialTheme.typography.bodyMedium,
                fontFamily = vazirFontFamily
            )
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                "لیست غذاها:",
                style = MaterialTheme.typography.bodyLarge,
                fontFamily = vazirFontFamily
            )
            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn {
                items(foods) { food ->
                    val foodName = food["name"] as String
                    val quantity = food["quantity"] as Int
                    Text(
                        "$foodName - $quantity عدد",
                        style = MaterialTheme.typography.bodyMedium,
                        fontFamily = vazirFontFamily,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
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
                }

                Button(
                    onClick = { navController.popBackStack() },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Blue)
                ) {
                    Text("بازگشت", fontFamily = vazirFontFamily)
                }
            }
        }
    }
}

