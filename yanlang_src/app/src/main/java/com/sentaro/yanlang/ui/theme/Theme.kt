package com.sentaro.yanlang.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val YanLangColorScheme = lightColorScheme(
    primary = YanAccent,
    onPrimary = YanWhite,
    primaryContainer = YanAccent,
    onPrimaryContainer = YanWhite,
    secondary = YanBlack,
    onSecondary = YanWhite,
    secondaryContainer = YanBackground,
    onSecondaryContainer = YanBlack,
    tertiary = YanBlack,
    onTertiary = YanWhite,
    tertiaryContainer = YanBackground,
    onTertiaryContainer = YanBlack,
    error = YanError,
    background = YanBackground,
    onBackground = YanBlack,
    surface = YanWhite,
    onSurface = YanBlack,
    surfaceVariant = YanBackground,
    onSurfaceVariant = YanMuted,
    outline = YanOutline,
    outlineVariant = YanOutline,
    surfaceContainerLowest = YanWhite,
    surfaceContainerLow = YanWhite,
    surfaceContainer = YanBackground,
    surfaceContainerHigh = YanBackground,
    surfaceContainerHighest = YanOutline,
)

@Composable
fun YanLangTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = YanLangColorScheme,
        typography = Typography,
        content = content,
    )
}
