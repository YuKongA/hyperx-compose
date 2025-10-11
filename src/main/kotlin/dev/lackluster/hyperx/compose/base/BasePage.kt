package dev.lackluster.hyperx.compose.base

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.add
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import androidx.navigation.NavController
import dev.chrisbanes.haze.HazeStyle
import dev.chrisbanes.haze.HazeTint
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.IconButton
import top.yukonga.miuix.kmp.basic.MiuixScrollBehavior
import top.yukonga.miuix.kmp.basic.TopAppBar
import top.yukonga.miuix.kmp.basic.rememberTopAppBarState
import top.yukonga.miuix.kmp.icon.MiuixIcons
import top.yukonga.miuix.kmp.icon.icons.useful.Back
import top.yukonga.miuix.kmp.theme.MiuixTheme
import top.yukonga.miuix.kmp.utils.getWindowSize
import top.yukonga.miuix.kmp.utils.overScrollVertical
import top.yukonga.miuix.kmp.utils.scrollEndHaptic

@Composable
fun BasePage(
    navController: NavController,
    adjustPadding: PaddingValues,
    title: String,
    blurEnabled: MutableState<Boolean> = mutableStateOf(true),
    mode: BasePageDefaults.Mode = BasePageDefaults.Mode.FULL,
    navigationIcon: @Composable (padding: PaddingValues) -> Unit = { padding ->
        IconButton(
            modifier = Modifier
                .padding(padding)
                .padding(start = 21.dp)
                .size(40.dp),
            onClick = {
                navController.popBackStack()
            }
        ) {
            Icon(
                modifier = Modifier.size(26.dp),
                imageVector = MiuixIcons.Useful.Back,
                contentDescription = "Back",
                tint = MiuixTheme.colorScheme.onSurfaceSecondary
            )
        }
    },
    actions: @Composable RowScope.(padding: PaddingValues) -> Unit = {},
    content: LazyListScope.() -> Unit
) {
    val scrollBehavior = MiuixScrollBehavior(rememberTopAppBarState())
    val hazeTint = MiuixTheme.colorScheme.background.copy(
        if (scrollBehavior.state.collapsedFraction <= 0f) 1f
        else lerp(1f, 0.67f, (scrollBehavior.state.collapsedFraction))
    )
    val listState = rememberLazyListState()
    val layoutDirection = LocalLayoutDirection.current
    val systemBarInsets =
        WindowInsets.systemBars.add(WindowInsets.displayCutout).only(WindowInsetsSides.Horizontal).asPaddingValues()
    val navigationIconPadding = PaddingValues.Absolute(
        left = if (mode != BasePageDefaults.Mode.SPLIT_RIGHT) systemBarInsets.calculateLeftPadding(layoutDirection) else 0.dp
    )
    val actionsPadding = PaddingValues.Absolute(
        right = if (mode != BasePageDefaults.Mode.SPLIT_LEFT) systemBarInsets.calculateRightPadding(layoutDirection) else 0.dp
    )
    HazeScaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { contentPadding ->
            TopAppBar(
                modifier = Modifier
                    .windowInsetsPadding(WindowInsets.systemBars.only(WindowInsetsSides.Top)),
                color = if (blurEnabled.value) Color.Transparent else MiuixTheme.colorScheme.background,
                title = title,
                scrollBehavior = scrollBehavior,
                navigationIcon = { navigationIcon.invoke(navigationIconPadding) },
                actions = { actions(this, actionsPadding) },
                defaultWindowInsetsPadding = false,
                horizontalPadding = 28.dp + contentPadding.calculateLeftPadding(LocalLayoutDirection.current)
            )
        },
        blurTopBar = blurEnabled.value,
        hazeStyle = HazeStyle(
            blurRadius = 25.dp,
            noiseFactor = 0f,
            backgroundColor = MiuixTheme.colorScheme.background,
            tint = HazeTint(hazeTint)
        ),
        adjustPadding = adjustPadding,
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .overScrollVertical()
                .nestedScroll(scrollBehavior.nestedScrollConnection)
                .scrollEndHaptic()
                .height(getWindowSize().height.dp)
                .background(MiuixTheme.colorScheme.background),
            state = listState,
            contentPadding = paddingValues,
            content = content
        )
    }
}

object BasePageDefaults {
    enum class Mode {
        FULL,
        SPLIT_LEFT,
        SPLIT_RIGHT
    }
}