package ir.nimaali.nimafooddeliveryapp.screen.seller.comment


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
import ir.nimaali.nimafooddeliveryapp.data.seller.SellerCommentRequestGroup
import ir.nimaali.nimafooddeliveryapp.data.seller.SellerFoodRequestGroup
import ir.nimaali.nimafooddeliveryapp.models.home.Restaurant
import ir.nimaali.nimafooddeliveryapp.models.seller.Food
import ir.nimaali.nimafooddeliveryapp.models.seller.comment.Comment
import ir.nimaali.nimafooddeliveryapp.screen.functions.LoadingProgressbar
import ir.nimaali.nimafooddeliveryapp.ui.theme.BackgroundColor
import ir.nimaali.nimafooddeliveryapp.ui.theme.PrimaryColor
import ir.nimaali.nimafooddeliveryapp.ui.theme.SurfaceColor
import ir.nimaali.nimafooddeliveryapp.ui.theme.vazirFontFamily
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SellerCommentScreen(navHostController: NavHostController) {

    var isDeleteDialogOpen by remember { mutableStateOf(false) }
    var selected_item_id by remember { mutableStateOf(0) }


    var listComment by remember {
        mutableStateOf(emptyList<Comment>())
    }

    var onGoingProgress by remember {
        mutableStateOf(true)
    }

    val m_context = LocalContext.current

    val seller_comment_request_group = SellerCommentRequestGroup(m_context)

    seller_comment_request_group.showCommentSeller { _, it ->
        listComment = it.comments
        onGoingProgress = false
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "لیست نظرات رستوران",
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
            if (listComment.isEmpty()) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text(
                        "درمورد رستوران شما هنوز نظری ثبت نشده است",
                        color = MaterialTheme.colorScheme.error
                    )


                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .padding(innerPadding)
                        .padding(16.dp)
                ) {
                    items(listComment) { comment ->
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
                                            text = comment.commentContent,
                                            style = MaterialTheme.typography.bodyLarge
                                        )
                                    },
                                    supportingContent = {

                                    },
                                    trailingContent = {

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
                                        selected_item_id = comment.commentId
                                    }) {
                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = "حذف",
                                            tint = Color.Red
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
                            seller_comment_request_group.DeleteCommentSeller(selected_item_id.toString()) {

                                onGoingProgress = true
                                seller_comment_request_group.showCommentSeller { _, it ->
                                    listComment = it.comments
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
