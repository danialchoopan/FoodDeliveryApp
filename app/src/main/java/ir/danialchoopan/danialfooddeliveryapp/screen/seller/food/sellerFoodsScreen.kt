package ir.danialchoopan.danialfooddeliveryapp.screen.seller.food

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import ir.danialchoopan.danialfooddeliveryapp.data.seller.SellerFoodRequestGroup
import ir.danialchoopan.danialfooddeliveryapp.models.seller.Food
import ir.danialchoopan.danialfooddeliveryapp.screen.functions.*
import ir.danialchoopan.danialfooddeliveryapp.ui.theme.*
import androidx.compose.ui.platform.LocalContext


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
        containerColor = BackgroundColor,
        topBar = {
            GradientTopBar(
                title = "لیست غذاها",
                actions = {
                    IconButton(onClick = {
                        navHostController.popBackStack()
                    }) {
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
    ) { innerPadding ->

        if (onGoingProgress) {

            LoadingProgressbar {

            }

        } else {
            if (listFoods.isEmpty()) {
                EmptyState(
                    message = "شما هنوز غذایی را اضافه نکرده اید",
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                LazyColumn(
                    modifier = Modifier
                        .padding(innerPadding)
                        .padding(16.dp)
                ) {
                    items(listFoods) { food ->
                        ModernCard(
                            modifier = Modifier
                                .padding(vertical = 8.dp)
                                .fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(8.dp)) {
                                ListItem(
                                    headlineContent = {
                                        Text(
                                            text = food.name,
                                            style = MaterialTheme.typography.bodyLarge,
                                            color = TextPrimary
                                        )
                                    },
                                    supportingContent = {
                                        Text(
                                            text = food.description,
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontFamily = vazirFontFamily,
                                            color = TextSecondary
                                        )
                                    },
                                    trailingContent = {
                                        PillBadge(
                                            text = "${food.price} تومان",
                                            backgroundColor = PrimaryColor
                                        )
                                    },
                                    modifier = Modifier.padding(8.dp)
                                )

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
                                            tint = ErrorColor
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
                                            tint = PrimaryColor
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
                        isDeleteDialogOpen = false
                    },
                    title = {
                        Text(text = "آیا مطمئن هستید?")
                    },
                    text = {
                        Text(text = "این آیتم حذف خواهد شد. این عمل قابل بازگشت نیست.")
                    },
                    confirmButton = {
                        TextButton(onClick = {
                            seller_food_request_group.deleteSellerFoods(selected_item_id.toString()) {
                                seller_food_request_group.showSellerFoods { _, it ->
                                    listFoods = it.foods
                                    onGoingProgress = false
                                }
                            }
                            isDeleteDialogOpen = false
                        }) {
                            Text(text = "بله", color = ErrorColor)
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = {
                            isDeleteDialogOpen = false
                        }) {
                            Text(text = "خیر")
                        }
                    }
                )
            }

        }
    }
}
