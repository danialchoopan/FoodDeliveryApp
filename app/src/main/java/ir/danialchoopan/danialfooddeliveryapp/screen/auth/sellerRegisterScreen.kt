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
fun SellerRegisterScreen(navController: NavController) {
    var restaurantName by remember { mutableStateOf("") }
    var sellerName by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var selectedCity by remember { mutableStateOf("مشهد") }
    var selectedCategory by remember { mutableStateOf<String?>(null) }
    var expandedCity by remember { mutableStateOf(false) }
    var expandedCategory by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    val cities = listOf("مشهد", "سبزوار", "نیشابور", "تربت حیدریه", "کاشمر")
    val categories = listOf("رستوران", "فست‌فود", "کافه", "شیرینی‌فروشی")

    val authUserViewModel: AuthUserSellerViewModel = viewModel()
    val m_context = LocalContext.current

    Scaffold(
        modifier = Modifier.background(color = BackgroundColor),
        topBar = {
            GradientTopBar(title = "ثبت‌نام فروشنده")
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
                OutlinedTextField(
                    value = restaurantName,
                    onValueChange = { restaurantName = it },
                    label = { Text("نام رستوران", fontFamily = vazirFontFamily) },
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
                    value = phone,
                    onValueChange = { phone = it },
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
                    maxLines = 1
                )

                Spacer(modifier = Modifier.height(8.dp))

                ExposedDropdownMenuBox(
                    expanded = expandedCity,
                    onExpandedChange = { expandedCity = !expandedCity }
                ) {
                    OutlinedTextField(
                        value = selectedCity,
                        onValueChange = {},
                        label = { Text("شهر", fontFamily = vazirFontFamily) },
                        readOnly = true,
                        modifier = Modifier.menuAnchor().fillMaxWidth(),
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
                        expanded = expandedCity,
                        onDismissRequest = { expandedCity = false }
                    ) {
                        cities.forEach { city ->
                            DropdownMenuItem(
                                onClick = {
                                    selectedCity = city
                                    expandedCity = false
                                },
                                text = { Text(city, fontFamily = vazirFontFamily) }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                ExposedDropdownMenuBox(
                    expanded = expandedCategory,
                    onExpandedChange = { expandedCategory = !expandedCategory }
                ) {
                    OutlinedTextField(
                        value = selectedCategory ?: "انتخاب دسته‌بندی",
                        onValueChange = {},
                        label = { Text("دسته‌بندی", fontFamily = vazirFontFamily) },
                        readOnly = true,
                        modifier = Modifier.menuAnchor().fillMaxWidth(),
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
                        expanded = expandedCategory,
                        onDismissRequest = { expandedCategory = false }
                    ) {
                        categories.forEach { category ->
                            DropdownMenuItem(
                                onClick = {
                                    selectedCategory = category
                                    expandedCategory = false
                                },
                                text = { Text(category, fontFamily = vazirFontFamily) }
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
                    maxLines = 1
                )

                Spacer(modifier = Modifier.height(8.dp))

                GradientButton(
                    text = "ثبت‌نام",
                    onClick = {
                        if (password == confirmPassword && selectedCategory != null) {
                            authUserViewModel.sellerRegisterRequest(
                                m_context,
                                restaurantName,
                                selectedCategory!!, phone, selectedCity, address, password
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
                            errorMessage = "لطفاً تمامی فیلدها را به درستی پر کنید."
                        }
                    }
                )

                if (errorMessage.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(errorMessage, color = MaterialTheme.colorScheme.error, fontFamily = vazirFontFamily)
                }
                Spacer(modifier = Modifier.height(8.dp))
                TextButton(onClick = {
                    navController.navigate("seller_login")
                }) {
                    Text(
                        "حساب کاربری دارید؟ ورود فروشنده",
                        style = MaterialTheme.typography.titleMedium,
                        fontFamily = vazirFontFamily,
                    )
                }
            }
        }
    )
}
