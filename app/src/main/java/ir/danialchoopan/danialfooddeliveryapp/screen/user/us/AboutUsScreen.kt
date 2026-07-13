package ir.danialchoopan.danialfooddeliveryapp.screen.user.us

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import ir.danialchoopan.danialfooddeliveryapp.screen.functions.GradientTopBar
import ir.danialchoopan.danialfooddeliveryapp.screen.functions.ModernCard
import ir.danialchoopan.danialfooddeliveryapp.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutUsScreen(navController: NavController) {
    Scaffold(
        containerColor = BackgroundColor,
        topBar = {
            GradientTopBar(
                title = "درباره ما ",
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
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // App Name Card
            ModernCard(modifier = Modifier.fillMaxWidth()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(GradientCard)
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "دانیال فود",
                        fontSize = 36.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontFamily = vazirFontFamily
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // About Section
            ModernCard(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "درباره شرکت/سرویس ما",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = vazirFontFamily,
                        color = TextPrimary,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    HorizontalDivider(
                        color = DividerColor,
                        thickness = 1.dp,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    Text(
                        text = """
                            تیم ما متشکل از افرادی با تجربه در زمینه‌های مختلف است که هدفشان ارائه بهترین خدمات به شماست. 
                            ما به بهبود مداوم و ارائه راهکارهای نوین برای رفع نیازهای کاربران خود تمرکز داریم.
                            
                            برای اطلاعات بیشتر و یا هر گونه سوال می‌توانید با تیم پشتیبانی ما در ارتباط باشید.
                        """.trimIndent(),
                        fontSize = 16.sp,
                        fontFamily = vazirFontFamily,
                        lineHeight = 26.sp,
                        color = TextSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Developer Card
            ModernCard(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "توسعه‌دهنده",
                        fontSize = 14.sp,
                        fontFamily = vazirFontFamily,
                        color = TextSecondary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "دانیال چوپان",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = vazirFontFamily,
                        color = PrimaryColor
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
