package ir.danialchoopan.danialfooddeliveryapp.screen

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import ir.danialchoopan.danialfooddeliveryapp.data.check_internet.InternetCheck
import ir.danialchoopan.danialfooddeliveryapp.screen.functions.GradientButton
import ir.danialchoopan.danialfooddeliveryapp.ui.theme.GradientSplash
import ir.danialchoopan.danialfooddeliveryapp.ui.theme.PrimaryColor
import ir.danialchoopan.danialfooddeliveryapp.ui.theme.TextPrimary
import ir.danialchoopan.danialfooddeliveryapp.ui.theme.vazirFontFamily

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SplashScreen(navController: NavController) {
    Scaffold(
        topBar = {},
        modifier = Modifier.fillMaxSize(),
        content = {
            val m_context = LocalContext.current
            val internetCheck = InternetCheck(m_context)

            val userSharedPreferences =
                m_context.getSharedPreferences("app_data", Context.MODE_PRIVATE)

            val login_level by remember {
                mutableStateOf(
                    userSharedPreferences.getString("has_login", "none")
                )
            }

            var failedStatus by remember {
                mutableStateOf(false)
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .background(GradientSplash)
                    .padding(20.dp)
            ) {
                Text(
                    text = "\uD83C\uDF5C",
                    fontSize = 72.sp
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "دانیال فود",
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = vazirFontFamily,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    style = TextStyle(
                        shadow = Shadow(
                            color = Color.Black.copy(alpha = 0.3f),
                            offset = androidx.compose.ui.geometry.Offset(4f, 4f),
                            blurRadius = 8f
                        )
                    )
                )
                Spacer(modifier = Modifier.height(120.dp))
                if (failedStatus) {
                    Text(
                        text = "به نظر می آید مشکلی پیش آمده است !",
                        color = Color.White,
                        modifier = Modifier.padding(10.dp),
                        fontFamily = vazirFontFamily
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    GradientButton(
                        text = "تلاش دوباره",
                        onClick = {
                            failedStatus = false
                            internetCheck.checkConnection({
                                navController.navigate("home") {
                                    popUpTo(0)
                                }
                                failedStatus = false
                            },
                                {
                                    failedStatus = true
                                }
                            )
                        },
                        modifier = Modifier.padding(horizontal = 32.dp)
                    )
                } else {
                    Text(
                        text = "درحال برسی ارتباط شما با اینترنت ",
                        color = Color.White,
                        fontFamily = vazirFontFamily
                    )
                    CircularProgressIndicator(
                        color = PrimaryColor,
                        modifier = Modifier.padding(10.dp)
                    )
                }
                when (login_level) {
                    "none" -> navController.navigate("user_login") {
                        popUpTo(0)
                    }
                    "user" -> navController.navigate("home_user") {
                        popUpTo(0)
                    }
                    "seller" -> navController.navigate("dashboard_seller") {
                        popUpTo(0)
                    }
                }

                internetCheck.checkConnection({
                    when (login_level) {
                        "none" -> navController.navigate("user_login") {
                            popUpTo(0)
                        }
                        "user" -> navController.navigate("home_user") {
                            popUpTo(0)
                        }
                        "seller" -> navController.navigate("dashboard_seller") {
                            popUpTo(0)
                        }
                    }
                },
                    {
                        failedStatus = true
                    }
                )
            }
        }
    )
}
