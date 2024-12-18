package ir.nimaali.nimafooddeliveryapp.screen.seller.food


import android.content.Context
import android.content.SharedPreferences
import android.provider.CalendarContract.Colors
import android.view.MenuItem
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import ir.nimaali.nimafooddeliveryapp.ui.theme.BackgroundColor
import ir.nimaali.nimafooddeliveryapp.ui.theme.PrimaryColor
import ir.nimaali.nimafooddeliveryapp.ui.theme.SurfaceColor
import ir.nimaali.nimafooddeliveryapp.ui.theme.vazirFontFamily
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
data class Food(
    val name: String,
    val type: String,
    val price: Double
)

val foods = listOf(
    Food("همبرگر", "Fast Food", 25000.0),
    Food("پاستا", "غذای اصلی", 35000.0),
    Food("کیک شکلاتی", "دسر", 15000.0),
    Food("پیتزا", "Fast Food", 45000.0),
    Food("پاستا", "غذای اصلی", 35000.0),
    Food("کیک شکلاتی", "دسر", 15000.0),
    Food("پیتزا", "Fast Food", 45000.0),
    Food("پاستا", "غذای اصلی", 35000.0),
    Food("کیک شکلاتی", "دسر", 15000.0),
    Food("پیتزا", "Fast Food", 45000.0),
    Food("سالاد", "پیش‌غذا", 20000.0)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SellerFoodListScreen(navHostController: NavHostController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "لیست غذاها",
                        style = MaterialTheme.typography.headlineSmall,
                        fontFamily = vazirFontFamily,
                        textAlign = TextAlign.Center,
                        color = Color.White,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF4CAF50)),
                actions = {
                    IconButton(onClick = { /* Action for back */ }) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = "برگشت",
                            modifier = Modifier.size(42.dp),
                            tint = Color.White
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            items(foods) { food ->
                Card(
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        ListItem(
                            headlineContent = {
                                Text(
                                    text = food.name,
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            },
                            supportingContent = {
                                Text(
                                    text = food.type,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontFamily = vazirFontFamily
                                )
                            },
                            trailingContent = {
                                Text(
                                    text = "${food.price} تومان",
                                    color = Color(0xFF4CAF50),
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontFamily = vazirFontFamily
                                )
                            },
                            modifier = Modifier.padding(8.dp)
                        )

                        // دکمه‌های ویرایش و حذف
                        Row(
                            horizontalArrangement = Arrangement.End,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            IconButton(onClick = { /* Handle Edit */ }) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "ویرایش",
                                    tint = Color.Blue
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            IconButton(onClick = { /* Handle Delete */ }) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "حذف",
                                    tint = Color.Red
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
