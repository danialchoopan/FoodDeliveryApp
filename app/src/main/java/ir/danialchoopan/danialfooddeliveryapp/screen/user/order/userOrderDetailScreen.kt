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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import ir.danialchoopan.danialfooddeliveryapp.data.RequestEndPoints
import ir.danialchoopan.danialfooddeliveryapp.data.home.HomeRestaurantOderRequestGroup
import ir.danialchoopan.danialfooddeliveryapp.formatPrice
import ir.danialchoopan.danialfooddeliveryapp.models.home.detail.Food
import ir.danialchoopan.danialfooddeliveryapp.models.user.order.Detail
import ir.danialchoopan.danialfooddeliveryapp.screen.functions.*
import ir.danialchoopan.danialfooddeliveryapp.ui.theme.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


@OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)
@Composable
fun UserOrderDetailScreen(
    navController: NavHostController,
    order_id: String
) {
    val m_context = LocalContext.current
    val homeRestaurantOderRequestGroup = HomeRestaurantOderRequestGroup(m_context)

    var listFoods by remember {
        mutableStateOf(emptyList<Detail>())
    }
    var seller_name by remember {
        mutableStateOf("")
    }
    var status_order by remember {
        mutableStateOf("")
    }
    var date_order by remember {
        mutableStateOf("")
    }
    var total_price by remember {
        mutableStateOf("")
    }

    homeRestaurantOderRequestGroup.getUserOrderDetailByID(order_id) {
        status_order = it.status
        date_order = it.orderDate
        listFoods = it.details
        seller_name = it.seller_name
        total_price = it.total_items.toString()
    }
    Scaffold(
        containerColor = BackgroundColor,
        topBar = {
            GradientTopBar(
                title = "جزئیات سفارش",
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
            // Order Info Card
            ModernCard(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            "نام رستوران: $seller_name",
                            style = MaterialTheme.typography.bodyLarge,
                            fontFamily = vazirFontFamily,
                            color = TextPrimary
                        )
                        StatusChip(status = status_order)
                    }
                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        "زمان سفارش: $date_order",
                        style = MaterialTheme.typography.bodyMedium,
                        fontFamily = vazirFontFamily,
                        color = TextSecondary
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        "مجموع: ${formatPrice(total_price)}",
                        style = MaterialTheme.typography.bodyMedium,
                        fontFamily = vazirFontFamily,
                        color = PrimaryColor,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                "لیست غذاها:",
                style = MaterialTheme.typography.bodyLarge,
                fontFamily = vazirFontFamily,
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn {
                items(listFoods) { food ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = CardSurface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            GlideImage(
                                model = RequestEndPoints.rootDomain + "/" + food.foodImg,
                                contentDescription = "",
                                modifier = Modifier
                                    .size(64.dp)
                                    .clip(RoundedCornerShape(12.dp)),
                                contentScale = ContentScale.Crop
                            )
                            Spacer(modifier = Modifier.width(8.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = food.foodName,
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = TextPrimary,
                                    fontFamily = vazirFontFamily
                                )
                                Text(
                                    text = "${formatPrice(food.price.toString())}  تومان",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = PrimaryColor,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (status_order != "لغو شده") {
                    GradientButton(
                        text = "لغو سفارش",
                        onClick = {
                            homeRestaurantOderRequestGroup.setOrderCancelByUser(order_id) {
                                navController.navigate("home_user") {
                                    popUpTo(0)
                                }
                            }
                        },
                        modifier = Modifier.padding(horizontal = 32.dp)
                    )
                }
            }
        }
    }
}
