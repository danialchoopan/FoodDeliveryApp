package ir.danialchoopan.danialfooddeliveryapp.screen.user.order

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import ir.danialchoopan.danialfooddeliveryapp.data.home.HomeRestaurantOderRequestGroup
import ir.danialchoopan.danialfooddeliveryapp.models.home.Restaurant
import ir.danialchoopan.danialfooddeliveryapp.models.home.order.OrderListUsersAllItem
import ir.danialchoopan.danialfooddeliveryapp.screen.functions.*
import ir.danialchoopan.danialfooddeliveryapp.ui.theme.*
import ir.danialchoopan.danialfooddeliveryapp.viewmodel.AuthUserSellerViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserOrdersAllScreen(navController: NavController) {
    val m_context = LocalContext.current
    val homeRestaurantOderRequestGroup = HomeRestaurantOderRequestGroup(m_context)

    var listOrders by remember {
        mutableStateOf(emptyList<OrderListUsersAllItem>())
    }

    var loading by remember {
        mutableStateOf(true)
    }

    homeRestaurantOderRequestGroup.getAllOrdersUser {
        listOrders = it
        loading = false
    }

    Scaffold(
        containerColor = BackgroundColor,
        topBar = {
            GradientTopBar(
                title = "نمایش تمام سفارش های کاربر",
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
        if (loading) {
            LoadingProgressbar {

            }
        } else {
            if (listOrders.isEmpty()) {
                EmptyState(message = "شما تا به حال سفارشی ثبت نکرده اید !")
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
