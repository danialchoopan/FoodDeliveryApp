package ir.nimaali.nimafooddeliveryapp.screen.seller.food


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.provider.CalendarContract.Colors
import android.view.MenuItem
import android.widget.Toast
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
import ir.nimaali.nimafooddeliveryapp.ui.theme.BackgroundColor
import ir.nimaali.nimafooddeliveryapp.ui.theme.PrimaryColor
import ir.nimaali.nimafooddeliveryapp.ui.theme.SurfaceColor
import ir.nimaali.nimafooddeliveryapp.ui.theme.vazirFontFamily
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SellerAddFoodScreen(navController: NavHostController) {
    val name = remember { mutableStateOf("") }
    val description = remember { mutableStateOf("") }
    val price = remember { mutableStateOf("") }
    val imageUri = remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current

    fun pickImage() {
        val intent = Intent(Intent.ACTION_PICK).apply {
            type = "image/*"
        }
        (context as Activity).startActivityForResult(intent, 100) // برای انتخاب تصویر
    }

    fun submitForm() {
        if (name.value.isEmpty() || price.value.isEmpty()) {
            Toast.makeText(context, "لطفا تمام فیلدها را پر کنید", Toast.LENGTH_SHORT).show()
            return
        }

        val foodName = name.value
        val foodDescription = description.value
        val foodPrice = price.value.toDoubleOrNull() ?: 0.0
        val imagePath = imageUri.value?.path ?: ""

        // ارسال داده‌ها به سرور
        Toast.makeText(context, "غذا با موفقیت افزوده شد", Toast.LENGTH_SHORT).show()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "افزودن غذا",
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = name.value,
                onValueChange = { name.value = it },
                label = {
                    Text(
                        "نام غذا",
                        fontFamily = vazirFontFamily
                    )
                },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = description.value,
                onValueChange = { description.value = it },
                label = {
                    Text(
                        "توضیحات غذا",
                        fontFamily = vazirFontFamily
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                maxLines = 4
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = price.value,
                onValueChange = { price.value = it },
                label = {
                    Text(
                        "قیمت غذا",
                        fontFamily = vazirFontFamily
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            Spacer(modifier = Modifier.height(16.dp))

//            imageUri.value?.let {
//                Image(
//                    painter = rememberImagePainter(it),
//                    contentDescription = null,
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .height(200.dp)
//                )
//            } ?: Button(onClick = { pickImage() }) {
//                Text("انتخاب تصویر غذا")
//            }
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = { submitForm() },
            ) {
                Text(
                    "افزودن غذا",
                    color = Color.White,
                    fontFamily = vazirFontFamily,
                    style = MaterialTheme.typography.headlineSmall
                )
            }
        }
    }
}
