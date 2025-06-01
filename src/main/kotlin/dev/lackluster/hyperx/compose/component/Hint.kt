package dev.lackluster.hyperx.compose.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.lackluster.hyperx.compose.R
import dev.lackluster.hyperx.compose.icon.HintClose
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.IconButton
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.icon.MiuixIcons
import top.yukonga.miuix.kmp.theme.MiuixTheme

@Composable
fun Hint(
    modifier: Modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
    text: String,
    foregroundColor: Color = colorResource(R.color.hyperx_hint_fg),
    backgroundColor: Color = colorResource(R.color.hyperx_hint_bg),
    cornerRadius: Dp = 16.dp,
    contentPadding: PaddingValues = PaddingValues(16.dp),
    closeable: Boolean = false,
    onClose: (() -> Unit)? = null
) {
    Card(
        modifier = modifier
            .fillMaxWidth(),
        color = backgroundColor,
        cornerRadius = cornerRadius,
        insideMargin = PaddingValues()
    ) {
        Row(
            modifier = Modifier
                .heightIn(min = 60.dp)
                .padding(contentPadding)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = text,
                fontSize = MiuixTheme.textStyles.subtitle.fontSize,
                fontWeight = FontWeight.Medium,
                color = foregroundColor,
                textAlign = TextAlign.Start
            )
            if (closeable) {
                IconButton(
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .size(16.dp),
                    onClick = {
                        onClose?.invoke()
                    }
                ) {
                    Icon(
                        modifier = Modifier.size(11.dp),
                        imageVector = MiuixIcons.HintClose,
                        contentDescription = "Close",
                        tint = foregroundColor
                    )
                }
            }
        }
    }
}