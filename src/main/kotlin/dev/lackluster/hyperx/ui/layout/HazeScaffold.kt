package dev.lackluster.hyperx.ui.layout

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import dev.chrisbanes.haze.ExperimentalHazeApi
import dev.chrisbanes.haze.HazeInputScale
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.HazeStyle
import dev.chrisbanes.haze.HazeTint
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.hazeSource
import top.yukonga.miuix.kmp.basic.FabPosition
import top.yukonga.miuix.kmp.basic.Scaffold
import top.yukonga.miuix.kmp.theme.MiuixTheme

@OptIn(ExperimentalHazeApi::class)
@Composable
fun HazeScaffold(
    modifier: Modifier = Modifier,
    topBar: @Composable ((contentPadding: PaddingValues) -> Unit)? = null,
    bottomBar: @Composable ((contentPadding: PaddingValues) -> Unit)? = null,
    floatingActionButton: @Composable () -> Unit = {},
    floatingActionButtonPosition: FabPosition = FabPosition.End,
    snackbarHost: @Composable () -> Unit = {},
    containerColor: Color = MiuixTheme.colorScheme.surface,
    contentWindowInsets: WindowInsets = WindowInsets.systemBars.only(WindowInsetsSides.Vertical),
    blurTopBar: Boolean = false,
    isTopBarScrolled: Boolean = true,
    blurBottomBar: Boolean = false,
    isBottomBarScrolled: Boolean = true,
    blurTintAlpha: Float = 0.8f,
    hazeState: HazeState = remember { HazeState() },
    layoutPadding: PaddingValues = PaddingValues(0.dp),
    fixedBackgroundColor: Color = containerColor,
    fixedHeader: (@Composable (contentPadding: PaddingValues) -> Unit)? = null,
    content: @Composable (PaddingValues) -> Unit
) {
    val density = LocalDensity.current
    val layoutDirection = LocalLayoutDirection.current

    var fixedHeaderHeightPx by remember { mutableIntStateOf(0) }
    val fixedHeaderPadding = with(density) { fixedHeaderHeightPx.toDp() }

    val topBlurRadius = if (isTopBarScrolled) 20.dp else 0.dp
    val topTintAlpha = if (isTopBarScrolled) blurTintAlpha else 0f
    val topHazeStyle = remember(containerColor, topTintAlpha) {
        HazeStyle(
            backgroundColor = containerColor,
            tint = HazeTint(containerColor.copy(alpha = topTintAlpha))
        )
    }

    val bottomBlurRadius = if (isBottomBarScrolled) 20.dp else 0.dp
    val bottomTintAlpha = if (isBottomBarScrolled) blurTintAlpha else 0f
    val bottomHazeStyle = remember(containerColor, bottomTintAlpha) {
        HazeStyle(
            backgroundColor = containerColor,
            tint = HazeTint(containerColor.copy(alpha = bottomTintAlpha))
        )
    }

    Scaffold(
        modifier = modifier,
        topBar = @Composable {
            topBar?.let {
                if (blurTopBar) {
                    Box(
                        modifier = Modifier.hazeEffect(state = hazeState, style = topHazeStyle) {
                            blurRadius = topBlurRadius
                            inputScale = HazeInputScale.Fixed(0.35f)
                            noiseFactor = 0f
                            forceInvalidateOnPreDraw = false
                        },
                    ) {
                        it(layoutPadding)
                    }
                } else {
                    it(layoutPadding)
                }
            }
        },
        bottomBar = @Composable {
            bottomBar?.let {
                if (blurBottomBar) {
                    Box(
                        modifier = Modifier.hazeEffect(state = hazeState, style = bottomHazeStyle) {
                            blurRadius = bottomBlurRadius
                            inputScale = HazeInputScale.Fixed(0.35f)
                            noiseFactor = 0f
                            forceInvalidateOnPreDraw = false
                        },
                    ) {
                        it(layoutPadding)
                    }
                } else {
                    it(layoutPadding)
                }
            }
        },
        floatingActionButton = floatingActionButton,
        floatingActionButtonPosition = floatingActionButtonPosition,
        snackbarHost = snackbarHost,
        containerColor = containerColor,
        contentWindowInsets = contentWindowInsets,
    ) { contentPadding ->
        Box(Modifier.hazeSource(state = hazeState)) {
            content(
                PaddingValues(
                    start = contentPadding.calculateLeftPadding(layoutDirection) +
                            layoutPadding.calculateLeftPadding(layoutDirection),
                    top = contentPadding.calculateTopPadding() +
                            layoutPadding.calculateTopPadding() +
                            fixedHeaderPadding,
                    end = contentPadding.calculateRightPadding(layoutDirection) +
                            layoutPadding.calculateRightPadding(layoutDirection),
                    bottom = contentPadding.calculateBottomPadding() +
                            layoutPadding.calculateBottomPadding()
                )
            )
        }

        fixedHeader?.let { header ->
            Box(
                modifier = if (blurTopBar) {
                    Modifier.hazeEffect(state = hazeState, style = topHazeStyle) {
                        blurRadius = topBlurRadius
                        inputScale = HazeInputScale.Fixed(0.35f)
                        noiseFactor = 0f
                        forceInvalidateOnPreDraw = false
                    }
                } else {
                    Modifier.background(fixedBackgroundColor)
                }
                    .zIndex(1f)
                    .fillMaxWidth()
                    .padding(top = contentPadding.calculateTopPadding() + layoutPadding.calculateTopPadding())
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {} // 拦截点击事件，防止穿透
                    .onSizeChanged { size ->
                        if (fixedHeaderHeightPx != size.height) {
                            fixedHeaderHeightPx = size.height
                        }
                    }
            ) {
                header(
                    PaddingValues(
                        start = contentPadding.calculateStartPadding(layoutDirection) +
                                layoutPadding.calculateStartPadding(layoutDirection),
                        top = 0.dp,
                        end = contentPadding.calculateEndPadding(layoutDirection) +
                                layoutPadding.calculateEndPadding(layoutDirection),
                        bottom = 0.dp
                    )
                )
            }
        }
    }
}