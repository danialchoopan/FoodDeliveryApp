package ir.nimaali.nimafooddeliveryapp.screen.seller.food

import ir.nimaali.nimafooddeliveryapp.screen.functions.LoadingProgressbar

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.provider.CalendarContract.Colors
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
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
import coil.compose.rememberImagePainter
import ir.nimaali.nimafooddeliveryapp.data.home.HomeRestaurantOderRequestGroup
import ir.nimaali.nimafooddeliveryapp.data.seller.SellerFoodRequestGroup
import ir.nimaali.nimafooddeliveryapp.models.home.Restaurant
import ir.nimaali.nimafooddeliveryapp.models.seller.Foods
import ir.nimaali.nimafooddeliveryapp.ui.theme.BackgroundColor
import ir.nimaali.nimafooddeliveryapp.ui.theme.PrimaryColor
import ir.nimaali.nimafooddeliveryapp.ui.theme.SurfaceColor
import ir.nimaali.nimafooddeliveryapp.ui.theme.vazirFontFamily
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SellerEditFoodScreen(
    foodId: String,
    navController: NavHostController
) {
    val context = LocalContext.current

    var loading by remember {
        mutableStateOf(true)
    }
    val sellerFoodRequestGroup = SellerFoodRequestGroup(context)


    var name = remember { mutableStateOf("") }
    var description = remember { mutableStateOf("") }
    var price = remember { mutableStateOf("") }
    val imageUri = remember { mutableStateOf<Uri?>(null) }

    // : مقادیر اولیه غذا با استفاده از foodId از سرور یا پایگاه داده دریافت می‌شوند

    sellerFoodRequestGroup.getSellerFoodById(foodId) { success, food_item ->
        loading = false

        name.value = food_item.foods.name
        description.value = food_item.foods.description
        price.value = food_item.foods.price.toString()
    }
    // Image picker launcher
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            imageUri.value = uri
        }
    )

    fun submitForm() {
        if (name.value.isEmpty() || price.value.isEmpty()) {
            Toast.makeText(context, "لطفا تمام فیلدها را پر کنید", Toast.LENGTH_SHORT).show()
            return
        }

        val foodName = name.value
        val foodDescription = description.value
        val foodPrice = price.value
        val selectedImageUri = imageUri.value

        sellerFoodRequestGroup.editSellerFood(
            foodId,
            foodName,
            foodDescription,
            foodPrice,
            selectedImageUri
        )

        Toast.makeText(context, "غذا با موفقیت ویرایش شد", Toast.LENGTH_SHORT).show()
        navController.popBackStack()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "ویرایش غذا",
                        style = MaterialTheme.typography.headlineSmall,
                        fontFamily = vazirFontFamily,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        color = Color.White
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF4CAF50)),
                actions = {
                    IconButton(onClick = { navController.popBackStack() }) {
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
    ) { padding ->

        if (loading) {

            LoadingProgressbar {
            }

        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
            ) {
                OutlinedTextField(
                    value = name.value,
                    onValueChange = { name.value = it },
                    label = { Text("نام غذا", fontFamily = vazirFontFamily) },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = description.value,
                    onValueChange = { description.value = it },
                    label = { Text("توضیحات غذا", fontFamily = vazirFontFamily) },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 4
                )
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = price.value,
                    onValueChange = { price.value = it },
                    label = { Text("قیمت غذا", fontFamily = vazirFontFamily) },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Display image or prompt to select one
                imageUri.value?.let {
                    Image(
                        painter = rememberImagePainter(it),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(230.dp)
                    )
                } ?: Button(
                    onClick = { launcher.launch("image/*") },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("انتخاب تصویر جدید")
                }
                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { submitForm() }
                ) {
                    Text(
                        "ذخیره تغییرات",
                        color = Color.White,
                        fontFamily = vazirFontFamily,
                        style = MaterialTheme.typography.headlineSmall
                    )
                }

            }

        }
    }
}
