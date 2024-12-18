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
import androidx.compose.material.icons.filled.ArrowDropDown
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
fun UserRegisterScreen(navController: NavController) {
    //context
    val m_context = LocalContext.current

    //input
    var userName by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }


    val authUserViewModel: AuthUserSellerViewModel = viewModel()
    // لیست شهرستان‌های خراسان رضوی
    var cities = listOf(
        "مشهد", "سبزوار", "نیشابور", "تربت حیدریه", "کاشمر", "خواف", "فریمان", "چناران", "کلات"
    )

    var context = LocalContext.current

    Scaffold(
        modifier = Modifier.background(color = BackgroundColor),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "ثبت‌نام کاربر",
                        style = MaterialTheme.typography.headlineSmall,
                        fontFamily = vazirFontFamily,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        color = Color.White
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1565C0) // Deep Blue
                )
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
                    "ثبت‌نام کاربر جدید",
                    style = MaterialTheme.typography.headlineSmall,
                    fontFamily = vazirFontFamily,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Right,
                    color = Color(0xFF03A9F4)
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = userName,
                    onValueChange = { userName = it },
                    label = { Text("نام و نام خانوادگی") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 1
                )
                Spacer(modifier = Modifier.height(8.dp))


                OutlinedTextField(
                    value = phoneNumber,
                    onValueChange = { phoneNumber = it },
                    label = { Text("شماره تلفن") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    maxLines = 1
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("رمز عبور") },
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    maxLines = 1
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    label = { Text("تکرار رمز عبور") },
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    maxLines = 1
                )

                Spacer(modifier = Modifier.height(10.dp))

                // مقداردهی اولیه متغیر selectedCity با مقدار پیش‌فرض
                var selectedCity by remember { mutableStateOf(cities[0]) } // "مشهد" به عنوان مقدار پیش‌فرض

                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        value = selectedCity, // مقدار فعلی انتخاب شده
                        onValueChange = {}, // به‌روزرسانی مستقیم نیازی نیست
                        label = { Text("شهر") },
                        readOnly = true, // این فیلد فقط خواندنی است
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth(),
                        trailingIcon = {
                            Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                        }
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        cities.forEach { city ->
                            DropdownMenuItem(
                                onClick = {
                                    selectedCity = city // انتخاب شهر جدید
                                    expanded = false // بستن منو
                                },
                                text = { Text(city) }
                            )
                        }
                    }
                }



                Spacer(modifier = Modifier.height(16.dp))

                // فیلد آدرس
                OutlinedTextField(
                    value = address,
                    onValueChange = { address = it },
                    label = { Text("آدرس") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions.Default,
                    maxLines = 1
                )

                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
//                        // تابع ثبت‌نام کاربر
                        if (password == confirmPassword) {
                            authUserViewModel.userRegisterRequest(
                                m_context,
                                userName,
                                phoneNumber,
                                selectedCity,
                                address,
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
                            errorMessage = "رمز عبور و تکرار رمز عبور یکسان نیستند."
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        "ثبت‌نام",
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
                    navController.navigate("user_login")
                }) {
                    Text(
                        "حساب کاربری دارید؟ وارد شوید",
                        style = MaterialTheme.typography.titleMedium,
                        fontFamily = vazirFontFamily,
                    )
                }
            }
        }
    )
}