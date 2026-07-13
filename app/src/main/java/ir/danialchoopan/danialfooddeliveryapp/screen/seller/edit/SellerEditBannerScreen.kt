package ir.danialchoopan.danialfooddeliveryapp.screen.seller.edit

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import ir.danialchoopan.danialfooddeliveryapp.data.RequestEndPoints
import ir.danialchoopan.danialfooddeliveryapp.data.user.UserAuthRequestGroup
import ir.danialchoopan.danialfooddeliveryapp.screen.functions.*
import ir.danialchoopan.danialfooddeliveryapp.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)
@Composable
fun SellerEditBannerScreen(
    navController: NavHostController,
) {
    val context = LocalContext.current

    var loading by remember {
        mutableStateOf(true)
    }
    val sellerUserAuthRequestGroup = UserAuthRequestGroup(context)


    var img_banner_old_url = remember { mutableStateOf("") }
    val imageUri = remember { mutableStateOf<Uri?>(null) }

    sellerUserAuthRequestGroup.getSellerBanner {
        img_banner_old_url.value = it
        loading = false
    }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            imageUri.value = uri
        }
    )

    fun submitForm() {

        val selectedImageUri = imageUri.value

        sellerUserAuthRequestGroup.addBannerSeller(
            selectedImageUri
        )

        Toast.makeText(context, "بننر با موفقیت ویرایش شد", Toast.LENGTH_SHORT).show()
        navController.popBackStack()
    }

    Scaffold(
        containerColor = BackgroundColor,
        topBar = {
            GradientTopBar(
                title = "ویرایش بنر",
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
                Spacer(modifier = Modifier.height(16.dp))
                GlideImage(
                    model = RequestEndPoints.rootDomain + "/" + img_banner_old_url.value,
                    contentDescription = "",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(16.dp)),
                    contentScale = ContentScale.Crop
                )
                imageUri.value?.let {
                    Image(
                        painter = rememberImagePainter(it),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(230.dp)
                            .clip(RoundedCornerShape(16.dp))
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
