package ir.danialchoopan.danialfooddeliveryapp.screen.user.edit

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import ir.danialchoopan.danialfooddeliveryapp.data.user.UserAuthRequestGroup
import ir.danialchoopan.danialfooddeliveryapp.screen.functions.GradientButton
import ir.danialchoopan.danialfooddeliveryapp.screen.functions.GradientTopBar
import ir.danialchoopan.danialfooddeliveryapp.ui.theme.*
import ir.danialchoopan.danialfooddeliveryapp.viewmodel.AuthUserSellerViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditUserScreen(navController: NavController) {
    val m_context = LocalContext.current

    var selectedCity by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    val userAuthRequestGroup = UserAuthRequestGroup(m_context)
    val old_city_name = userAuthRequestGroup.userSharedPreferences.getString("user_city", "")

    val cities = listOf(
        "مشهد", "سبزوار", "نیشابور", "تربت حیدریه", "کاشمر", "خواف", "فریمان", "چناران", "کلات"
    )
    selectedCity = old_city_name.toString()

    Scaffold(
        containerColor = BackgroundColor,
        topBar = {
            GradientTopBar(
                title = "ویرایش اطلاعات کاربر",
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
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
                        trailingIcon = {
                            Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PrimaryColor,
                            unfocusedBorderColor = BorderColor,
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp)
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
                                text = {
                                    Text(city, fontFamily = vazirFontFamily)
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                GradientButton(
                    text = "ذخیره تغییرات",
                    onClick = {
                        if (selectedCity != old_city_name) {
                            userAuthRequestGroup.userEditCityName(selectedCity) { success ->
                                if (success) {
                                    Toast.makeText(m_context, "شهر شما با موفقیت تغییر کرد", Toast.LENGTH_SHORT).show()
                                    navController.navigate("home_user") {
                                        popUpTo(0)
                                    }
                                } else {
                                    errorMessage = "مشکلی پیش آمده است لطفا بعدا امتحان کنید"
                                }
                            }
                        } else {
                            errorMessage = "شهر انتخابی شما با شهر قبلی یکی است"
                        }
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                GradientButton(
                    text = "تغییر رمزعبور",
                    onClick = {
                        navController.navigate("home/user/edit/password")
                    }
                )

                if (errorMessage.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(errorMessage, color = ErrorColor)
                }
            }
        }
    )
}
