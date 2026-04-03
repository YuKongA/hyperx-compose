package dev.lackluster.hyperx.ui.layout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.add
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.dp
import dev.lackluster.hyperx.navigation.LocalNavigator
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.IconButton
import top.yukonga.miuix.kmp.basic.MiuixScrollBehavior
import top.yukonga.miuix.kmp.basic.TopAppBar
import top.yukonga.miuix.kmp.basic.rememberTopAppBarState
import top.yukonga.miuix.kmp.icon.MiuixIcons
import top.yukonga.miuix.kmp.icon.extended.Back
import top.yukonga.miuix.kmp.theme.MiuixTheme
import top.yukonga.miuix.kmp.utils.overScrollVertical
import top.yukonga.miuix.kmp.utils.scrollEndHaptic

@Composable
fun HyperXPage(
    title: String,
    modifier: Modifier = Modifier,
    contentModifier: Modifier = Modifier,
    listState: LazyListState = rememberLazyListState(),
    navigationIcon: @Composable (padding: PaddingValues) -> Unit = { padding ->
        val navigator = LocalNavigator.current
        IconButton(
            modifier = Modifier
                .padding(padding)
                .padding(start = 21.dp)
                .size(40.dp),
            onClick = { navigator.pop() }
        ) {
            Icon(
                modifier = Modifier.size(26.dp),
                imageVector = MiuixIcons.Back,
                contentDescription = "Back",
                tint = MiuixTheme.colorScheme.onSurfaceSecondary
            )
        }
    },
    actions: @Composable RowScope.(padding: PaddingValues) -> Unit = {},
    fixedHeader: (@Composable () -> Unit)? = null,
    content: LazyListScope.() -> Unit
) {
    val uiConfig = LocalHyperXLayoutConfig.current
    val pageMode = LocalPageMode.current
    val layoutPadding = LocalLayoutPadding.current // 注意我们在 Locals 里把 adjustPadding 改名了

    val scrollBehavior = MiuixScrollBehavior(rememberTopAppBarState())

    val isScrolled by remember {
        derivedStateOf {
            listState.canScrollBackward
        }
    }

    val layoutDirection = LocalLayoutDirection.current
    val systemBarInsets = WindowInsets.systemBars.add(WindowInsets.displayCutout).only(WindowInsetsSides.Horizontal).asPaddingValues()

    val navigationIconPadding = PaddingValues.Absolute(
        left = if (pageMode != PageLayoutMode.SPLIT_SECONDARY) systemBarInsets.calculateLeftPadding(layoutDirection) else 0.dp
    )
    val actionsPadding = PaddingValues.Absolute(
        right = if (pageMode != PageLayoutMode.SPLIT_PRIMARY) systemBarInsets.calculateRightPadding(layoutDirection) else 0.dp
    )

    val topBarColor = if (uiConfig.isBlurEnabled && isScrolled) {
        Color.Transparent
    } else {
        MiuixTheme.colorScheme.surface
    }

    val blurTintAlpha = if (MiuixTheme.colorScheme.surface.luminance() >= 0.5f) {
        uiConfig.lightBlurAlpha
    } else {
        uiConfig.darkBlurAlpha
    }

    HazeScaffold(
        modifier = modifier.fillMaxSize(),
        topBar = { contentPadding ->
            TopAppBar(
                color = topBarColor,
                title = title,
                scrollBehavior = scrollBehavior,
                navigationIcon = { navigationIcon(navigationIconPadding) },
                actions = { actions(this, actionsPadding) },
                defaultWindowInsetsPadding = false,
                horizontalPadding = 28.dp + contentPadding.calculateLeftPadding(layoutDirection)
            )
        },
        blurTopBar = uiConfig.isBlurEnabled,
        isTopBarScrolled = isScrolled,
        blurTintAlpha = blurTintAlpha,
        layoutPadding = layoutPadding,
        fixedHeader = { contentPadding ->
            fixedHeader?.let {
                Box(
                    modifier = Modifier
                        .background(topBarColor)
                        .padding(contentPadding)
                ) {
                    it()
                }
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = contentModifier
                .fillMaxHeight()
                .scrollEndHaptic()
                .overScrollVertical()
                .nestedScroll(scrollBehavior.nestedScrollConnection),
            state = listState,
            contentPadding = paddingValues,
            content = content,
        )
    }
}