package dev.lackluster.hyperx.compose.base

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableFloatState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import dev.chrisbanes.haze.HazeStyle
import dev.chrisbanes.haze.HazeTint
import dev.lackluster.hyperx.compose.icon.Back
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.IconButton
import top.yukonga.miuix.kmp.basic.LazyColumn
import top.yukonga.miuix.kmp.basic.MiuixScrollBehavior
import top.yukonga.miuix.kmp.basic.TopAppBar
import top.yukonga.miuix.kmp.basic.rememberTopAppBarState
import top.yukonga.miuix.kmp.icon.MiuixIcons
import top.yukonga.miuix.kmp.theme.MiuixTheme
import top.yukonga.miuix.kmp.utils.getWindowSize

@Composable
fun BasePage(
    navController: NavController,
    adjustPadding: PaddingValues,
    title: String,
    blurEnabled: MutableState<Boolean> = mutableStateOf(true),
    blurTintAlphaLight: MutableFloatState = mutableFloatStateOf(0.6f),
    blurTintAlphaDark: MutableFloatState = mutableFloatStateOf(0.5f),
    navigationIcon: @Composable () -> Unit = {
        IconButton(
            modifier = Modifier.padding(start = 21.dp).size(40.dp),
            onClick = {
                navController.popBackStack()
            }
        ) {
            Icon(
                modifier = Modifier.size(26.dp),
                imageVector = MiuixIcons.Back,
                contentDescription = "Back",
                tint = MiuixTheme.colorScheme.onSurfaceSecondary
            )
        }
    },
    actions: @Composable RowScope.() -> Unit = {},
    content: LazyListScope.() -> Unit
) {
    val topAppBarBackground = MiuixTheme.colorScheme.background
    val scrollBehavior = MiuixScrollBehavior(rememberTopAppBarState())
    val listState = rememberLazyListState()
    val topBarBlurState by remember {
        derivedStateOf {
            blurEnabled.value &&
                    scrollBehavior.state.collapsedFraction >= 1.0f &&
                    (listState.isScrollInProgress || listState.firstVisibleItemIndex > 0 || listState.firstVisibleItemScrollOffset > 12)
        }
    }
    val topBarBlurTintAlpha = remember { mutableFloatStateOf(
        if (topAppBarBackground.luminance() >= 0.5f) blurTintAlphaLight.floatValue
        else blurTintAlphaDark.floatValue
    ) }
    HazeScaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { contentPadding ->
            TopAppBar(
                color = topAppBarBackground.copy(
                    if (topBarBlurState) 0f else 1f
                ),
                title = title,
                scrollBehavior = scrollBehavior,
                navigationIcon = navigationIcon,
                actions = actions,
                horizontalPadding = 28.dp + contentPadding.calculateLeftPadding(LocalLayoutDirection.current)
            )
        },
        blurTopBar = blurEnabled.value,
        hazeStyle = HazeStyle(
            blurRadius = 66.dp,
            backgroundColor = topAppBarBackground,
            tint = HazeTint(
                topAppBarBackground.copy(alpha = topBarBlurTintAlpha.floatValue),
            )
        ),
        adjustPadding = adjustPadding,
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .height(getWindowSize().height.dp)
                .background(MiuixTheme.colorScheme.background)
                .windowInsetsPadding(WindowInsets.displayCutout.only(WindowInsetsSides.Horizontal))
                .windowInsetsPadding(WindowInsets.navigationBars.only(WindowInsetsSides.Horizontal)),
            state = listState,
            contentPadding = paddingValues,
            topAppBarScrollBehavior = scrollBehavior,
            content = content
        )
    }
}