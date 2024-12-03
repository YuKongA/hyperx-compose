package dev.lackluster.hyperx.compose.preference

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import dev.lackluster.hyperx.compose.base.ImageIcon
import dev.lackluster.hyperx.compose.base.DrawableResIcon
import top.yukonga.miuix.kmp.extra.SuperArrow

@Composable
fun TextPreference(
    icon: ImageIcon? = null,
    title: String,
    summary: String? = null,
    value: String? = null,
    onClick: (() -> Unit)? = null,
) {
    SuperArrow(
        title = title,
        summary = summary,
        leftAction = {
            icon?.let {
                DrawableResIcon(it)
            }
        },
        rightText = value,
        insideMargin = PaddingValues((icon?.getHorizontalPadding() ?: 16.dp), 16.dp, 16.dp, 16.dp),
        onClick = onClick
    )
}