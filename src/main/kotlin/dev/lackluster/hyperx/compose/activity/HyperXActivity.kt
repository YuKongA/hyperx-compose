package dev.lackluster.hyperx.compose.activity

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.Keep
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import top.yukonga.miuix.kmp.utils.getRoundedCorner

@Keep
abstract class HyperXActivity : ComponentActivity() {
    companion object {
        private var cornerRadius = 0.dp
        val screenCornerRadius: MutableState<Dp> = mutableStateOf(cornerRadius)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        window.isNavigationBarContrastEnforced = false
        setContent {
            cornerRadius = getRoundedCorner()
            screenCornerRadius.value = cornerRadius
            AppContent()
        }
    }

    override fun onMultiWindowModeChanged(isInMultiWindowMode: Boolean, newConfig: Configuration) {
        super.onMultiWindowModeChanged(isInMultiWindowMode, newConfig)
        if (isInMultiWindowMode) {
            screenCornerRadius.value = 0.dp
        } else {
            screenCornerRadius.value = cornerRadius
        }
    }

    @Composable
    abstract fun AppContent()
}