package ir.nimaali.nimafooddeliveryapp.screen.seller.order


import android.content.Context
import android.content.SharedPreferences
import android.provider.CalendarContract.Colors
import android.view.MenuItem
import android.widget.Toast
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
import ir.nimaali.nimafooddeliveryapp.data.seller.SellerHomeRequestGroup
import ir.nimaali.nimafooddeliveryapp.models.seller.dash.SellerOrdersDashShowItem
import ir.nimaali.nimafooddeliveryapp.screen.functions.LoadingProgressbar
import ir.nimaali.nimafooddeliveryapp.ui.theme.BackgroundColor
import ir.nimaali.nimafooddeliveryapp.ui.theme.PrimaryColor
import ir.nimaali.nimafooddeliveryapp.ui.theme.SurfaceColor
import ir.nimaali.nimafooddeliveryapp.ui.theme.vazirFontFamily
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SellerOrderHistoryScreen(navController: NavHostController) {

    val m_context = LocalContext.current

    val sellerHomeRequestGroup = SellerHomeRequestGroup(m_context)

    var onGoingProgress by remember {
        mutableStateOf(true)
    }
    var listOrders by remember {
        mutableStateOf(emptyList<SellerOrdersDashShowItem>())
    }


    sellerHomeRequestGroup.getAllOrderSeller {
        listOrders=it
        onGoingProgress=false
    }

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
        if (onGoingProgress) {

            LoadingProgressbar {

            }

        } else {
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            ) {
                if (listOrders.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "شما هیچ سفارش در حال انجام ندارید!.\n برای دریافت سفارش لطفا عذا به رستوران خود اضافه کنید",
                            style = MaterialTheme.typography.titleLarge,
                            fontFamily = vazirFontFamily
                        )
                    }
                } else {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(listOrders) { order ->
                            val customerName =order.userName
                            val orderStatus = order.status
                            val address = order.userAddress
                            val orderTime = order.orderDate
                            val foods = order.foodDetails

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
                                        color = Color.Gray
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
                                        val foodName = food.foodName
                                        val quantity = food.quantity
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
                                        if (orderStatus=="تایید رستوران") {
                                            Button(
                                                onClick = {
                                                    sellerHomeRequestGroup.setGettingOrderReady(order.orderId.toString()){
                                                        Toast.makeText(m_context,"سفارش توسط شما تایید شد",
                                                            Toast.LENGTH_SHORT).show()
                                                        sellerHomeRequestGroup.getNotCompleteOrderSeller {
                                                            listOrders=it
                                                            onGoingProgress=false
                                                        }
                                                    }
                                                },
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
                                            onClick = {
                                                navController.navigate("seller/order/detail/"+order.orderId)
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
}


