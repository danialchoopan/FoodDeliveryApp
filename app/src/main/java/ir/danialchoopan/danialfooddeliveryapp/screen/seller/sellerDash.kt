package ir.danialchoopan.danialfooddeliveryapp.screen.seller

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import ir.danialchoopan.danialfooddeliveryapp.data.seller.SellerHomeRequestGroup
import ir.danialchoopan.danialfooddeliveryapp.models.seller.dash.SellerOrdersDashShowItem
import ir.danialchoopan.danialfooddeliveryapp.screen.functions.*
import ir.danialchoopan.danialfooddeliveryapp.ui.theme.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SellerDashboardScreen(navController: NavHostController) {
    val m_context = LocalContext.current

    val sellerSharedPreferences =
        m_context.getSharedPreferences("app_data", Context.MODE_PRIVATE)

    val sellerHomeRequestGroup = SellerHomeRequestGroup(m_context)

    var onGoingProgress by remember {
        mutableStateOf(true)
    }

    var isStoreOpen by remember { mutableStateOf(true) }
    var showBottomSheet by remember { mutableStateOf(false) }
    var listOrders by remember {
        mutableStateOf(emptyList<SellerOrdersDashShowItem>())
    }

    sellerHomeRequestGroup.getSellerStatus {
        isStoreOpen = it
        onGoingProgress=false
    }
    sellerHomeRequestGroup.getNotCompleteOrderSeller {
        listOrders=it
        onGoingProgress=false
    }

    Scaffold(
        containerColor = BackgroundColor,
        topBar = {
            GradientTopBar(
                title = "داشبورد فروشنده",
                navigationIcon = {
                    IconButton(onClick = {
                        showBottomSheet = true
                    }) {
                        Icon(
                            Icons.Default.AddCircle,
                            contentDescription = "افزودن",
                            modifier = Modifier.size(40.dp),
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {
                        sellerHomeRequestGroup.getNotCompleteOrderSeller {
                            listOrders=it
                            onGoingProgress=false
                        } }) {
                        Icon(
                            Icons.Default.Refresh,
                            contentDescription = "بارگذاری دوباره",
                            modifier = Modifier.size(40.dp),
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
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
                    MenuItem(icon = Icons.Default.Menu, title = "لیست غذاها", onClick = {
                        navController.navigate("seller/list/food")
                        showBottomSheet = false
                    })
                    MenuItem(icon = Icons.Default.AddCircle, title = "افزودن غذا", onClick = {
                        navController.navigate("seller/add/food")
                        showBottomSheet = false
                    })
                    MenuItem(icon = Icons.Default.Email, title = "نظرات مشتریان", onClick = {
                        navController.navigate("seller/show/comments")
                        showBottomSheet = false
                    })
                    MenuItem(icon = Icons.Default.DateRange, title = "تاریخچه سفارش", onClick = {
                        navController.navigate("seller/order/history")
                        showBottomSheet = false
                    })
                    MenuItem(icon = Icons.Default.Face, title = "درآمد", onClick = {
                        navController.navigate("seller/payment")
                        showBottomSheet = false
                    })
                    MenuItem(icon = Icons.Default.Person, title = "ویرایش اطلاعات", onClick = {
                        navController.navigate("seller/edit")
                        showBottomSheet = false
                    })
                    MenuItem(
                        icon = Icons.Default.ExitToApp,
                        title = "خروج از حساب کاربری",
                        onClick = {
                            showBottomSheet = false
                            sellerSharedPreferences.edit().let {
                                it.clear()
                                it.apply()
                            }
                            navController.navigate("seller_login") {
                                popUpTo(0)
                            }
                        })
                }
            }
        }
        if (onGoingProgress) {

            LoadingProgressbar {

            }

        } else {
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            ) {
                ModernCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
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
                                tint = if (isStoreOpen) SuccessColor else ErrorColor
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                "وضعیت فروشگاه: ${if (isStoreOpen) "باز" else "بسته"}",
                                color = if (isStoreOpen) SuccessColor else ErrorColor,
                                style = MaterialTheme.typography.titleMedium,
                                fontFamily = vazirFontFamily
                            )
                        }
                        Button(
                            onClick = {
                                onGoingProgress=true
                                isStoreOpen = !isStoreOpen
                                sellerHomeRequestGroup.setSellerStatus(isStoreOpen) {
                                    onGoingProgress=false
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isStoreOpen) ErrorColor else SuccessColor
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                if (isStoreOpen) "بستن فروشگاه" else "باز کردن فروشگاه",
                                fontFamily = vazirFontFamily,
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                    }
                }

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
                                                        Toast.makeText(m_context,"سفارش توسط شما تایید شد",Toast.LENGTH_SHORT).show()
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
            tint = PrimaryColor,
            modifier = Modifier.size(34.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = title,
            fontSize = 18.sp,
            fontFamily = vazirFontFamily,
            color = TextPrimary
        )
    }
}
