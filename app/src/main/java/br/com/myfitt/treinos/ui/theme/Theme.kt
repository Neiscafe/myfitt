package br.com.myfitt.treinos.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun MyFittTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = darkColorScheme(
            Color.White,
            onPrimary = Color(0xff2f3131),
            primaryContainer = Color(0xffe2e2e2),
            onPrimaryContainer = Color(0xff636565),
            secondary = Color(0xffc8c6c6),
            onSecondary = Color(0xff303030),
            secondaryContainer = Color(0xff494949),
            onSecondaryContainer = Color(0xffb9b8b8),
            tertiary = Color.White,
            onTertiary = Color(0xff2f3131),
            tertiaryContainer = Color(0xffe2e2e2),
            onTertiaryContainer = Color(0xff636565),
            surface = Color(0xff141313),
            surfaceDim = Color(0xff141313),
            surfaceBright = Color(0xff3a3939),
            surfaceContainerLowest = Color(0xff0e0e0e),
            surfaceContainerLow = Color(0xff1c1b1b),
            surfaceContainer = Color(0xff201f1f),
            surfaceContainerHigh = Color(0xff2a2a2a),
            surfaceContainerHighest = Color(0xff353434),
            onSurface = Color(0xffe5e2e1),
            onSurfaceVariant = Color(0xffc4c7c8),
            outline = Color(0xff8e9192),
            outlineVariant = Color(0xff444748),
            inverseSurface = Color(0xffe5e2e1),
            inverseOnSurface = Color(0xff313030),
            inversePrimary = Color(0xff5d5f5f),
            scrim = Color(0xff000000),
        ), content = content
    )
}