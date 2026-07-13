package ir.danialchoopan.danialfooddeliveryapp.screen.seller.food

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import ir.danialchoopan.danialfooddeliveryapp.data.RequestEndPoints
import ir.danialchoopan.danialfooddeliveryapp.data.seller.SellerFoodRequestGroup
import ir.danialchoopan.danialfooddeliveryapp.screen.functions.*
import ir.danialchoopan.danialfooddeliveryapp.ui.theme.*
import androidx.compose.ui.draw.clip

@OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)
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
    var img_food_old_url = remember { mutableStateOf("") }
    val imageUri = remember { mutableStateOf<Uri?>(null) }

    sellerFoodRequestGroup.getSellerFoodById(foodId) { success, food_item ->
        loading = false

        name.value = food_item.foods.name
        description.value = food_item.foods.description
        price.value = food_item.foods.price.toString()
        img_food_old_url.value = food_item.foods.photo
    }
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
        containerColor = BackgroundColor,
        topBar = {
            GradientTopBar(
                title = "ویرایش غذا",
                actions = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = "برگشت",
                            modifier = Modifier.size(42.dp),
                            tint = MaterialTheme.colorScheme.onPrimary
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
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PrimaryColor,
                        unfocusedBorderColor = DividerColor,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent
                    )
                )
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = description.value,
                    onValueChange = { description.value = it },
                    label = { Text("توضیحات غذا", fontFamily = vazirFontFamily) },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 4,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PrimaryColor,
                        unfocusedBorderColor = DividerColor,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent
                    )
                )
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = price.value,
                    onValueChange = { price.value = it },
                    label = { Text("قیمت غذا", fontFamily = vazirFontFamily) },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PrimaryColor,
                        unfocusedBorderColor = DividerColor,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent
                    )
                )
                Spacer(modifier = Modifier.height(16.dp))
                GlideImage(
                    model = RequestEndPoints.rootDomain + "/" + img_food_old_url.value,
                    contentDescription = "",
                    modifier = Modifier
                        .size(128.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
                imageUri.value?.let {
                    Image(
                        painter = rememberImagePainter(it),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(230.dp)
                    )
                } ?: GradientButton(
                    text = "انتخاب تصویر جدید",
                    onClick = { launcher.launch("image/*") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))

                GradientButton(
                    text = "ذخیره تغییرات",
                    onClick = { submitForm() },
                    modifier = Modifier.fillMaxWidth()
                )

            }

        }
    }
}
