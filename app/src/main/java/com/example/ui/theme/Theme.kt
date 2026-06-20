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

private val HyperDarkBase = darkColorScheme(
    primary = AccentGold,
    secondary = ThemeCardGlow,
    tertiary = AccentPink,
    background = ThemeDeepBlack,
    surface = ThemeCardDark,
    onBackground = ThemeTextWhite,
    onSurface = ThemeTextWhite
)

@Composable
fun HyperLoungeTheme(
    accentName: String = "GOLD",
    content: @Composable () -> Unit
) {
    // Determine primary accent color based on user customization
    val primaryAccent = when (accentName) {
        "PINK" -> AccentPink
        "CYAN" -> AccentCyan
        else -> AccentGold
    }

    val customDarkScheme = HyperDarkBase.copy(
        primary = primaryAccent,
        secondary = primaryAccent.copy(alpha = 0.2f),
        outline = primaryAccent
    )

    MaterialTheme(
        colorScheme = customDarkScheme,
        typography = Typography,
        content = content
    )
}

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  // Dynamic color is available on Android 12+
  dynamicColor: Boolean = true,
  content: @Composable () -> Unit,
) {
  val colorScheme =
    when {
      dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
        val context = LocalContext.current
        if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
      }

      darkTheme -> darkColorScheme(primary = Purple80, secondary = PurpleGrey80, tertiary = Pink80)
      else -> lightColorScheme(
        primary = Purple40,
        secondary = PurpleGrey40,
        tertiary = Pink40,
      )
    }

  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}

