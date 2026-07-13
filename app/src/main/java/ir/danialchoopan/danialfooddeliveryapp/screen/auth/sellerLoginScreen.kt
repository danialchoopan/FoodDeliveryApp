package ir.danialchoopan.danialfooddeliveryapp.screen.auth

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import ir.danialchoopan.danialfooddeliveryapp.screen.functions.GradientButton
import ir.danialchoopan.danialfooddeliveryapp.screen.functions.GradientTopBar
import ir.danialchoopan.danialfooddeliveryapp.ui.theme.BackgroundColor
import ir.danialchoopan.danialfooddeliveryapp.ui.theme.BorderColor
import ir.danialchoopan.danialfooddeliveryapp.ui.theme.PrimaryColor
import ir.danialchoopan.danialfooddeliveryapp.ui.theme.TextPrimary
import ir.danialchoopan.danialfooddeliveryapp.ui.theme.vazirFontFamily
import ir.danialchoopan.danialfooddeliveryapp.viewmodel.AuthUserSellerViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SellerLoginScreen(navController: NavController) {
    var phoneNumber by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    val authUserViewModel: AuthUserSellerViewModel = viewModel()
    val m_context = LocalContext.current

    Scaffold(
        modifier = Modifier.background(color = BackgroundColor),
        topBar = {
            GradientTopBar(
                title = "ورود فروشندگان",
                navigationIcon = {
                    IconButton(onClick = {
                        navController.navigate("user_login") {
                            popUpTo(0)
                        }
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
        content = { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    "دانیال فود",
                    style = MaterialTheme.typography.headlineLarge,
                    fontFamily = vazirFontFamily,
                    color = PrimaryColor,
                    fontSize = 32.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    "فروش بیشتر با ما!",
                    style = MaterialTheme.typography.headlineSmall,
                    fontFamily = vazirFontFamily,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = phoneNumber,
                    onValueChange = { phoneNumber = it },
                    label = { Text("شماره تلفن", fontFamily = vazirFontFamily) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PrimaryColor,
                        unfocusedBorderColor = BorderColor,
                        cursorColor = PrimaryColor
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    maxLines = 1
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("رمز عبور", fontFamily = vazirFontFamily) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PrimaryColor,
                        unfocusedBorderColor = BorderColor,
                        cursorColor = PrimaryColor
                    ),
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    maxLines = 1
                )

                Spacer(modifier = Modifier.height(20.dp))

                GradientButton(
                    text = "ورود",
                    onClick = {
                        if (password.isNotEmpty() && phoneNumber.isNotEmpty()) {
                            authUserViewModel.sellerLoginRequest(
                                m_context,
                                phoneNumber,
                                password
                            ) { success ->
                                if (success) {
                                    navController.navigate("dashboard_seller") {
                                        popUpTo(0)
                                    }
                                } else {
                                    errorMessage = "مشکلی پیش آمده است لطفا بعدا امتحان کنید"
                                }
                            }
                        } else {
                            errorMessage = "لطفا فیلد های لازم را پر کنید"
                        }
                    }
                )

                if (errorMessage.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(errorMessage, color = MaterialTheme.colorScheme.error, fontFamily = vazirFontFamily)
                }

                Spacer(modifier = Modifier.height(12.dp))

                TextButton(onClick = {
                    navController.navigate("seller_register")
                }) {
                    Text(
                        "حساب کاربری ندارید؟ ثبت‌نام فروشنده",
                        style = MaterialTheme.typography.titleMedium,
                        fontFamily = vazirFontFamily,
                    )
                }
            }
        }
    )
}

suspend fun checkSellerLogin(context: Context, navController: NavController) {
    withContext(Dispatchers.IO) {
        val prefs: SharedPreferences =
            context.getSharedPreferences("seller_prefs", Context.MODE_PRIVATE)
        val token = prefs.getString("token", null)
        val accessLevel = prefs.getString("user", null)

        if (!token.isNullOrEmpty()) {
            withContext(Dispatchers.Main) {
                if (accessLevel == "seller") {
                    navController.navigate("dashboard_screen") {
                        popUpTo(0)
                    }
                } else {
                    navController.navigate("home_screen") {
                        popUpTo(0)
                    }
                }
            }
        }
    }
}
