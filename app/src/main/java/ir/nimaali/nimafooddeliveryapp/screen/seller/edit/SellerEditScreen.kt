package ir.nimaali.nimafooddeliveryapp.screen.seller.edit


import android.content.Context
import android.content.SharedPreferences
import android.provider.CalendarContract.Colors
import android.view.MenuItem
import android.widget.Toast
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
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Face
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
import ir.nimaali.nimafooddeliveryapp.data.seller.SellerHomeRequestGroup
import ir.nimaali.nimafooddeliveryapp.data.user.UserAuthRequestGroup
import ir.nimaali.nimafooddeliveryapp.models.home.order.OrderListUsersAllItem
import ir.nimaali.nimafooddeliveryapp.models.seller.dash.SellerOrdersDashShowItem
import ir.nimaali.nimafooddeliveryapp.screen.functions.LoadingProgressbar
import ir.nimaali.nimafooddeliveryapp.ui.theme.BackgroundColor
import ir.nimaali.nimafooddeliveryapp.ui.theme.PrimaryColor
import ir.nimaali.nimafooddeliveryapp.ui.theme.SurfaceColor
import ir.nimaali.nimafooddeliveryapp.ui.theme.vazirFontFamily
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditSellerScreen(navController: NavController) {
    // context
    val m_context = LocalContext.current

    // input fields
    var selectedCity by remember { mutableStateOf("") } // مقدار پیش‌فرض
    var expanded by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }


    val userAuthRequestGroup= UserAuthRequestGroup(m_context)
    val old_city_name=userAuthRequestGroup.userSharedPreferences.getString("seller_city","")

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
                        "ویرایش اطلاعات رستوران",
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
                            userAuthRequestGroup.sellerEditCityName(selectedCity){success ->
                                if(success){
                                    Toast.makeText(m_context,"شهر شما با موفقیت تغییر کرد",Toast.LENGTH_SHORT).show()
                                    navController.navigate("dashboard_seller") {
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


                //تغییر بنرفروشگاه
                Button(
                    onClick = {
                        navController.navigate("seller/edit/banner")
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        "تغییر بنر رستوران",
                        fontFamily = vazirFontFamily,
                        style = MaterialTheme.typography.headlineSmall
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                // تغییر رمزعبور
                Button(
                    onClick = {
                        navController.navigate("seller/edit/password")
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
