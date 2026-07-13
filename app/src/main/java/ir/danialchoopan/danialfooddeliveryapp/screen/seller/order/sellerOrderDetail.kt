package ir.danialchoopan.danialfooddeliveryapp.screen.seller.order

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import ir.danialchoopan.danialfooddeliveryapp.data.RequestEndPoints
import ir.danialchoopan.danialfooddeliveryapp.data.seller.SellerHomeRequestGroup
import ir.danialchoopan.danialfooddeliveryapp.formatPrice
import ir.danialchoopan.danialfooddeliveryapp.models.seller.dash.orderDetail.FoodDetail
import ir.danialchoopan.danialfooddeliveryapp.screen.functions.*
import ir.danialchoopan.danialfooddeliveryapp.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)
@Composable
fun SellerOrderDetailScreen(
    navController: NavHostController,
    order_id:String
) {

    val m_context= LocalContext.current
    val sellerHomeRequestGroup=SellerHomeRequestGroup(m_context)

    var listFoods by remember {
        mutableStateOf(emptyList<FoodDetail>())
    }
    var orderStatus by remember {
        mutableStateOf("")
    }
    var order_address by remember {
        mutableStateOf("")
    }
    var order_time by remember {
        mutableStateOf("")
    }
    var user_name by remember {
        mutableStateOf("")
    }
    var total_price by remember {
        mutableStateOf("")
    }

    sellerHomeRequestGroup.sellerGetOrderDetailById(order_id){
        listFoods=it.foodDetails
        orderStatus=it.status
        order_time=it.orderDate
        order_address=it.userAddress
        total_price=it.totalPrice.toInt().toString()

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
                            tint = MaterialTheme.colorScheme.onPrimary
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
            ModernCard(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        "نام سفارش‌دهنده: "+user_name,
                        style = MaterialTheme.typography.bodyLarge,
                        fontFamily = vazirFontFamily,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    StatusChip(status = orderStatus)
                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        "آدرس: "+order_address,
                        style = MaterialTheme.typography.bodyMedium,
                        fontFamily = vazirFontFamily,
                        color = TextSecondary
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        "زمان سفارش: "+order_time,
                        style = MaterialTheme.typography.bodyMedium,
                        fontFamily = vazirFontFamily,
                        color = TextSecondary
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

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(CardSurface, RoundedCornerShape(16.dp))
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
                                color = TextPrimary
                            )
                            Text(
                                text = "${formatPrice(food.price.toString())} تومان",
                                style = MaterialTheme.typography.bodyMedium,
                                color = PrimaryColor
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text(
                "مجموع قیمت:"+formatPrice(total_price),
                style = MaterialTheme.typography.bodyLarge,
                fontFamily = vazirFontFamily,
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(8.dp))


            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (orderStatus=="تایید رستوران") {
                    GradientButton(
                        text = "تایید سفارش",
                        onClick = {
                            sellerHomeRequestGroup.setGettingOrderReady(order_id){
                                Toast.makeText(m_context,"سفارش توسط شما تایید شد", Toast.LENGTH_SHORT).show()
                                navController.popBackStack()
                            }
                        }
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                }
                if (orderStatus=="در حال آماده سازی") {
                    GradientButton(
                        text = "تکمیل سفارش",
                        onClick = {
                            sellerHomeRequestGroup.setCompletedOrderReady(order_id){
                                Toast.makeText(m_context,"ثبت تکمیل سفارش کاربر تکمیل شد ", Toast.LENGTH_SHORT).show()
                                navController.popBackStack()
                            }
                        }
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                }
                if (orderStatus!="لغو رستوران") {

                    Button(
                        onClick = {
                            sellerHomeRequestGroup.setCanselBySellerOrderReady(order_id) {
                                Toast.makeText(
                                    m_context,
                                    "سفارش از سمت رستوران لغو شده",
                                    Toast.LENGTH_SHORT
                                ).show()
                                navController.popBackStack()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = ErrorColor),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("لغو سفارش ", fontFamily = vazirFontFamily)
                    }
                }


            }
        }
    }
}
