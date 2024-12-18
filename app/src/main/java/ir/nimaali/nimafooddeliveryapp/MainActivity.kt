package ir.nimaali.nimafooddeliveryapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ir.nimaali.nimafooddeliveryapp.screen.SplashScreen
import ir.nimaali.nimafooddeliveryapp.screen.auth.SellerLoginScreen
import ir.nimaali.nimafooddeliveryapp.screen.auth.SellerRegisterScreen
import ir.nimaali.nimafooddeliveryapp.screen.auth.UserLoginScreen
import ir.nimaali.nimafooddeliveryapp.screen.auth.UserRegisterScreen
import ir.nimaali.nimafooddeliveryapp.screen.seller.SellerDashboardScreen
import ir.nimaali.nimafooddeliveryapp.screen.seller.food.SellerAddFoodScreen
import ir.nimaali.nimafooddeliveryapp.screen.seller.food.SellerEditFoodScreen
import ir.nimaali.nimafooddeliveryapp.screen.seller.food.SellerFoodDetailsScreen
import ir.nimaali.nimafooddeliveryapp.screen.seller.food.SellerFoodListScreen
import ir.nimaali.nimafooddeliveryapp.screen.seller.order.SellerMonthlyIncomeScreen
import ir.nimaali.nimafooddeliveryapp.screen.seller.order.SellerOrderDetailScreen
import ir.nimaali.nimafooddeliveryapp.screen.seller.order.SellerOrderHistoryScreen
import ir.nimaali.nimafooddeliveryapp.screen.user.edit.EditUserScreen
import ir.nimaali.nimafooddeliveryapp.screen.user.UserHomeScreen
import ir.nimaali.nimafooddeliveryapp.screen.user.edit.EditUserPasswordScreen
import ir.nimaali.nimafooddeliveryapp.ui.theme.NimaFoodDeliveryAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            //nav controller
            val navController= rememberNavController()

            NimaFoodDeliveryAppTheme {
                RightToLeftLayout {
                    NavHost(navController=navController, startDestination = "splashScreen"){
                        composable("splashScreen") {
                            SplashScreen(navController)
                        }

                        //auth user and seller
                        composable("user_login") {
                            UserLoginScreen(navController)
                        }

                        composable("user_register") {
                            UserRegisterScreen(navController)
                        }

                        composable("seller_login") {
                            SellerLoginScreen(navController)
                        }

                        composable("seller_register") {
                            SellerRegisterScreen(navController)
                        }

                        //user
                        composable("home_user") {
                            UserHomeScreen(navController)
                        }
                        composable("home/user/edit") {
                            EditUserScreen(navController)
                        }
                        composable("home/user/edit/password") {
                            EditUserPasswordScreen(navController)
                        }

                        //seller
                        composable("dashboard_seller") {
                            SellerDashboardScreen(navController)
                        }

                        //seller food
                        composable("seller/list/food") {
                            SellerFoodListScreen(navController)
                        }
                        composable("seller/add/food") {
                            SellerAddFoodScreen(navController)
                        }

                        composable("seller/edit/food") {
                            SellerEditFoodScreen(navController)
                        }

                        composable("seller/food/detail") {
                            SellerFoodDetailsScreen(navController)
                        }


                        //order
                        composable("seller/order/history") {
                            SellerOrderHistoryScreen(navController)
                        }
                        composable("seller/order/detail") {
                            SellerOrderDetailScreen(navController,false)
                        }
                        composable("seller/payment") {
                            SellerMonthlyIncomeScreen(navController)
                        }

                    }
                }
            }
        }
    }
}

@Composable
fun RightToLeftLayout(content: @Composable () -> Unit) {
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        content()
    }
}