package dev.lackluster.hyperx.compose.preference

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import dev.lackluster.hyperx.compose.R
import dev.lackluster.hyperx.compose.activity.SafeSP
import dev.lackluster.hyperx.compose.base.ImageIcon
import dev.lackluster.hyperx.compose.base.DrawableResIcon
import top.yukonga.miuix.kmp.extra.SpinnerEntry
import top.yukonga.miuix.kmp.extra.SpinnerMode
import top.yukonga.miuix.kmp.extra.SuperSpinner

@Composable
fun DropDownPreference(
    icon: ImageIcon? = null,
    title: String,
    summary: String? = null,
    entries: List<DropDownEntry>,
    key: String? = null,
    defValue: Int = 0,
    mode: DropDownMode = DropDownMode.AlwaysOnRight,
    showValue: Boolean = true,
    onSelectedIndexChange: ((Int) -> Unit)? = null,
) {
    val wrappedEntries = entries.map { entry ->
        SpinnerEntry(
            icon = { imageModifier ->
                entry.iconVector?.let {
                    Image(
                        modifier = imageModifier,
                        imageVector = it,
                        contentDescription = null
                    )
                } ?: entry.iconRes?.let {
                    Image(
                        modifier = imageModifier,
                        painter = painterResource(it),
                        contentDescription = null
                    )
                } ?: entry.iconBitmap?.let {
                    Image(
                        modifier = imageModifier,
                        bitmap = it,
                        contentDescription = null
                    )
                }
            },
            title = entry.title,
            summary = entry.summary,
        )
    }
    val spValue = remember { mutableIntStateOf(
        (key?.let { SafeSP.getInt(it, defValue) } ?: defValue).coerceIn(
            minimumValue = 0,
            maximumValue = entries.size - 1
        )
    ) }
    val doOnSelectedIndexChange = { newValue: Int ->
        spValue.intValue = newValue
        key?.let { SafeSP.putAny(it, newValue) }
        onSelectedIndexChange?.let { it1 -> it1(newValue) }
    }
    if (mode == DropDownMode.Dialog) {
        SuperSpinner(
            title = title,
            summary = summary,
            items = wrappedEntries,
            selectedIndex = spValue.intValue,
            dialogButtonString = stringResource(R.string.button_cancel),
            leftAction = {
                icon?.let {
                    DrawableResIcon(it)
                }
            },
            showValue = showValue
        ) { newValue ->
            doOnSelectedIndexChange(newValue)
        }
    } else {
        val spinnerMode = if (mode == DropDownMode.AlwaysOnRight) SpinnerMode.AlwaysOnRight else SpinnerMode.Normal
        SuperSpinner(
            title = title,
            summary = summary,
            items = wrappedEntries,
            selectedIndex = spValue.intValue,
            mode = spinnerMode,
            leftAction = {
                icon?.let {
                    DrawableResIcon(it)
                }
            },
            showValue = showValue
        ) { newValue ->
            doOnSelectedIndexChange(newValue)
        }
    }
}

data class DropDownEntry(
    val title: String? = null,
    val summary: String? = null,
    val iconRes: Int? = null,
    val iconBitmap: ImageBitmap? = null,
    val iconVector: ImageVector? = null,
)

enum class DropDownMode {
    Normal,
    AlwaysOnRight,
    Dialog
}