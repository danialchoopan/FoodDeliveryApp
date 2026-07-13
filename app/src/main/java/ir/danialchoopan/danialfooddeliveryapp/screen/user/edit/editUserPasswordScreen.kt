package ir.danialchoopan.danialfooddeliveryapp.screen.user.edit

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditUserPasswordScreen(navController: NavController) {
    val m_context = LocalContext.current

    var old_password by remember { mutableStateOf("") }
    var new_password by remember { mutableStateOf("") }
    var new_confirm_password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    Scaffold(
        containerColor = BackgroundColor,
        topBar = {
            GradientTopBar(
                title = "ویرایش رمزعبور کاربر",
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

                OutlinedTextField(
                    value = old_password,
                    onValueChange = { old_password = it },
                    label = { Text("رمزعبور قبلی", fontFamily = vazirFontFamily) },
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    maxLines = 1,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PrimaryColor,
                        unfocusedBorderColor = BorderColor,
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = new_password,
                    onValueChange = { new_password = it },
                    label = { Text("رمز عبور جدید", fontFamily = vazirFontFamily) },
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    maxLines = 1,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PrimaryColor,
                        unfocusedBorderColor = BorderColor,
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = new_confirm_password,
                    onValueChange = { new_confirm_password = it },
                    label = { Text("تکرار رمز عبور جدید", fontFamily = vazirFontFamily) },
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    maxLines = 1,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PrimaryColor,
                        unfocusedBorderColor = BorderColor,
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(20.dp))

                GradientButton(
                    text = "تغییر رمزعبور",
                    onClick = {
                        if (new_password.trim() == new_confirm_password.trim()) {
                            val userAuthRequestGroup = UserAuthRequestGroup(m_context)
                            userAuthRequestGroup.userEditPasswordEdit(
                                old_password.trim(),
                                new_password.trim()
                            ) { success ->
                                if (success) {
                                    navController.navigate("home_user") {
                                        popUpTo(0)
                                    }
                                } else {
                                    errorMessage = "مشکلی پیش امده است لطفا بعدا امتحان کنید"
                                }
                            }

                        } else {
                            errorMessage = "رمزعبور جدید شما با تکرار ان برابر نیست"
                        }
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
