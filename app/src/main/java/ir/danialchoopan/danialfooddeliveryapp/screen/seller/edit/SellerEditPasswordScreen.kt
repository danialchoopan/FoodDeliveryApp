package ir.nimaali.nimafooddeliveryapp.screen.seller.edit


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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditSellerPasswordScreen(navController: NavController) {
    // context
    val m_context = LocalContext.current

    // input fields
    var old_password by remember { mutableStateOf("") }
    var new_password by remember { mutableStateOf("") }
    var new_confirm_password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    Scaffold(
        modifier = Modifier.background(color = BackgroundColor),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "ویرایش رمزعبور رستوران",
                        style = MaterialTheme.typography.headlineSmall,
                        fontFamily = vazirFontFamily,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        color = Color.White
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF1565C0)) // Deep Blue
                , navigationIcon = {
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

                // رمز عبور
                OutlinedTextField(
                    value = old_password,
                    onValueChange = { old_password = it },
                    label = { Text("رمزعبور قبلی") },
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    maxLines = 1
                )


                Spacer(modifier = Modifier.height(8.dp))

                // رمز عبور
                OutlinedTextField(
                    value = new_password,
                    onValueChange = { new_password = it },
                    label = { Text("رمز عبور جدید") },
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    maxLines = 1
                )

                Spacer(modifier = Modifier.height(10.dp))

                // تکرار رمز عبور
                OutlinedTextField(
                    value = new_confirm_password,
                    onValueChange = { new_confirm_password = it },
                    label = { Text("تکرار رمز عبور جدید") },
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    maxLines = 1
                )

                Spacer(modifier = Modifier.height(16.dp))

                // دکمه ذخیره تغییرات
                Button(
                    onClick = {
                        if (new_password.trim() == new_confirm_password.trim()) {
                            val userAuthRequestGroup = UserAuthRequestGroup(m_context)
                            userAuthRequestGroup.sellerEditPasswordEdit(
                                old_password.trim(),
                                new_password.trim()
                            ) { success ->
                                if (success) {
                                    navController.navigate("dashboard_seller") {
                                        popUpTo(0)
                                    }
                                } else {
                                    errorMessage = "مشکلی پیش امده است لطفا بعدا امتحان کنید"
                                }
                            }

                        } else {
                            errorMessage = "رمزعبور جدید شما با تکرار ان برابر نیست"
                        }
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
