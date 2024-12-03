package dev.lackluster.hyperx.compose.preference

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.SmallTitle
import top.yukonga.miuix.kmp.theme.MiuixTheme

@Composable
fun PreferenceGroup(
    title: String? = null,
    textColor: Color = MiuixTheme.colorScheme.onBackgroundVariant,
    first: Boolean = false,
    content: @Composable ColumnScope.() -> Unit
) {
    title?.let {
        SmallTitle(
            text = it,
            modifier = if (first) Modifier else Modifier.padding(top = 6.dp),
            textColor = textColor
        )
    }
    val cardTopPadding = if (title == null) {
        if (first) 12.dp else 6.dp
    } else 0.dp
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp)
            .padding(bottom = 6.dp, top = cardTopPadding),
        content = content
    )
}