package dev.lackluster.hyperx.compose.preference

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import dev.lackluster.hyperx.compose.activity.SafeSP
import dev.lackluster.hyperx.compose.base.ImageIcon
import dev.lackluster.hyperx.compose.base.DrawableResIcon
import top.yukonga.miuix.kmp.extra.SuperSwitch

@Composable
fun SwitchPreference(
    icon: ImageIcon? = null,
    title: String,
    summary: String? = null,
    key: String? = null,
    defValue: Boolean = false,
    enabled: Boolean = true,
    onCheckedChange: ((Boolean) -> Unit)? = null,
) {
    val spValue = remember { mutableStateOf(
        key?.let { SafeSP.getBoolean(it, defValue) } ?: defValue
    ) }
    SuperSwitch(
        title = title,
        summary = summary,
        leftAction = {
            icon?.let {
                DrawableResIcon(it)
            }
        },
        checked = spValue.value,
        onCheckedChange = { newValue ->
            spValue.value = newValue
            key?.let { SafeSP.putAny(it, newValue) }
            onCheckedChange?.let { it1 -> it1(newValue) }
        },
        insideMargin = PaddingValues((icon?.getHorizontalPadding() ?: 16.dp), 16.dp, 16.dp, 16.dp),
        enabled = enabled
    )
}

@Composable
fun SwitchPreference(
    icon: ImageIcon? = null,
    title: String,
    summary: String? = null,
    key: String? = null,
    defValue: Boolean = false,
    enabled: Boolean = true,
    checked: MutableState<Boolean>
) {
    checked.value = key?.let { SafeSP.getBoolean(it, defValue) } ?: defValue
    SuperSwitch(
        title = title,
        summary = summary,
        leftAction = {
            icon?.let {
                DrawableResIcon(it)
            }
        },
        checked = checked.value,
        onCheckedChange = { newValue ->
            key?.let { SafeSP.putAny(it, newValue) }
            checked.value = newValue
        },
        insideMargin = PaddingValues((icon?.getHorizontalPadding() ?: 16.dp), 16.dp, 16.dp, 16.dp),
        enabled = enabled
    )
}