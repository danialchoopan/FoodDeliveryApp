package ir.nimaali.nimafooddeliveryapp.screen.user.edit

import android.widget.Toast
import androidx.compose.foundation.background
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
import ir.nimaali.nimafooddeliveryapp.data.user.UserAuthRequestGroup
import ir.nimaali.nimafooddeliveryapp.ui.theme.BackgroundColor
import ir.nimaali.nimafooddeliveryapp.ui.theme.vazirFontFamily
import ir.nimaali.nimafooddeliveryapp.viewmodel.AuthUserSellerViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditUserScreen(navController: NavController) {
    // context
    val m_context = LocalContext.current

    // input fields
    var selectedCity by remember { mutableStateOf("") } // مقدار پیش‌فرض
    var expanded by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }


    val userAuthRequestGroup=UserAuthRequestGroup(m_context)
    val old_city_name=userAuthRequestGroup.userSharedPreferences.getString("user_city","")

    // لیست شهرستان‌های خراسان رضوی
    val cities = listOf(
        "مشهد", "سبزوار", "نیشابور", "تربت حیدریه", "کاشمر", "خواف", "فریمان", "چناران", "کلات"
    )
    selectedCity=old_city_name.toString()

    Scaffold(
        modifier = Modifier.background(color = BackgroundColor),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "ویرایش اطلاعات کاربر",
                        style = MaterialTheme.typography.headlineSmall,
                        fontFamily = vazirFontFamily,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        color = Color.White
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF1565C0)) // Deep Blue
                ,
                navigationIcon = {
                    IconButton(onClick = {
                        // عملکرد بازگشت
                        navController.popBackStack()
                    }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack, // آیکون بازگشت پیش‌فرض
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

                // انتخاب شهر
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        value = selectedCity,
                        onValueChange = {}, // به‌روزرسانی مستقیم نیازی نیست
                        label = { Text("شهر") },
                        readOnly = true,
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
                                    selectedCity = city
                                    expanded = false
                                },
                                text = { Text(city) }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // دکمه ذخیره تغییرات
                Button(
                    onClick = {
                        if(selectedCity!=old_city_name){
                            userAuthRequestGroup.userEditCityName(selectedCity){success ->
                                if(success){
                                    Toast.makeText(m_context,"شهر شما با موفقیت تغییر کرد",Toast.LENGTH_SHORT).show()
                                    navController.navigate("home_user") {
                                        popUpTo(0)
                                    }
                                }else{
                                    errorMessage="مشکلی پیش آمده است لطفا بعدا امتحان کنید"
                                }
                            }
                        }else{
                            errorMessage="شهر انتخابی شما با شهر قبلی یکی است"
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        "ذخیره تغییرات",
                        fontFamily = vazirFontFamily,
                        style = MaterialTheme.typography.headlineSmall
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // دکمه ذخیره تغییرات
                Button(
                    onClick = {
                        navController.navigate("home/user/edit/password")
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        "تغییر رمزعبور",
                        fontFamily = vazirFontFamily,
                        style = MaterialTheme.typography.headlineSmall
                    )
                }

                // پیام خطا
                if (errorMessage.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(errorMessage, color = MaterialTheme.colorScheme.error)
                }
            }
        }
    )
}
