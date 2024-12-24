package ir.nimaali.nimafooddeliveryapp.screen.user.order

import android.content.Context
import android.content.SharedPreferences
import android.provider.CalendarContract.Colors
import android.view.MenuItem
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import ir.nimaali.nimafooddeliveryapp.data.RequestEndPoints
import ir.nimaali.nimafooddeliveryapp.data.home.HomeRestaurantOderRequestGroup
import ir.nimaali.nimafooddeliveryapp.models.home.detail.Food
import ir.nimaali.nimafooddeliveryapp.models.user.order.Detail
import ir.nimaali.nimafooddeliveryapp.ui.theme.BackgroundColor
import ir.nimaali.nimafooddeliveryapp.ui.theme.PrimaryColor
import ir.nimaali.nimafooddeliveryapp.ui.theme.SurfaceColor
import ir.nimaali.nimafooddeliveryapp.ui.theme.vazirFontFamily
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
        status_order=it.status
        date_order=it.orderDate
        listFoods = it.details
        seller_name=it.seller_name
        total_price=it.total_items.toString()
    }
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
                "نام رستوران: "+seller_name,
                style = MaterialTheme.typography.bodyLarge,
                fontFamily = vazirFontFamily
            )
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                "وضعیت سفارش: "+status_order,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                "زمان سفارش: "+date_order,
                style = MaterialTheme.typography.bodyMedium,
                fontFamily = vazirFontFamily
            )
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                "مجموع: "+total_price,
                style = MaterialTheme.typography.bodyMedium,
                fontFamily = vazirFontFamily
            )
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                "لیست غذاها:",
                style = MaterialTheme.typography.bodyLarge,
                fontFamily = vazirFontFamily
            )
            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn {
                items(listFoods) { food ->

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFFF5F5F5), RoundedCornerShape(8.dp))
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // تصویر غذا
                        GlideImage(
                            model = RequestEndPoints.rootDomain + "/" + food.foodImg,
                            contentDescription = "",
                            modifier = Modifier
                                .size(64.dp)
                                .clip(RoundedCornerShape(8.dp)),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(modifier = Modifier.width(8.dp))

                        // اطلاعات غذا
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = food.foodName,
                                style = MaterialTheme.typography.bodyLarge,
                                color = Color.Black
                            )
                            Text(
                                text = "${food.price} تومان",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color(0xFF4CAF50)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (status_order!="لغو شده") {
                    Button(
                        onClick = {
                            homeRestaurantOderRequestGroup.setOrderCancelByUser(order_id){
                                navController.navigate("home_user"){
                                    popUpTo(0)
                                }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Red
                        )
                    ) {
                        Text("لغو سفارش", fontFamily = vazirFontFamily)
                    }
                }

            }
        }
    }
}