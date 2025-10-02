package ir.nimaali.nimafooddeliveryapp.screen.user.order

import android.content.Context
import android.content.SharedPreferences
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
import ir.nimaali.nimafooddeliveryapp.data.home.HomeRestaurantOderRequestGroup
import ir.nimaali.nimafooddeliveryapp.data.user.UserAuthRequestGroup
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
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "نمایش تمام سفارش های کاربر",
                        style = MaterialTheme.typography.headlineSmall,
                        fontFamily = vazirFontFamily,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        color = Color.White
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
                    Text("شما تا به حال سفارشی ثبت نکرده اید !")
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