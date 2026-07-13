package ir.danialchoopan.danialfooddeliveryapp.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// Primary - Warm Orange
val PrimaryColor = Color(0xFFFF6B35)
val PrimaryDark = Color(0xFFE55A25)
val PrimaryLight = Color(0xFFFF8C42)

// Secondary - Fresh Teal/Green
val SecondaryColor = Color(0xFF2EC4B6)
val SecondaryDark = Color(0xFF20A89C)
val SecondaryLight = Color(0xFF38D9A9)

// Accent
val AccentColor = Color(0xFFE63946)

// Background & Surface
val BackgroundColor = Color(0xFFFFF8F0) // Warm cream
val SurfaceColor = Color(0xFFFFFFFF)
val CardSurface = Color(0xFFFFF1E6)

// Text
val TextPrimary = Color(0xFF2D2D2D)
val TextSecondary = Color(0xFF6B7280)
val OnPrimaryColor = Color.White

// Status Colors
val SuccessColor = Color(0xFF2EC4B6)
val ErrorColor = Color(0xFFE63946)
val WarningColor = Color(0xFFFFB627)

// Dividers & Borders
val DividerColor = Color(0xFFF0E6DB)
val BorderColor = Color(0xFFE8DDD0)

// Gradient Brushes
val GradientPrimary = Brush.horizontalGradient(
    colors = listOf(PrimaryColor, PrimaryLight)
)

val GradientPrimaryVertical = Brush.verticalGradient(
    colors = listOf(PrimaryColor, PrimaryLight)
)

val GradientSecondary = Brush.horizontalGradient(
    colors = listOf(SecondaryColor, SecondaryLight)
)

val GradientSplash = Brush.linearGradient(
    colors = listOf(PrimaryColor, AccentColor, PrimaryLight)
)

val GradientCard = Brush.verticalGradient(
    colors = listOf(CardSurface, Color(0xFFFFE4CC))
)

val GradientAuth = Brush.verticalGradient(
    colors = listOf(PrimaryColor, PrimaryLight, BackgroundColor)
)

val GradientDark = Brush.verticalGradient(
    colors = listOf(Color(0xFF1A1A2E), Color(0xFF16213E))
)
