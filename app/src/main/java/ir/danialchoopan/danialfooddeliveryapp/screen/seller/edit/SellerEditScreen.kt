package ir.danialchoopan.danialfooddeliveryapp.screen.seller.edit

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ir.danialchoopan.danialfooddeliveryapp.data.user.UserAuthRequestGroup
import ir.danialchoopan.danialfooddeliveryapp.screen.functions.*
import ir.danialchoopan.danialfooddeliveryapp.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditSellerScreen(navController: NavController) {
    val m_context = androidx.compose.ui.platform.LocalContext.current

    var selectedCity by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }


    val userAuthRequestGroup= UserAuthRequestGroup(m_context)
    val old_city_name=userAuthRequestGroup.userSharedPreferences.getString("seller_city","")

    val cities = listOf(
        "مشهد", "سبزوار", "نیشابور", "تربت حیدریه", "کاشمر", "خواف", "فریمان", "چناران", "کلات"
    )
    selectedCity=old_city_name.toString()

    Scaffold(
        modifier = Modifier.background(color = BackgroundColor),
        containerColor = BackgroundColor,
        topBar = {
            GradientTopBar(
                title = "ویرایش اطلاعات رستوران",
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

                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        value = selectedCity,
                        onValueChange = {},
                        label = { Text("شهر") },
                        readOnly = true,
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        trailingIcon = {
                            Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PrimaryColor,
                            unfocusedBorderColor = DividerColor,
                            focusedContainerColor = CardSurface,
                            unfocusedContainerColor = CardSurface
                        )
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

                GradientButton(
                    text = "ذخیره تغییرات",
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
                )

                Spacer(modifier = Modifier.height(16.dp))

                GradientButton(
                    text = "تغییر بنر رستوران",
                    onClick = {
                        navController.navigate("seller/edit/banner")
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                GradientButton(
                    text = "تغییر رمزعبور",
                    onClick = {
                        navController.navigate("seller/edit/password")
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
