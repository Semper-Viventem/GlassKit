package f.cking.software.glasskit.view

import android.annotation.SuppressLint
import android.graphics.Rect
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import f.cking.software.glasskit.effect.glass.glassPanel
import f.cking.software.glasskit.utils.letIf
import f.cking.software.glasskit.utils.toDp

@SuppressLint("NewApi")
@Composable
fun GlassScaffold(
    effect: Effect,
    modifier: Modifier = Modifier,
    topBar: @Composable () -> Unit = {},
    bottomBar: @Composable () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit
) {

    Box(
        modifier = modifier
    ) {

        var topBarHeight by remember { mutableIntStateOf(0) }
        var bottomBarHeight by remember { mutableIntStateOf(0) }
        var height by remember { mutableIntStateOf(0) }
        var width by remember { mutableIntStateOf(0) }

        val topRect = remember (topBarHeight, width) {
            Rect(0, 0, width, topBarHeight)
        }

        val bottomRect = remember (bottomBarHeight, width, height) {
            Rect(0, height - bottomBarHeight, width, height)
        }

        val isRenderEffectSupported = remember { Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU }

        Box(
            modifier = Modifier.fillMaxSize()
                .onGloballyPositioned {
                    width = it.size.width
                    height = it.size.height
                }
                .letIf(isRenderEffectSupported) { contentModifier ->
                    contentModifier
                        .withEffect(effect, topRect)
                        .withEffect(effect, bottomRect)
                }
        ) {
            content(PaddingValues(top = topBarHeight.toDp(), bottom = bottomBarHeight.toDp()))
        }

        Box(
            modifier = Modifier.align(Alignment.TopCenter)
                .onGloballyPositioned { topBarHeight = it.size.height }
        ) {
            topBar()
        }

        Box(
            modifier = Modifier.align(Alignment.BottomCenter)
                .onGloballyPositioned { bottomBarHeight = it.size.height }
        ) {
            bottomBar()
        }
    }
}

@Composable
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
private fun Modifier.withEffect(effect: Effect, rect: Rect): Modifier {
    return when (effect) {
        is Effect.Glass -> this.withGlassEffect(effect, rect)
        is Effect.ProgressiveBlur -> this.withProgressiveBlurEffect(effect, rect)
        is Effect.Blur -> this.withBlurEffect(effect, rect)
    }
}

@Composable
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
private fun Modifier.withGlassEffect(effect: Effect.Glass, rect: Rect): Modifier {
    return this.then(
        glassPanel(
            rect = rect,
            material = effect.material,
            aberrationIndex = effect.aberrationIndex,
            curveType = effect.curveType,
            blurRadius = effect.blurRadius,
            tilt = effect.tilt,
        )
    )
}

@Composable
private fun Modifier.withProgressiveBlurEffect(effect: Effect.ProgressiveBlur, rect: Rect): Modifier {
    throw NotImplementedError("Not implemented yet")
    return this
}

@Composable
private fun Modifier.withBlurEffect(effect: Effect.Blur, rect: Rect): Modifier {
    throw NotImplementedError("Not implemented yet")
    return this
}