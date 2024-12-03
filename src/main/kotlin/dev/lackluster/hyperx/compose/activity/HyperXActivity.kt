package dev.lackluster.hyperx.compose.activity

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.annotation.Keep
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable

@Keep
abstract class HyperXActivity : ComponentActivity() {
    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        window.isNavigationBarContrastEnforced = false
        context = this
        setContent {
            AppContent()
        }
    }

    @Composable
    abstract fun AppContent()
}