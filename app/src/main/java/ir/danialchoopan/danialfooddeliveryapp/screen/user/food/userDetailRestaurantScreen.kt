package ir.danialchoopan.danialfooddeliveryapp.screen.user.food


import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage

import ir.danialchoopan.danialfooddeliveryapp.data.RequestEndPoints
import ir.danialchoopan.danialfooddeliveryapp.data.home.HomeRestaurantOderRequestGroup
import ir.danialchoopan.danialfooddeliveryapp.formatPrice
import ir.danialchoopan.danialfooddeliveryapp.models.home.Restaurant
import ir.danialchoopan.danialfooddeliveryapp.models.home.detail.Comment
import ir.danialchoopan.danialfooddeliveryapp.models.home.detail.Food
import ir.danialchoopan.danialfooddeliveryapp.screen.functions.*
import ir.danialchoopan.danialfooddeliveryapp.ui.theme.*
import ir.danialchoopan.danialfooddeliveryapp.viewmodel.AuthUserSellerViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)
@Composable
fun UserDetailRestaurantScreen(navController: NavController, restaurant_id: String) {

    val m_context = LocalContext.current
    val rememberScroll = rememberScrollState()
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
        containerColor = BackgroundColor,
        topBar = {
            GradientTopBar(
                title = "$restaurantName رستوران ",
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
                restaurantImage = image
                if (!foods.isNullOrEmpty()) {
                    listFoods = foods
                    quantities.clear()
                    quantities.addAll(List(foods.size) { 0 })
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

                    // Restaurant Info Section
                    ModernCard(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = restaurantName,
                                style = MaterialTheme.typography.headlineMedium,
                                color = TextPrimary,
                                fontFamily = vazirFontFamily
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = restaurantCategory,
                                style = MaterialTheme.typography.bodyMedium,
                                color = TextSecondary,
                                fontFamily = vazirFontFamily
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = restaurantAddress,
                                style = MaterialTheme.typography.bodyMedium,
                                color = TextSecondary,
                                fontFamily = vazirFontFamily
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Menu Section
                    Text(
                        text = "منوی غذاها",
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.padding(horizontal = 16.dp),
                        fontFamily = vazirFontFamily,
                        color = TextPrimary
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
                                    price = totalPrice,
                                    onQuantityChange = { i, change, price ->
                                        totalPrice = price
                                        if (i < quantities.size) {
                                            quantities[i] =
                                                (quantities[i] + change).coerceAtLeast(0)
                                        }
                                        Log.d(
                                            "QuantityChanged",
                                            "Food: ${food.foodName}, Quantity: ${quantities[i]}"
                                        )
                                    }
                                )
                            }
                        }
                    } else {
                        Text(
                            "این رستوران در حال حاضر غذایی ثبت نکرده است!",
                            modifier = Modifier.padding(16.dp),
                            color = TextSecondary
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "مجموع : " + formatPrice(totalPrice.toString()),
                        style = MaterialTheme.typography.titleSmall,
                        color = PrimaryColor,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    GradientButton(
                        text = "ثبت سفارش",
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
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )

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

                    // Comments Section
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(16.dp, 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Text(
                            text = "نظرات کاربران",
                            style = MaterialTheme.typography.headlineSmall,
                            fontFamily = vazirFontFamily,
                            color = TextPrimary
                        )

                        GradientButton(
                            text = "نوشتن نظر",
                            onClick = { showCommentDialog = true },
                            modifier = Modifier.width(140.dp)
                        )
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
                                    Card(
                                        modifier = Modifier.fillMaxWidth(),
                                        shape = RoundedCornerShape(12.dp),
                                        colors = CardDefaults.cardColors(
                                            containerColor = CardSurface
                                        )
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            modifier = Modifier.padding(12.dp)
                                        ) {
                                            Text(
                                                text = it.commentContent,
                                                style = MaterialTheme.typography.bodyLarge,
                                                color = TextPrimary,
                                            )
                                            IconButton(onClick = {
                                                loading = true
                                                homeRestaurantOderRequestGroup.DeleteCommentDetail(it.commentId.toString()) {
                                                    loading = false
                                                    Toast.makeText(
                                                        m_context,
                                                        "نظر شما با موفقیت حذف شد!",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                }
                                            }) {
                                                Icon(
                                                    imageVector = Icons.Default.Delete,
                                                    contentDescription = "حذف",
                                                    tint = ErrorColor
                                                )
                                            }
                                        }
                                    }
                                } else {
                                    Card(
                                        modifier = Modifier.fillMaxWidth(),
                                        shape = RoundedCornerShape(12.dp),
                                        colors = CardDefaults.cardColors(
                                            containerColor = CardSurface
                                        )
                                    ) {
                                        Text(
                                            text = it.commentContent,
                                            style = MaterialTheme.typography.bodyLarge,
                                            color = TextPrimary,
                                            modifier = Modifier.padding(16.dp)
                                        )
                                    }
                                }
                            }
                        }
                    } else {
                        Text(
                            "در حال نظری برای این رستوران وجود ندارد",
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            color = TextSecondary
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
fun DishItem(food: Food, index: Int, price: Int, quantities: List<Int>, onQuantityChange: (Int, Int, Int) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
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
            var tprice by remember {
                mutableStateOf(price)
            }

            GlideImage(
                model = RequestEndPoints.rootDomain + "/" + food.foodImage,
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
                    text = food.foodDescription,
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary
                )
                Text(
                    text = "${formatPrice(food.foodPrice)} تومان",
                    style = MaterialTheme.typography.bodyMedium,
                    color = PrimaryColor,
                    fontWeight = FontWeight.Bold
                )
            }

            // Quantity Controls
            Card(
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(4.dp)
                ) {
                    IconButton(onClick = {
                        onQuantityChange(index, -1, 0)
                        tprice = tprice - food.foodPrice.toInt()
                    }) {
                        Icon(
                            Icons.Default.Clear,
                            contentDescription = "کاهش تعداد",
                            tint = ErrorColor,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Text(
                        text = quantities.getOrElse(index) { 0 }.toString(),
                        style = MaterialTheme.typography.bodyLarge,
                        color = TextPrimary,
                        modifier = Modifier.padding(horizontal = 4.dp)
                    )
                    IconButton(onClick = {
                        onQuantityChange(index, 1, 0)
                        tprice = tprice + food.foodPrice.toInt()
                    }) {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = "افزایش تعداد",
                            tint = PrimaryColor,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
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
        shape = RoundedCornerShape(20.dp),
        containerColor = Color.White,
        title = {
            Text(
                text = "تایید سفارش",
                fontFamily = vazirFontFamily,
                color = TextPrimary
            )
        },
        text = {
            Text(
                text = "آیا برای ثبت سفارش مطمئن هستید؟",
                fontFamily = vazirFontFamily,
                color = TextSecondary
            )
        },
        confirmButton = {
            GradientButton(
                text = "تایید",
                onClick = { onConfirm() },
                modifier = Modifier.width(120.dp)
            )
        },
        dismissButton = {
            OutlinedButton(
                onClick = { onCancel() },
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = TextSecondary
                )
            ) {
                Text("لغو", fontFamily = vazirFontFamily)
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
        onDismissRequest = { },
        shape = RoundedCornerShape(20.dp),
        containerColor = Color.White,
        title = {
            Text(
                text = "کامنت شما",
                style = MaterialTheme.typography.headlineSmall,
                fontFamily = vazirFontFamily,
                color = TextPrimary
            )
        },
        text = {
            Column {
                OutlinedTextField(
                    value = comment_user,
                    onValueChange = { comment_user = it },
                    label = { Text("نظر کاربر", fontFamily = vazirFontFamily) },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 1,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PrimaryColor,
                        unfocusedBorderColor = BorderColor
                    )
                )
            }
        },
        confirmButton = {
            GradientButton(
                text = "ارسال",
                onClick = {
                    homeRestaurantOderRequestGroup.AddCommentDetail(seller_id, comment_user) {
                        onDismissRequest()
                    }
                },
                modifier = Modifier.width(100.dp)
            )
        },
        dismissButton = {
            OutlinedButton(
                onClick = { onDismissRequest() },
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = TextSecondary
                )
            ) {
                Text(text = "بستن", fontFamily = vazirFontFamily)
            }
        }
    )

}
