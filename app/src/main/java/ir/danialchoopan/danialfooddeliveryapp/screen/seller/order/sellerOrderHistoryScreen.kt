package ir.danialchoopan.danialfooddeliveryapp.screen.seller.order

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import ir.danialchoopan.danialfooddeliveryapp.data.seller.SellerHomeRequestGroup
import ir.danialchoopan.danialfooddeliveryapp.models.seller.dash.SellerOrdersDashShowItem
import ir.danialchoopan.danialfooddeliveryapp.screen.functions.*
import ir.danialchoopan.danialfooddeliveryapp.ui.theme.*


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
        containerColor = BackgroundColor,
        topBar = {
            GradientTopBar(
                title = "تاریخچه سفارش",
                actions = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = "برگشت",
                            modifier = Modifier.size(42.dp),
                            tint = MaterialTheme.colorScheme.onPrimary
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
                    EmptyState(
                        message = "شما هیچ سفارش در حال انجام ندارید!.\n برای دریافت سفارش لطفا عذا به رستوران خود اضافه کنید",
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(listOrders) { order ->
                            val customerName =order.userName
                            val orderStatus = order.status
                            val address = order.userAddress
                            val orderTime = order.orderDate
                            val foods = order.foodDetails

                            ModernCard(
                                modifier = Modifier
                                    .padding(8.dp)
                                    .fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text(
                                        "نام سفارش‌دهنده: $customerName",
                                        style = MaterialTheme.typography.bodyLarge,
                                        fontFamily = vazirFontFamily,
                                        color = TextPrimary
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    StatusChip(status = orderStatus)
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        "آدرس: $address",
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontFamily = vazirFontFamily,
                                        color = TextSecondary
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        "زمان سفارش: $orderTime",
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontFamily = vazirFontFamily,
                                        color = TextSecondary
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        "لیست غذاها:",
                                        style = MaterialTheme.typography.bodyLarge,
                                        fontFamily = vazirFontFamily,
                                        color = TextPrimary
                                    )
                                    foods.forEach { food ->
                                        val foodName = food.foodName
                                        val quantity = food.quantity
                                        Text(
                                            "$foodName - $quantity عدد",
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontFamily = vazirFontFamily,
                                            color = TextSecondary
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Row(
                                        horizontalArrangement = Arrangement.Center,
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        if (orderStatus=="تایید رستوران") {
                                            GradientButton(
                                                text = "تایید سفارش",
                                                onClick = {
                                                    sellerHomeRequestGroup.setGettingOrderReady(order.orderId.toString()){
                                                        Toast.makeText(m_context,"سفارش توسط شما تایید شد",
                                                            Toast.LENGTH_SHORT).show()
                                                        sellerHomeRequestGroup.getNotCompleteOrderSeller {
                                                            listOrders=it
                                                            onGoingProgress=false
                                                        }
                                                    }
                                                }
                                            )
                                            Spacer(modifier = Modifier.width(16.dp))
                                        }
                                        GradientButton(
                                            text = "جزئیات سفارش",
                                            onClick = {
                                                navController.navigate("seller/order/detail/"+order.orderId)
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
    }
}
