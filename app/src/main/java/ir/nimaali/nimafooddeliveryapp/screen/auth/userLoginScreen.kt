package ir.nimaali.nimafooddeliveryapp.screen.auth

import android.content.Context
import android.content.SharedPreferences
import android.provider.CalendarContract.Colors
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
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
fun UserLoginScreen(navController: NavController) {
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
                    "ورود کاربر",
                    style = MaterialTheme.typography.headlineSmall,
                    fontFamily = vazirFontFamily,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    color = Color.White
                )
            },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1565C0) // Deep Blue
                ),// Custom Light Blue (SkyBlue)
            actions = {
                // اقدامات
            }
        )

        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp)
                    .fillMaxSize()
                ,
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "ورود فروشندگان",
                    style = MaterialTheme.typography.headlineSmall,
                    fontFamily = vazirFontFamily,
                    modifier = Modifier.fillMaxWidth().clickable {
                        navController.navigate("seller_login")
                    },
                    textAlign = TextAlign.Right,
                    color = Color(0xFF03A9F4),
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    "نیما فود",
                    style = MaterialTheme.typography.headlineMedium,
                    fontFamily = vazirFontFamily
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "سفارش بده، لذت ببر!",
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
                            authUserViewModel.userLoginRequest(
                                m_context,
                                phoneNumber,
                                password
                            ) {success->
                                if(success){
                                    navController.navigate("home_user"){
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
                    Text("ورود", fontFamily = vazirFontFamily,
                        style = MaterialTheme.typography.headlineSmall)
                }

                if (errorMessage.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(errorMessage, color = MaterialTheme.colorScheme.error)
                }

                Spacer(modifier = Modifier.height(18.dp))

                TextButton(onClick = {
                    navController.navigate("user_register")
                }) {
                    Text(
                        "حساب کاربری ندارید؟ ثبت‌نام کنید",
                        style = MaterialTheme.typography.titleMedium,
                        fontFamily = vazirFontFamily,

                    )
                }
            }
        }
    )
}



