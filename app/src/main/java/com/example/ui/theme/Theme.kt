package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
  primary = SignalBlue,
  onPrimary = Color.White,
  primaryContainer = BlueprintNavy,
  onPrimaryContainer = SignalBlueLight,
  secondary = SignalGreen,
  onSecondary = Color.White,
  secondaryContainer = Color(0xFF14532D),
  tertiary = FeedbackRed,
  background = BlueprintDark,
  surface = BlueprintSurface,
  onBackground = Color(0xFFF1F5F9),
  onSurface = Color(0xFFF1F5F9),
  surfaceVariant = Color(0xFF334155),
  onSurfaceVariant = Color(0xFFCBD5E1),
  outline = BlueprintBorderDark
)

private val LightColorScheme = lightColorScheme(
  primary = BlueprintNavy,
  onPrimary = Color.White,
  primaryContainer = SignalBlueLight,
  onPrimaryContainer = BlueprintNavy,
  secondary = SignalGreen,
  onSecondary = Color.White,
  secondaryContainer = SignalGreenLight,
  tertiary = FeedbackRed,
  background = Slate50,
  surface = Color.White,
  onBackground = TextPrimary,
  onSurface = TextPrimary,
  surfaceVariant = Slate100,
  onSurfaceVariant = TextSecondary,
  outline = BlueprintBorder
)

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = false, // Use intentional corporate blueprint theme
  content: @Composable () -> Unit
) {
  val colorScheme = when {
    dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
      val context = LocalContext.current
      if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
    }
    darkTheme -> DarkColorScheme
    else -> LightColorScheme
  }

  MaterialTheme(
    colorScheme = colorScheme,
    typography = Typography,
    content = content
  )
}
