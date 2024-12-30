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
import ir.nimaali.nimafooddeliveryapp.screen.seller.edit.EditSellerPasswordScreen
import ir.nimaali.nimafooddeliveryapp.screen.seller.edit.EditSellerScreen
import ir.nimaali.nimafooddeliveryapp.screen.seller.edit.SellerEditBannerScreen
import ir.nimaali.nimafooddeliveryapp.screen.seller.food.SellerAddFoodScreen
import ir.nimaali.nimafooddeliveryapp.screen.seller.food.SellerEditFoodScreen
import ir.nimaali.nimafooddeliveryapp.screen.seller.food.SellerFoodListScreen
import ir.nimaali.nimafooddeliveryapp.screen.seller.order.SellerMonthlyIncomeScreen
import ir.nimaali.nimafooddeliveryapp.screen.seller.order.SellerOrderDetailScreen
import ir.nimaali.nimafooddeliveryapp.screen.seller.order.SellerOrderHistoryScreen
import ir.nimaali.nimafooddeliveryapp.screen.user.edit.EditUserScreen
import ir.nimaali.nimafooddeliveryapp.screen.user.UserHomeScreen
import ir.nimaali.nimafooddeliveryapp.screen.user.edit.EditUserPasswordScreen
import ir.nimaali.nimafooddeliveryapp.screen.user.food.UserDetailRestaurantScreen
import ir.nimaali.nimafooddeliveryapp.screen.user.order.UserOrderDetailScreen
import ir.nimaali.nimafooddeliveryapp.screen.user.order.UserOrdersAllScreen
import ir.nimaali.nimafooddeliveryapp.screen.user.us.AboutUsScreen
import ir.nimaali.nimafooddeliveryapp.ui.theme.NimaFoodDeliveryAppTheme
import java.text.DecimalFormat

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
                        composable("user/orders/all") {
                            UserOrdersAllScreen(navController)
                        }
                        composable("home/user/edit") {
                            EditUserScreen(navController)
                        }
                        composable("home/user/edit/password") {
                            EditUserPasswordScreen(navController)
                        }

                        //user order
                        composable("user/order/{order_id}") {navBackStackEntry->
                            val order_id=navBackStackEntry.arguments!!.getString("order_id")
                            UserOrderDetailScreen(navController,order_id.toString())
                        }
                        //home pages
                        composable("home/about/us") {
                            AboutUsScreen(navController)
                        }

                        //restaurant
                        composable("home/restaurant/{restaurant_id}") {navBackStackEntry->
                            val restaurant_id=navBackStackEntry.arguments!!.getString("restaurant_id")
                            UserDetailRestaurantScreen(navController,restaurant_id.toString())
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

                        composable("seller/edit/food/{food_id}") {navBackStackEntry->
                            val food_it=navBackStackEntry.arguments!!.getString("food_id")
                            SellerEditFoodScreen(food_it.toString(),navController)
                        }



                        //order
                        composable("seller/order/history") {
                            SellerOrderHistoryScreen(navController)
                        }
                        composable("seller/order/detail/{order_id}") {navBackStackEntry->
                            val order_id=navBackStackEntry.arguments!!.getString("order_id")
                            SellerOrderDetailScreen(navController, order_id.toString())
                        }
                        composable("seller/payment") {
                            SellerMonthlyIncomeScreen(navController)
                        }
                        composable("seller/edit") {
                            EditSellerScreen(navController)
                        }
                        composable("seller/edit/banner") {
                            SellerEditBannerScreen(navController)
                        }
                        composable("seller/edit/password") {
                            EditSellerPasswordScreen(navController)
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

fun formatPrice(input: String): String {
    return try {
        val number = input.toInt()
        if (number == 0) {
            "0"
        } else {
            val decimalFormat = DecimalFormat("###,###")
            decimalFormat.format(number)
        }
    } catch (e: NumberFormatException) {
        "Invalid input"
    }
}