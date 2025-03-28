package f.cking.software.glasskit.view

import android.annotation.SuppressLint
import android.graphics.Rect
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import f.cking.software.glasskit.effect.blur.blurShader
import f.cking.software.glasskit.effect.blur.progressiveBlurShader
import f.cking.software.glasskit.effect.glass.glassPanel
import f.cking.software.glasskit.utils.letIf
import f.cking.software.glasskit.utils.letIfNotNull
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

        val tint = remember(effect) { (effect as? Effect.WithTint)?.tint }
        val gradientTarget = remember(effect) {
            (effect as? Effect.ProgressiveBlur)?.tint
        }

        var topBarHeight by remember { mutableIntStateOf(0) }
        var bottomBarHeight by remember { mutableIntStateOf(0) }
        var height by remember { mutableIntStateOf(0) }
        var width by remember { mutableIntStateOf(0) }


        val isRenderEffectSupported = remember { Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU }

        Box(
            modifier = Modifier.fillMaxSize()
                .onGloballyPositioned {
                    width = it.size.width
                    height = it.size.height
                }
                .letIf(isRenderEffectSupported) { contentModifier ->
                    contentModifier
                        .withEffect(effect, topBarHeight, bottomBarHeight, width, height)
                }
        ) {
            content(PaddingValues(top = topBarHeight.toDp(), bottom = bottomBarHeight.toDp()))
        }

        Box(
            modifier = Modifier.align(Alignment.TopCenter)
                .letIfNotNull(tint) { modifier, tint ->
                    modifier.background(tint)
                }
                .letIfNotNull(gradientTarget) { modifier, gradientTarget ->
                    modifier.background(Brush.verticalGradient(listOf(
                        gradientTarget,
                        Color.Transparent,
                    )))
                }
                .onGloballyPositioned { topBarHeight = it.size.height }
        ) {
            topBar()
        }

        Box(
            modifier = Modifier.align(Alignment.BottomCenter)
                .letIfNotNull(tint) { modifier, tint ->
                    modifier.background(tint)
                }
                .letIfNotNull(gradientTarget) { modifier, gradientTarget ->
                    modifier.background(Brush.verticalGradient(listOf(
                        Color.Transparent,
                        gradientTarget,
                    )))
                }
                .onGloballyPositioned { bottomBarHeight = it.size.height }
        ) {
            bottomBar()
        }
    }
}

@Composable
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
private fun Modifier.withEffect(
    effect: Effect,
    topBarHeight: Int,
    bottomBarHeight: Int,
    width: Int,
    height: Int
): Modifier {

    val topRect = remember (topBarHeight, width) {
        Rect(0, 0, width, topBarHeight)
    }

    val bottomRect = remember (bottomBarHeight, width, height) {
        Rect(0, height - bottomBarHeight, width, height)
    }

    return when (effect) {
        is Effect.Glass -> this.withGlassEffect(effect, topRect).withGlassEffect(effect, bottomRect)
        is Effect.ProgressiveBlur -> this.withProgressiveBlurEffect(effect, topBarHeight, bottomBarHeight)
        is Effect.Blur -> this.withBlurEffect(effect, topRect).withBlurEffect(effect, bottomRect)
    }
}

@Composable
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
private fun Modifier.withGlassEffect(effect: Effect.Glass, rect: Rect): Modifier {
    return this.glassPanel(
        rect = rect,
        material = effect.material,
        aberrationIndex = effect.aberrationIndex,
        curveType = effect.curveType,
        blurRadius = effect.blurRadius,
        tilt = effect.tilt,
    )
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
private fun Modifier.withProgressiveBlurEffect(effect: Effect.ProgressiveBlur, topHeight: Int, bottomHeight: Int): Modifier {
    return this.progressiveBlurShader(
        blurRadius = effect.maxBlurRadius,
        topHeight = (topHeight * effect.bottomMultiplier).toInt(),
        bottomHeight = (bottomHeight * effect.bottomMultiplier).toInt(),
    )
}

@Composable
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
private fun Modifier.withBlurEffect(effect: Effect.Blur, rect: Rect): Modifier {
    return this.blurShader(rect, effect.radius)
}