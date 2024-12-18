package ir.nimaali.nimafooddeliveryapp.screen.functions


import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ir.nimaali.nimafooddeliveryapp.models.home.HomePageRestaurant


@Composable
fun LoadingProgressbar(
    Content_utail: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(15.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(modifier = Modifier.size(150.dp))
        Text(text = "در حال بارگذاری", color = Color.DarkGray, fontSize = 17.sp)
        Spacer(modifier = Modifier.height(10.dp))
        LinearProgressIndicator()
        Content_utail()
    }
}