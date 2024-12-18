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
import androidx.navigation.NavController
import ir.nimaali.nimafooddeliveryapp.ui.theme.BackgroundColor
import ir.nimaali.nimafooddeliveryapp.ui.theme.PrimaryColor
import ir.nimaali.nimafooddeliveryapp.ui.theme.SurfaceColor
import ir.nimaali.nimafooddeliveryapp.ui.theme.vazirFontFamily
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
    var selectedCity by remember { mutableStateOf("مشهد") } // مقدار پیش‌فرض
    var selectedCategory by remember { mutableStateOf<String?>(null) }
    var expandedCity by remember { mutableStateOf(false) }
    var expandedCategory by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    // لیست شهرستان‌ها و دسته‌بندی‌ها
    val cities = listOf("مشهد", "سبزوار", "نیشابور", "تربت حیدریه", "کاشمر")
    val categories = listOf("رستوران", "فست‌فود", "کافه", "شیرینی‌فروشی")

    Scaffold(
        modifier = Modifier.background(color = BackgroundColor),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "ثبت‌نام فروشنده",
                        style = MaterialTheme.typography.headlineSmall,
                        fontFamily = vazirFontFamily,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        color = Color.White
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF1565C0))
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
                OutlinedTextField(
                    value = restaurantName,
                    onValueChange = { restaurantName = it },
                    label = { Text("نام رستوران") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 1
                )

                Spacer(modifier = Modifier.height(8.dp))

//                OutlinedTextField(
//                    value = sellerName,
//                    onValueChange = { sellerName = it },
//                    label = { Text("نام فروشنده") },
//                    modifier = Modifier.fillMaxWidth(),
//                    maxLines = 1
//                )
//
//                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("شماره تلفن") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    maxLines = 1
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("رمز عبور") },
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = PasswordVisualTransformation(),
                    maxLines = 1
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    label = { Text("تکرار رمز عبور") },
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = PasswordVisualTransformation(),
                    maxLines = 1
                )

                Spacer(modifier = Modifier.height(8.dp))

                // انتخاب شهر
                ExposedDropdownMenuBox(
                    expanded = expandedCity,
                    onExpandedChange = { expandedCity = !expandedCity }
                ) {
                    OutlinedTextField(
                        value = selectedCity,
                        onValueChange = {},
                        label = { Text("شهر") },
                        readOnly = true,
                        modifier = Modifier.menuAnchor().fillMaxWidth(),
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
                                text = { Text(city) }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // انتخاب دسته‌بندی
                ExposedDropdownMenuBox(
                    expanded = expandedCategory,
                    onExpandedChange = { expandedCategory = !expandedCategory }
                ) {
                    OutlinedTextField(
                        value = selectedCategory ?: "انتخاب دسته‌بندی",
                        onValueChange = {},
                        label = { Text("دسته‌بندی") },
                        readOnly = true,
                        modifier = Modifier.menuAnchor().fillMaxWidth(),
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
                                text = { Text(category) }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = address,
                    onValueChange = { address = it },
                    label = { Text("آدرس") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 1
                )

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = {
                        if (password == confirmPassword && selectedCategory != null) {
//                            registerSeller(
//                                restaurantName = restaurantName,
//                                sellerName = sellerName,
//                                phone = phone,
//                                seller_password = password,
//                                city = selectedCity,
//                                category = selectedCategory!!,
//                                address = address
//                            )
                        } else {
                            errorMessage = "لطفاً تمامی فیلدها را به درستی پر کنید."
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("ثبت‌نام", fontFamily = vazirFontFamily, style = MaterialTheme.typography.headlineSmall)
                }

                if (errorMessage.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(errorMessage, color = MaterialTheme.colorScheme.error)
                }
                Spacer(modifier = Modifier.height(10.dp))
                TextButton(onClick = {
                    navController.navigate("seller_register")
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
