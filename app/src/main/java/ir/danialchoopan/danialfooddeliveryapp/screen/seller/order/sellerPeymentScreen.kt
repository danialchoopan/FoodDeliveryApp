package ir.danialchoopan.danialfooddeliveryapp.screen.seller.order

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import ir.danialchoopan.danialfooddeliveryapp.data.seller.SellerHomeRequestGroup
import ir.danialchoopan.danialfooddeliveryapp.formatPrice
import ir.danialchoopan.danialfooddeliveryapp.screen.functions.*
import ir.danialchoopan.danialfooddeliveryapp.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SellerMonthlyIncomeScreen(navHostController: NavHostController) {

    val m_context = androidx.compose.ui.platform.LocalContext.current

    val sellerHomeRequestGroup = SellerHomeRequestGroup(m_context)
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
        containerColor = BackgroundColor,
        topBar = {
            GradientTopBar(
                title = "درآمد ماهانه",
                actions = {
                    IconButton(onClick = { navHostController.popBackStack() }) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = "برگشت",
                            modifier = Modifier.size(42.dp),
                            tint = MaterialTheme.colorScheme.onPrimary
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
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                brush = GradientPrimary,
                                shape = RoundedCornerShape(16.dp)
                            )
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                "درآمد ماهانه",
                                fontFamily = vazirFontFamily,
                                style = TextStyle(
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White.copy(alpha = 0.9f)
                                )
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                "${formatPrice(monthlyIncome)} تومان",
                                style = TextStyle(
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                ),
                                fontFamily = vazirFontFamily
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        ModernCard(
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    "سفارش‌های موفق",
                                    fontFamily = vazirFontFamily,
                                    style = TextStyle(fontSize = 14.sp, color = TextSecondary)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    "$successfulOrders",
                                    fontFamily = vazirFontFamily,
                                    style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold, color = SuccessColor)
                                )
                            }
                        }
                        ModernCard(
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    "سفارش‌های ناموفق",
                                    fontFamily = vazirFontFamily,
                                    style = TextStyle(fontSize = 14.sp, color = TextSecondary)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    "$failedOrders",
                                    fontFamily = vazirFontFamily,
                                    style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold, color = ErrorColor)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    if (monthlyIncome.toInt() > 100000) {
                        GradientButton(
                            text = "تسویه حساب",
                            onClick = { showDialog = true },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    )
}
