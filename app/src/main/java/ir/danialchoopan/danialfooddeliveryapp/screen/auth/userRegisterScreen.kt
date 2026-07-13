package ir.danialchoopan.danialfooddeliveryapp.screen.auth

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
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
fun UserRegisterScreen(navController: NavController) {
    val m_context = LocalContext.current

    var userName by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }

    val authUserViewModel: AuthUserSellerViewModel = viewModel()
    val cities = listOf(
        "مشهد", "سبزوار", "نیشابور", "تربت حیدریه", "کاشمر", "خواف", "فریمان", "چناران", "کلات"
    )

    var context = LocalContext.current

    Scaffold(
        modifier = Modifier.background(color = BackgroundColor),
        topBar = {
            GradientTopBar(title = "ثبت‌نام کاربر")
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
                    color = PrimaryColor
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = userName,
                    onValueChange = { userName = it },
                    label = { Text("نام و نام خانوادگی", fontFamily = vazirFontFamily) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PrimaryColor,
                        unfocusedBorderColor = BorderColor,
                        cursorColor = PrimaryColor
                    ),
                    maxLines = 1
                )
                Spacer(modifier = Modifier.height(8.dp))

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

                Spacer(modifier = Modifier.height(8.dp))

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

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    label = { Text("تکرار رمز عبور", fontFamily = vazirFontFamily) },
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

                Spacer(modifier = Modifier.height(8.dp))

                var selectedCity by remember { mutableStateOf(cities[0]) }

                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        value = selectedCity,
                        onValueChange = {},
                        label = { Text("شهر", fontFamily = vazirFontFamily) },
                        readOnly = true,
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PrimaryColor,
                            unfocusedBorderColor = BorderColor,
                            cursorColor = PrimaryColor
                        ),
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
                                    selectedCity = city
                                    expanded = false
                                },
                                text = { Text(city, fontFamily = vazirFontFamily) }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = address,
                    onValueChange = { address = it },
                    label = { Text("آدرس", fontFamily = vazirFontFamily) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PrimaryColor,
                        unfocusedBorderColor = BorderColor,
                        cursorColor = PrimaryColor
                    ),
                    keyboardOptions = KeyboardOptions.Default,
                    maxLines = 1
                )

                Spacer(modifier = Modifier.height(16.dp))

                GradientButton(
                    text = "ثبت‌نام",
                    onClick = {
                        if (password == confirmPassword) {
                            authUserViewModel.userRegisterRequest(
                                m_context,
                                userName,
                                phoneNumber,
                                selectedCity,
                                address,
                                password
                            ) { success ->
                                if (success) {
                                    navController.navigate("home_user") {
                                        popUpTo(0)
                                    }
                                } else {
                                    errorMessage = "مشکلی پیش آمده است لطفا بعدا امتحان کنید"
                                }
                            }
                        } else {
                            errorMessage = "رمز عبور و تکرار رمز عبور یکسان نیستند."
                        }
                    }
                )

                if (errorMessage.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(errorMessage, color = MaterialTheme.colorScheme.error, fontFamily = vazirFontFamily)
                }

                Spacer(modifier = Modifier.height(8.dp))

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
