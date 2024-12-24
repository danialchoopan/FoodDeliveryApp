package ir.nimaali.nimafooddeliveryapp.screen.seller.order


import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.provider.CalendarContract.Colors
import android.view.MenuItem
import android.widget.Toast
import androidx.compose.foundation.Image
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import ir.nimaali.nimafooddeliveryapp.data.seller.SellerHomeRequestGroup
import ir.nimaali.nimafooddeliveryapp.screen.functions.LoadingProgressbar
import ir.nimaali.nimafooddeliveryapp.ui.theme.BackgroundColor
import ir.nimaali.nimafooddeliveryapp.ui.theme.PrimaryColor
import ir.nimaali.nimafooddeliveryapp.ui.theme.SurfaceColor
import ir.nimaali.nimafooddeliveryapp.ui.theme.vazirFontFamily
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SellerMonthlyIncomeScreen(navHostController: NavHostController) {

    val m_context = LocalContext.current

    val sellerHomeRequestGroup = SellerHomeRequestGroup(m_context)
    // درآمد ماهانه
    var monthlyIncome by remember {
        mutableStateOf("")
    }

    var successfulOrders by remember {
        mutableStateOf("")
    }

    var failedOrders by remember {
        mutableStateOf("")
    }
    var onGoingProgress by remember {
        mutableStateOf(true)
    }

    sellerHomeRequestGroup.sellerGetTotalOrder { success, failed, totalOrder ->

        monthlyIncome = totalOrder
        successfulOrders = success
        failedOrders = failed

        onGoingProgress=false

    }


    // نمایش دیالوگ تسویه حساب
    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = {
                Text(
                    "تسویه حساب",
                    fontFamily = vazirFontFamily
                )
            },
            text = {
                Text(
                    "آیا می‌خواهید تسویه حساب انجام دهید؟",
                    fontFamily = vazirFontFamily
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDialog = false
                    }
                ) {
                    Text(
                        "تسویه",
                        fontFamily = vazirFontFamily
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text(
                        "لغو",
                        fontFamily = vazirFontFamily
                    )
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "درآمد ماهانه",
                        style = MaterialTheme.typography.headlineSmall,
                        fontFamily = vazirFontFamily,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        color = Color.White
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF4CAF50)),
                actions = {
                    IconButton(onClick = { navHostController.popBackStack() }) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = "برگشت",
                            modifier = Modifier.size(42.dp),
                            tint = Color.White
                        )
                    }
                }
            )
        },
        content = { paddingValues ->
            if (onGoingProgress) {

                LoadingProgressbar {

                }

            } else {
                Column(
                    modifier = Modifier
                        .padding(paddingValues)
                        .padding(18.dp)
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // نمایش پیام درآمد ماهانه
                    Text(
                        "درآمد ماهانه",
                        fontFamily = vazirFontFamily,
                        style = TextStyle(
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black.copy(alpha = 0.87f)
                        )
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        "${monthlyIncome.toString()} تومان",
                        style = TextStyle(
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF013220)
                        ),
                        fontFamily = vazirFontFamily
                    )
                    Divider(modifier = Modifier.padding(vertical = 24.dp), color = Color.Gray)

                    // تعداد سفارش‌های موفق
                    Text(
                        "تعداد سفارش‌های موفق: $successfulOrders",
                        style = TextStyle(
                            fontSize = 16.sp, color = Color(0xFF013220),
                            fontFamily = vazirFontFamily
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    // تعداد سفارش‌های ناموفق
                    Text(
                        "تعداد سفارش‌های ناموفق: $failedOrders",
                        fontFamily = vazirFontFamily,
                        style = TextStyle(fontSize = 16.sp, color = Color.Red.copy(alpha = 0.7f))
                    )
                    Divider(modifier = Modifier.padding(vertical = 24.dp), color = Color.Gray)

                    // دکمه تسویه حساب اگر درآمد بیشتر از 100,000 تومان باشد
                    if (monthlyIncome.toInt() > 100000) {
                        Button(
                            onClick = { showDialog = true },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                "تسویه حساب",
                                style = MaterialTheme.typography.titleMedium,
                                fontFamily = vazirFontFamily,
                            )
                        }
                    }
                }
            }
        }
    )
}
