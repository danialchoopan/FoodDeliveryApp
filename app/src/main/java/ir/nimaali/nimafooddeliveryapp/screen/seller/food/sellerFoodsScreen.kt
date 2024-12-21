package ir.nimaali.nimafooddeliveryapp.screen.seller.food


import android.content.Context
import android.content.SharedPreferences
import android.provider.CalendarContract.Colors
import android.view.MenuItem
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import ir.nimaali.nimafooddeliveryapp.data.seller.SellerFoodRequestGroup
import ir.nimaali.nimafooddeliveryapp.models.home.Restaurant
import ir.nimaali.nimafooddeliveryapp.models.seller.Food
import ir.nimaali.nimafooddeliveryapp.screen.functions.LoadingProgressbar
import ir.nimaali.nimafooddeliveryapp.ui.theme.BackgroundColor
import ir.nimaali.nimafooddeliveryapp.ui.theme.PrimaryColor
import ir.nimaali.nimafooddeliveryapp.ui.theme.SurfaceColor
import ir.nimaali.nimafooddeliveryapp.ui.theme.vazirFontFamily
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SellerFoodListScreen(navHostController: NavHostController) {

    var isDeleteDialogOpen by remember { mutableStateOf(false) }
    var selected_item_id by remember { mutableStateOf(0) }


    var listFoods by remember {
        mutableStateOf(emptyList<Food>())
    }

    var onGoingProgress by remember {
        mutableStateOf(true)
    }

    val m_context = LocalContext.current

    val seller_food_request_group = SellerFoodRequestGroup(m_context)

    seller_food_request_group.showSellerFoods { _, it ->
        listFoods = it.foods
        onGoingProgress = false
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "لیست غذاها",
                        style = MaterialTheme.typography.headlineSmall,
                        fontFamily = vazirFontFamily,
                        textAlign = TextAlign.Center,
                        color = Color.White,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF4CAF50)),
                actions = {
                    IconButton(onClick = {
                        navHostController.popBackStack()
                    }) {
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

        if (onGoingProgress) {

            LoadingProgressbar {

            }

        } else {
            if (listFoods.isEmpty()) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text(
                        "رستورانی در شهر شما قابل ارائه سرویس نمی باشد!",
                        color = MaterialTheme.colorScheme.error
                    )


                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .padding(innerPadding)
                        .padding(16.dp)
                ) {
                    items(listFoods) { food ->
                        Card(
                            modifier = Modifier
                                .padding(vertical = 8.dp)
                                .fillMaxWidth(),
                            elevation = CardDefaults.cardElevation(4.dp)
                        ) {
                            Column(modifier = Modifier.padding(8.dp)) {
                                ListItem(
                                    headlineContent = {
                                        Text(
                                            text = food.name,
                                            style = MaterialTheme.typography.bodyLarge
                                        )
                                    },
                                    supportingContent = {
                                        Text(
                                            text = food.description,
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontFamily = vazirFontFamily
                                        )
                                    },
                                    trailingContent = {
                                        Text(
                                            text = "${food.price} تومان",
                                            color = Color(0xFF4CAF50),
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontFamily = vazirFontFamily
                                        )
                                    },
                                    modifier = Modifier.padding(8.dp)
                                )

                                // دکمه‌های ویرایش و حذف
                                Row(
                                    horizontalArrangement = Arrangement.End,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    IconButton(onClick = {
                                        isDeleteDialogOpen = true
                                        selected_item_id = food.id
                                    }) {
                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = "حذف",
                                            tint = Color.Red
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(8.dp))
                                    IconButton(onClick = {
                                        selected_item_id = food.id
                                        navHostController.navigate("seller/edit/food/$selected_item_id")
                                    }) {
                                        Icon(
                                            imageVector = Icons.Default.Edit,
                                            contentDescription = "ویرایش",
                                            tint = Color.DarkGray
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            if (isDeleteDialogOpen) {
                AlertDialog(
                    onDismissRequest = {
                        // بستن دیالوگ در صورت نیاز
                        isDeleteDialogOpen = false
                    },
                    title = {
                        Text(text = "آیا مطمئن هستید؟") // عنوان دیالوگ
                    },
                    text = {
                        Text(text = "این آیتم حذف خواهد شد. این عمل قابل بازگشت نیست.") // متن توضیحی دیالوگ
                    },
                    confirmButton = {
                        TextButton(onClick = {
                            seller_food_request_group.deleteSellerFoods(selected_item_id.toString()) {
                                seller_food_request_group.showSellerFoods { _, it ->
                                    listFoods = it.foods
                                    onGoingProgress = false
                                }
                            }
                            isDeleteDialogOpen = false // دیالوگ را ببندید
                        }) {
                            Text(text = "بله", color = Color.Red) // دکمه تایید حذف
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = {
                            isDeleteDialogOpen = false // بستن دیالوگ بدون حذف
                        }) {
                            Text(text = "خیر") // دکمه لغو حذف
                        }
                    }
                )
            }

        }
    }
}
