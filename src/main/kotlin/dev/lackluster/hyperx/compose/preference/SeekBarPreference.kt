package dev.lackluster.hyperx.compose.preference

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dev.lackluster.hyperx.compose.activity.SafeSP
import dev.lackluster.hyperx.compose.base.ImageIcon
import dev.lackluster.hyperx.compose.base.DrawableResIcon
import top.yukonga.miuix.kmp.basic.BasicComponent
import top.yukonga.miuix.kmp.basic.Slider
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.theme.MiuixTheme

@Composable
fun SeekBarPreference(
    icon: ImageIcon? = null,
    title: String,
    key: String? = null,
    defValue: Int = 0,
    min: Int = 0,
    max: Int = 1,
    showValue: Boolean = true,
    format: String = "%d",
    onValueChange: ((Int) -> Unit)? = null,
) {
    val spValue = remember { mutableIntStateOf(
        key?.let { SafeSP.getInt(it, defValue) } ?: defValue
    ) }

    Column {
        BasicComponent(
            modifier = Modifier,
            insideMargin = PaddingValues(16.dp, 16.dp, 16.dp, 12.dp),
            title = title,
            leftAction = {
                icon?.let {
                    DrawableResIcon(it)
                }
            },
            rightActions = {
                if (showValue) {
                    Text(
                        text = String.format(Locale.current.platformLocale, format, spValue.intValue),
                        fontSize = MiuixTheme.textStyles.body2.fontSize,
                        color = SeekBarPreferenceDefaults.rightActionColors().color(true),
                        textAlign = TextAlign.End,
                    )
                }
            }
        )
        Slider(
            modifier = Modifier.padding(16.dp, 0.dp, 16.dp, 16.dp),
            progress = spValue.intValue.toFloat(),
            minValue = min.toFloat(),
            maxValue = max.toFloat(),
            height = 28.dp,
            onProgressChange = { newValue ->
                val newInt = newValue.toInt()
                spValue.intValue = newInt
                key?.let { SafeSP.putAny(it, newInt) }
                onValueChange?.let { it1 -> it1(newInt) }
            }
        )
    }

}

object SeekBarPreferenceDefaults {

    /**
     * The default color of the value.
     */
    @Composable
    fun rightActionColors() = RightActionColors(
        color = MiuixTheme.colorScheme.onSurfaceVariantActions,
        disabledColor = MiuixTheme.colorScheme.disabledOnSecondaryVariant
    )

}


@Immutable
class RightActionColors(
    private val color: Color,
    private val disabledColor: Color
) {
    @Stable
    internal fun color(enabled: Boolean): Color = if (enabled) color else disabledColor
}