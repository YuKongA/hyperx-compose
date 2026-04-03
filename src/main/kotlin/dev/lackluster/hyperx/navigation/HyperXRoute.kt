package dev.lackluster.hyperx.navigation

import android.os.Parcelable
import androidx.navigation3.runtime.NavKey
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
sealed interface HyperXRoute : NavKey, Parcelable {
    @Serializable
    @Parcelize
    data object Main : HyperXRoute

    @Serializable
    @Parcelize
    data object Empty : HyperXRoute
}