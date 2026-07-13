package ir.danialchoopan.danialfooddeliveryapp.screen.seller.edit

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ir.danialchoopan.danialfooddeliveryapp.data.user.UserAuthRequestGroup
import ir.danialchoopan.danialfooddeliveryapp.screen.functions.GradientButton
import ir.danialchoopan.danialfooddeliveryapp.screen.functions.GradientTopBar
import ir.danialchoopan.danialfooddeliveryapp.ui.theme.*


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditSellerPasswordScreen(navController: NavController) {
    val m_context = LocalContext.current

    var old_password by remember { mutableStateOf("") }
    var new_password by remember { mutableStateOf("") }
    var new_confirm_password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    Scaffold(
        modifier = Modifier.background(color = BackgroundColor),
        containerColor = BackgroundColor,
        topBar = {
            GradientTopBar(
                title = "ویرایش رمزعبور رستوران",
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "بازگشت",
                            tint = MaterialTheme.colorScheme.onPrimary
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
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PrimaryColor,
                        unfocusedBorderColor = DividerColor,
                        focusedContainerColor = CardSurface,
                        unfocusedContainerColor = CardSurface
                    )
                )


                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = new_password,
                    onValueChange = { new_password = it },
                    label = { Text("رمز عبور جدید", fontFamily = vazirFontFamily) },
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    maxLines = 1,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PrimaryColor,
                        unfocusedBorderColor = DividerColor,
                        focusedContainerColor = CardSurface,
                        unfocusedContainerColor = CardSurface
                    )
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = new_confirm_password,
                    onValueChange = { new_confirm_password = it },
                    label = { Text("تکرار رمز عبور جدید", fontFamily = vazirFontFamily) },
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    maxLines = 1,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PrimaryColor,
                        unfocusedBorderColor = DividerColor,
                        focusedContainerColor = CardSurface,
                        unfocusedContainerColor = CardSurface
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                GradientButton(
                    text = "تغییر رمزعبور",
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
                )

                if (errorMessage.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(errorMessage, color = ErrorColor)
                }
            }
        }
    )
}
