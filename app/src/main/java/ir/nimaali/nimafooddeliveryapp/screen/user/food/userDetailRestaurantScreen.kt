package ir.nimaali.nimafooddeliveryapp.screen.user.food


import android.content.Context
import android.content.SharedPreferences
import android.provider.CalendarContract.Colors
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
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
import coil.request.ImageRequest
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage

import ir.nimaali.nimafooddeliveryapp.data.RequestEndPoints
import ir.nimaali.nimafooddeliveryapp.data.home.HomeRestaurantOderRequestGroup
import ir.nimaali.nimafooddeliveryapp.data.user.UserAuthRequestGroup
import ir.nimaali.nimafooddeliveryapp.models.home.Restaurant
import ir.nimaali.nimafooddeliveryapp.screen.functions.LoadingProgressbar
import ir.nimaali.nimafooddeliveryapp.ui.theme.BackgroundColor
import ir.nimaali.nimafooddeliveryapp.ui.theme.PrimaryColor
import ir.nimaali.nimafooddeliveryapp.ui.theme.SurfaceColor
import ir.nimaali.nimafooddeliveryapp.ui.theme.vazirFontFamily
import ir.nimaali.nimafooddeliveryapp.viewmodel.AuthUserSellerViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


fun LoadImageFormURLFixutils(img: String): String {
//    return RequestEndPoints.storageImageLoadFood + img.replace(
//        "\\",
//        "/"
//    )
    return img.replace(
        "\\",
        "/"
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)
@Composable
fun UserDetailRestaurantScreen(navController: NavController, restaurant_id: String) {

    val m_context= LocalContext.current
    val rememberScroll = rememberScrollState()
    // داده‌های نمونه
    val restaurantName = "رستوران نمونه"
    val restaurantCategory = "ایرانی و سنتی"

    var loading by remember {
        mutableStateOf(false)
    }


//    val restaurantImage = painterResource(id = R.drawable.restaurant_image) // تصویر نمونه
    val dishes = listOf(
        Dish("قرمه سبزی", "توضیحات خوشمزه", 45000),
        Dish("زرشک پلو", "بهترین کیفیت", 60000),
        Dish("کباب کوبیده", "با دو سیخ", 70000)
    )
    val comments = listOf(
        "غذا عالی بود!",
        "بسیار تمیز و مرتب.",
        "سرویس‌دهی کمی کند بود ولی کیفیت خوب بود."
    )

    // مقادیر تعداد انتخاب‌شده برای هر غذا
    val quantities = remember { mutableStateListOf(*Array(dishes.size) { 0 }) }


    LaunchedEffect(Unit) {


    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "نمایش رستوران ", style = MaterialTheme.typography.headlineSmall,
                        fontFamily = vazirFontFamily,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        color = Color.White
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF4CAF50)),
                navigationIcon = {
                    IconButton(onClick = {
                        // عملکرد بازگشت
                        navController.popBackStack()
                    }) {
                        Icon(
                            imageVector = Icons.Default.Close, // آیکون بازگشت پیش‌فرض
                            contentDescription = "بازگشت",
                            tint = Color.White
                        )
                    }
                }
            )
        },
        modifier = Modifier.fillMaxSize(),
        content = { padding ->
//            LoadImageOnline(
//                model = "http://192.168.1.11:5000/static/images/restaurants/1.jpg",
//                modifier = Modifier.size(400.dp, 200.dp)
//            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScroll)
            ) {
                Card {
                    GlideImage(
                        model = "https://abzarwp.com/static/uploads/2019/01/wordpress-bg-medblue.png",
                        contentDescription = "",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(230.dp),
                    )
                }

                if (loading) {
                    LoadingProgressbar {

                    }
                } else {
                    // اطلاعات رستوران
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = restaurantName,
                            style = MaterialTheme.typography.headlineMedium,
                            color = Color.Black,
                            fontFamily = vazirFontFamily
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = restaurantCategory,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // لیست غذاها
                    Text(
                        text = "منوی غذاها",
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.padding(horizontal = 16.dp),
                        fontFamily = vazirFontFamily
                    )
                    LazyColumn(
                        modifier = Modifier
                            .height(400.dp)
                            .padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        itemsIndexed(dishes) { index, dish ->
                            DishItem(dish = dish, quantity = quantities[index]) { change ->
                                quantities[index] = (quantities[index] + change).coerceAtLeast(0)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                    Button(
                        onClick = {

                        },
                        modifier = Modifier.fillMaxWidth().padding(16.dp)
                    ) {
                        Text(
                            "ثبت سفارش",
                            fontFamily = vazirFontFamily,
                            style = MaterialTheme.typography.headlineSmall
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // کامنت‌ها
                    Text(
                        text = "نظرات کاربران",
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.padding(horizontal = 16.dp),
                        fontFamily = vazirFontFamily
                    )
                    LazyColumn(
                        modifier = Modifier
                            .height(300.dp)
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(comments) { comment ->
                            Text(
                                text = comment,
                                style = MaterialTheme.typography.bodyLarge,
                                color = Color.Gray,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color(0xFFF5F5F5), RoundedCornerShape(8.dp))
                                    .padding(8.dp)
                            )
                        }
                    }

                }
            }
        }
    )
}

@Composable
fun DishItem(dish: Dish, quantity: Int, onQuantityChange: (Int) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF5F5F5), RoundedCornerShape(8.dp))
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // تصویر غذا
//        Image(
//            painter = painterResource(id = dish.imageRes),
//            contentDescription = "تصویر ${dish.name}",
//            modifier = Modifier
//                .size(64.dp)
//                .clip(RoundedCornerShape(8.dp)),
//            contentScale = ContentScale.Crop
//        )
        Spacer(modifier = Modifier.width(8.dp))

        // اطلاعات غذا
        Column(modifier = Modifier.weight(1f)) {
            Text(text = dish.name, style = MaterialTheme.typography.bodyLarge, color = Color.Black)
            Text(
                text = dish.description,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
            Text(
                text = "${dish.price} تومان",
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF4CAF50)
            )
        }

        // کنترل تعداد
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { onQuantityChange(-1) }) {
                Icon(Icons.Default.Clear, contentDescription = "کاهش تعداد", tint = Color.Red)
            }
            Text(
                text = "$quantity",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Black
            )
            IconButton(onClick = { onQuantityChange(1) }) {
                Icon(Icons.Default.Add, contentDescription = "افزایش تعداد", tint = Color.Green)
            }
        }
    }
}

// کلاس داده برای غذا
data class Dish(
    val name: String,
    val description: String,
    val price: Int,
//    val imageRes: Int
)
//@Composable
//fun LoadImageOnline(
//    model: String,
//    modifier: Modifier = Modifier
//) {
//    SubcomposeAsyncImage(
//        modifier = modifier,
//        model = model,
//        loading = {
//            CircularProgressIndicator(modifier = Modifier.requiredSize(40.dp))
//        },
//        contentDescription = null,
//    )
//}