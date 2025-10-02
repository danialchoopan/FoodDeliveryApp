package ir.nimaali.nimafooddeliveryapp.screen

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import ir.nimaali.nimafooddeliveryapp.data.check_internet.InternetCheck
import ir.nimaali.nimafooddeliveryapp.ui.theme.PrimaryColor

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SplashScreen(navController: NavController) {
    Scaffold(
        topBar = {

        },
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
                    .background(PrimaryColor)
                    .padding(20.dp)
            ) {
                Text(text = "نیما فود", fontSize = 42.sp, color = Color.White)
                Spacer(modifier = Modifier.height(150.dp))
                if (failedStatus) {
                    Text(
                        text = "به نظر می آید مشکلی پیش آمده است !",
                        color = Color.White,
                        modifier = Modifier.padding(10.dp)
                    )
                    Column(
                        modifier = Modifier.clickable {
                            failedStatus = false
                            internetCheck.checkConnection({
                                navController.navigate("home") {
                                    popUpTo(0)
                                }
                                failedStatus = false
                            },
                                //failed
                                {
                                    failedStatus = true
                                }
                            )

                        }
                    ) {

                        Text(text = "تلاش دوباره", color = Color.White)
//                        Image(
//                            painter = painterResource(id = R.drawable.),
//                            contentDescription = "",
//                            modifier = Modifier.size(70.dp)
//                        )
                    }

                } else {
                    Text(text = "درحال برسی ارتباط شما با اینترنت ", color = Color.White)
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.padding(10.dp)
                    )
                }
                when (login_level) {
                    "none" -> navController.navigate("user_login"){
                        popUpTo(0)
                    }
                    "user" -> navController.navigate("home_user"){
                        popUpTo(0)
                    }
                    "seller" -> navController.navigate("dashboard_seller"){
                        popUpTo(0)
                    }
                }

                internetCheck.checkConnection({
                    when (login_level) {
                        "none" -> navController.navigate("user_login"){
                            popUpTo(0)
                        }
                        "user" -> navController.navigate("home_user"){
                            popUpTo(0)
                        }
                        "seller" -> navController.navigate("dashboard_seller"){
                            popUpTo(0)
                        }
                    }
                },
                    //failed
                    {
                        failedStatus = true
                    }
                )
            }
        }
    )
}