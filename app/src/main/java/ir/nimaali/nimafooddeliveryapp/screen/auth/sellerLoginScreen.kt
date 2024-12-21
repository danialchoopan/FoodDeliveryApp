package ir.nimaali.nimafooddeliveryapp.screen.auth

import android.content.Context
import android.content.SharedPreferences
import android.provider.CalendarContract.Colors
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import ir.nimaali.nimafooddeliveryapp.ui.theme.BackgroundColor
import ir.nimaali.nimafooddeliveryapp.ui.theme.PrimaryColor
import ir.nimaali.nimafooddeliveryapp.ui.theme.SurfaceColor
import ir.nimaali.nimafooddeliveryapp.ui.theme.vazirFontFamily
import ir.nimaali.nimafooddeliveryapp.viewmodel.AuthUserSellerViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SellerLoginScreen(navController: NavController) {
    var phoneNumber by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    //auth
    val authUserViewModel: AuthUserSellerViewModel = viewModel()

    //context
    val m_context= LocalContext.current

    Scaffold(
        modifier = Modifier.background(color = BackgroundColor),

        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "ورود فروشندگان",
                        style = MaterialTheme.typography.headlineSmall,
                        fontFamily = vazirFontFamily,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        color = Color.White
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1565C0) // Deep Blue
                ),
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

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    "نیما فود",
                    style = MaterialTheme.typography.headlineMedium,
                    fontFamily = vazirFontFamily
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "فروش بیشتر با ما!",
                    style = MaterialTheme.typography.headlineSmall,
                    fontFamily = vazirFontFamily
                )
                Spacer(modifier = Modifier.height(14.dp))

                OutlinedTextField(
                    value = phoneNumber,
                    onValueChange = { phoneNumber = it },
                    label = { Text("شماره تلفن") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    maxLines = 1
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("رمز عبور") },
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    maxLines = 1
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        if (password.isNotEmpty() && phoneNumber.isNotEmpty()) {
                            authUserViewModel.sellerLoginRequest(
                                m_context,
                                phoneNumber,
                                password
                            ) {success->
                                if(success){
                                    navController.navigate("dashboard_seller"){
                                        popUpTo(0)
                                    }
                                }else{
                                    errorMessage = "مشکلی پیش آمده است لطفا بعدا امتحان کنید"
                                }
                            }
                        } else {
                            errorMessage = "لطفا فیلد های لازم را پر کنید"
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        "ورود",
                        fontFamily = vazirFontFamily,
                        style = MaterialTheme.typography.headlineSmall
                    )
                }

                if (errorMessage.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(errorMessage, color = MaterialTheme.colorScheme.error)
                }

                Spacer(modifier = Modifier.height(18.dp))

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

// بررسی وضعیت ورود فروشنده
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
