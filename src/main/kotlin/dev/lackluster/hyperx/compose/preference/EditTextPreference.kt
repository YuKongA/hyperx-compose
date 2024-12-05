package dev.lackluster.hyperx.compose.preference

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import dev.lackluster.hyperx.compose.R
import dev.lackluster.hyperx.compose.activity.SafeSP
import dev.lackluster.hyperx.compose.base.ImageIcon
import dev.lackluster.hyperx.compose.base.DrawableResIcon
import top.yukonga.miuix.kmp.basic.ButtonDefaults
import top.yukonga.miuix.kmp.basic.TextButton
import top.yukonga.miuix.kmp.basic.TextField
import top.yukonga.miuix.kmp.extra.SuperArrow
import top.yukonga.miuix.kmp.extra.SuperDialog
import top.yukonga.miuix.kmp.utils.MiuixPopupUtil.Companion.dismissDialog

@Composable
fun EditTextPreference(
    icon: ImageIcon? = null,
    title: String,
    summary: String? = null,
    key: String? = null,
    defValue: Any = "",
    dataType: EditTextDataType,
    dialogMessage: String? = null,
    isValueValid: ((value: Any) -> Boolean)? = null,
    showValue: Boolean = true,
    onValueChange: ((String, Any) -> Unit)? = null,
) {
    val dialogVisibility = remember { mutableStateOf(false) }
    val spValue = remember { mutableStateOf(
        key?.let {
            when (dataType) {
                EditTextDataType.BOOLEAN -> SafeSP.getBoolean(key, defValue as? Boolean ?: false)
                EditTextDataType.INT -> SafeSP.getInt(key, defValue as? Int ?: 0)
                EditTextDataType.FLOAT -> SafeSP.getFloat(key, defValue as? Float ?: 0.0f)
                EditTextDataType.LONG -> SafeSP.getLong(key, defValue as? Long ?: 0L)
                EditTextDataType.STRING -> SafeSP.getString(key, defValue as? String ?: "")
            }
        } ?: defValue
    ) }
    val stringValue = remember { derivedStateOf { spValue.value.toString() } }
    val doOnInputConfirm: (String) -> Unit = { newString: String ->
        val oldValue = spValue.value
        val newValue = when (dataType) {
            EditTextDataType.BOOLEAN -> newString.toBooleanStrictOrNull()
            EditTextDataType.INT -> newString.toIntOrNull()
            EditTextDataType.FLOAT -> newString.toFloatOrNull()
            EditTextDataType.LONG -> newString.toLongOrNull()
            EditTextDataType.STRING -> newString
        }
        if (newValue != null && isValueValid?.invoke(newValue) != false && oldValue != newValue) {
            spValue.value = newValue
            key?.let { SafeSP.putAny(it, newValue) }
            onValueChange?.let { it(newString, newValue) }
        }
    }

    SuperArrow(
        title = title,
        summary = summary,
        leftAction = {
            icon?.let {
                DrawableResIcon(it)
            }
        },
        rightText = stringValue.value.takeIf { showValue },
        insideMargin = PaddingValues((icon?.getHorizontalPadding() ?: 16.dp), 16.dp, 16.dp, 16.dp),
        onClick = {
            dialogVisibility.value = true
        }
    )
    EditTextDialog(
        visibility = dialogVisibility,
        title = title,
        message = dialogMessage,
        value = stringValue.value,
        onInputConfirm = { newString ->
            doOnInputConfirm(newString)
        }
    )
}

@Composable
fun EditTextDialog(
    visibility: MutableState<Boolean>,
    title: String?,
    message: String? = null,
    value: String = "",
    onInputConfirm: ((value: String) -> Unit)? = null
) {
    val textState = remember { mutableStateOf(
        TextFieldValue(text = value, selection = TextRange(value.length))
    ) }
    val keyboard = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }
    SuperDialog(
        title = title,
        summary = message,
        show = visibility,
        onDismissRequest = {
            if (visibility.value) {
                keyboard?.hide()
                dismissDialog(visibility)
            }
        }
    ) {
        LaunchedEffect(visibility.value) {
            if (visibility.value) {
                textState.value = TextFieldValue(text = value, selection = TextRange(value.length))
                focusRequester.requestFocus()
            }
        }
        TextField(
            modifier = Modifier.padding(bottom = 12.dp).focusRequester(focusRequester),
            value = textState.value,
            maxLines = 1,
            onValueChange = { textState.value = it }
        )
        Row(
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextButton(
                modifier = Modifier.weight(1f),
                text = stringResource(R.string.button_cancel),
                minHeight = 50.dp,
                onClick = {
                    keyboard?.hide()
                    dismissDialog(visibility)
                }
            )
            Spacer(Modifier.width(12.dp))
            TextButton(
                modifier = Modifier.weight(1f),
                text = stringResource(R.string.button_ok),
                colors = ButtonDefaults.textButtonColorsPrimary(),
                minHeight = 50.dp,
                onClick = {
                    keyboard?.hide()
                    dismissDialog(visibility)
                    onInputConfirm?.let {
                        it(textState.value.text)
                    }
                }
            )
        }
    }
}

enum class EditTextDataType {
    BOOLEAN,
    INT,
    FLOAT,
    LONG,
    STRING,
}
