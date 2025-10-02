package ir.nimaali.nimafooddeliveryapp.screen.user.food


import android.content.Context
import android.content.SharedPreferences
import android.provider.CalendarContract.Colors
import android.util.Log
import android.widget.Toast
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
import androidx.compose.material.icons.filled.Delete
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
import ir.nimaali.nimafooddeliveryapp.formatPrice
import ir.nimaali.nimafooddeliveryapp.models.home.Restaurant
import ir.nimaali.nimafooddeliveryapp.models.home.detail.Comment
import ir.nimaali.nimafooddeliveryapp.models.home.detail.Food
import ir.nimaali.nimafooddeliveryapp.screen.functions.LoadingProgressbar
import ir.nimaali.nimafooddeliveryapp.ui.theme.BackgroundColor
import ir.nimaali.nimafooddeliveryapp.ui.theme.PrimaryColor
import ir.nimaali.nimafooddeliveryapp.ui.theme.SurfaceColor
import ir.nimaali.nimafooddeliveryapp.ui.theme.vazirFontFamily
import ir.nimaali.nimafooddeliveryapp.viewmodel.AuthUserSellerViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)
@Composable
fun UserDetailRestaurantScreen(navController: NavController, restaurant_id: String) {

    val m_context = LocalContext.current
    val rememberScroll = rememberScrollState()
    // داده‌های نمونه
    var restaurantName by remember {
        mutableStateOf("")
    }
    var restaurantCategory by remember {
        mutableStateOf("")
    }
    var restaurantAddress by remember {
        mutableStateOf("")
    }
    var restaurantImage by remember {
        mutableStateOf("")
    }



    var orderDetails by remember { mutableStateOf<List<Pair<Int, Int>>>(emptyList()) }
    var showCommentDialog by remember { mutableStateOf(false) }


    var loading by remember {
        mutableStateOf(true)
    }
    var showDialog by remember { mutableStateOf(false) }

    var listComment by remember {
        mutableStateOf(emptyList<Comment>())
    }
    var listFoods by remember {
        mutableStateOf(emptyList<Food>())
    }
    val quantities = remember { mutableStateListOf<Int>() }

    val homeRestaurantOderRequestGroup = HomeRestaurantOderRequestGroup(m_context)


    val userSharedPreferences = m_context.getSharedPreferences("app_data", Context.MODE_PRIVATE)

    var totalPrice by remember { mutableStateOf(0) }
    val user_id = userSharedPreferences.getString("user_id", "")

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "$restaurantName رستوران ", style = MaterialTheme.typography.headlineSmall,
                        fontFamily = vazirFontFamily,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        color = Color.White
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF4CAF50)),
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "بازگشت",
                            tint = Color.White
                        )
                    }
                }
            )
        },
        modifier = Modifier.fillMaxSize(),
        content = { padding ->

            homeRestaurantOderRequestGroup.homePageRestaurantDetailById(restaurant_id) { name, foods, comments, category, address, image ->
                restaurantName = name
                restaurantCategory = category
                restaurantAddress = address
                restaurantImage=image
                if (!foods.isNullOrEmpty()) {
                    listFoods = foods
                    quantities.clear() // پاک کردن مقادیر قبلی
                    quantities.addAll(List(foods.size) { 0 }) // مقداردهی اولیه به تعداد غذاها
                }

                if (!comments.isNullOrEmpty())
                    listComment = comments

                loading = false
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScroll)
            ) {
                if (loading) {
                    LoadingProgressbar {}
                } else {
                    GlideImage(
                        model = RequestEndPoints.rootDomain + "/" + restaurantImage,
                        contentDescription = "",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(160.dp),
                    )
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
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = restaurantAddress,
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

                    if (listFoods.isNotEmpty()) {
                        LazyColumn(
                            modifier = Modifier
                                .height(400.dp)
                                .padding(horizontal = 16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            itemsIndexed(listFoods) { index, food ->
                                DishItem(
                                    food = food,
                                    index = index,
                                    quantities = quantities,
                                    price=totalPrice,
                                    onQuantityChange = { i, change,price ->
                                        totalPrice=price
                                        // بروزرسانی تعداد غذا
                                        if (i < quantities.size) {
                                            quantities[i] =
                                                (quantities[i] + change).coerceAtLeast(0)
                                        }
                                        // لاگ کردن تغییرات
                                        Log.d(
                                            "QuantityChanged",
                                            "Food: ${food.foodName}, Quantity: ${quantities[i]}"
                                        )
                                    }
                                )
                            }
                        }
                    } else {
                        Text("این رستوران در حال حاضر غذایی ثبت نکرده است!")
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "مجموع : " + formatPrice(totalPrice.toString()),
                        style = MaterialTheme.typography.titleSmall,
                        color = Color(0xFF4CAF50)
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Button(
                        onClick = {


                            orderDetails = listFoods.mapIndexed { index, food ->
                                Pair(food.foodId, quantities.getOrElse(index) { 0 })
                            }.filter { it.second > 0 }


                            if (orderDetails.isEmpty()) {
                                Toast.makeText(
                                    m_context,
                                    "لطفا برای ثبت سفارش ابتدا تعداد عذا ها را ثبت کنید",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                showDialog = true
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text(
                            "ثبت سفارش",
                            fontFamily = vazirFontFamily,
                            style = MaterialTheme.typography.headlineSmall
                        )
                    }

                    if (orderDetails.isNotEmpty()) {
                        if (showDialog) {
                            ConfirmOrderDialog({
                                restaurantName = orderDetails.toString()
                                homeRestaurantOderRequestGroup.placeOrder(
                                    restaurant_id,
                                    orderDetails.toString()
                                ) {
                                    navController.navigate("home_user") {
                                        popUpTo(0)
                                    }
                                }
                            }, {
                                showDialog = false
                                Toast.makeText(
                                    m_context,
                                    "تایید سفارش لغو شد",
                                    Toast.LENGTH_SHORT
                                ).show()
                            })
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // کامنت‌ها
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Text(
                            text = "نظرات کاربران",
                            style = MaterialTheme.typography.headlineSmall,
                            modifier = Modifier.padding(horizontal = 16.dp),
                            fontFamily = vazirFontFamily
                        )

                        Button(
                            onClick = {
                                showCommentDialog = true
                            }
                        ) {
                            Text("نوشتن نظر")
                        }
                    }
                    if (showCommentDialog) {
                        CommentDialog(restaurant_id, {
                            showCommentDialog = false
                        })
                    }

                    if (listComment.isNotEmpty()) {
                        LazyColumn(
                            modifier = Modifier
                                .height(300.dp)
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(listComment) {
                                if (it.commentUserId.toString().trim() == user_id.toString()
                                        .trim()
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        modifier = Modifier.fillMaxWidth().background(Color(0xFFF5F5F5),
                                        RoundedCornerShape(8.dp)).padding(8.dp)
                                    ) {
                                        Text(
                                            text = it.commentContent,
                                            style = MaterialTheme.typography.bodyLarge,
                                            color = Color.Gray,
                                        )
                                        IconButton(onClick = {
                                            loading=true
                                            homeRestaurantOderRequestGroup.DeleteCommentDetail(it.commentId.toString()) {
                                                loading=false
                                                Toast.makeText(m_context,"نظر شما با موفقیت حذف شد!",Toast.LENGTH_SHORT).show()
                                            }
                                        }) {
                                            Icon(
                                                imageVector = Icons.Default.Delete,
                                                contentDescription = "حذف",
                                                tint = Color.Red
                                            )
                                        }
                                    }
                                } else {
                                    Text(
                                        text = it.commentContent,
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = Color.Gray,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .background(Color(0xFFF5F5F5), RoundedCornerShape(8.dp))
                                            .padding(16.dp)
                                    )
                                }
                            }
                        }
                    } else {
                        Text(
                            "در حال نظری برای این رستوران وجود ندارد",
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(70.dp))
        }
    )
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun DishItem(food: Food, index: Int,price:Int, quantities: List<Int>, onQuantityChange: (Int, Int,Int) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF5F5F5), RoundedCornerShape(8.dp))
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        var tprice by remember {
            mutableStateOf(price)
        }
        // تصویر غذا
        GlideImage(
            model = RequestEndPoints.rootDomain + "/" + food.foodImage,
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
                text = food.foodDescription,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
            Text(
                text = "${formatPrice(food.foodPrice)} تومان",
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF4CAF50)
            )
        }

        // کنترل تعداد
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = {
                onQuantityChange(index, -1,0)

                tprice=tprice-food.foodPrice.toInt();

            }) {
                Icon(Icons.Default.Clear, contentDescription = "کاهش تعداد", tint = Color.Red)
            }
            Text(
                text = quantities.getOrElse(index) { 0 }.toString(), // تعداد غذا از آرایه
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Black
            )
            IconButton(onClick = { onQuantityChange(index, 1,0)

                tprice=tprice+food.foodPrice.toInt();
            }) {
                Icon(Icons.Default.Add, contentDescription = "افزایش تعداد", tint = Color.Green)

            }
        }
    }
}

@Composable
fun ConfirmOrderDialog(
    onConfirm: () -> Unit,
    onCancel: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = { onCancel() },
        title = {
            Text(text = "تایید سفارش")
        },
        text = {
            Text(text = "آیا برای ثبت سفارش مطمئن هستید؟")
        },
        confirmButton = {
            Button(
                onClick = {
                    onConfirm() // وقتی کاربر تایید کرد
                }
            ) {
                Text("تایید")
            }
            Spacer(Modifier.width(20.dp))
        },
        dismissButton = {
            Button(
                onClick = {
                    onCancel() // وقتی کاربر لغو کرد
                }
            ) {
                Text("لغو")
            }
        }
    )

}

@Composable
fun CommentDialog(
    seller_id: String,
    onDismissRequest: () -> Unit,
) {
    val homeRestaurantOderRequestGroup = HomeRestaurantOderRequestGroup(LocalContext.current)
    var comment_user by remember {
        mutableStateOf("")
    }
    AlertDialog(
        onDismissRequest = {
            // جلوگیری از بسته شدن دیالوگ با کلیک بیرون
        },
        title = {
            Text(
                text = "کامنت شما",
                style = MaterialTheme.typography.headlineSmall,
                fontFamily = vazirFontFamily
            )
        },
        text = {
            Column {
                OutlinedTextField(
                    value = comment_user,
                    onValueChange = { comment_user = it },
                    label = { Text("نظر کاربر") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 1
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    homeRestaurantOderRequestGroup.AddCommentDetail(seller_id, comment_user) {
                        onDismissRequest()
                    }
                }
            ) {
                Text(text = "ارسال", fontFamily = vazirFontFamily)
            }
            Spacer(Modifier.width(20.dp))
        },
        dismissButton = {
            Button(
                onClick = { onDismissRequest() } // عملکرد بستن
            ) {
                Text(text = "بستن", fontFamily = vazirFontFamily)
            }
        }
    )

}
