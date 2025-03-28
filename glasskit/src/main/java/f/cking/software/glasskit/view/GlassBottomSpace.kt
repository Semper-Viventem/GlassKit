package f.cking.software.glasskit.view

import android.annotation.SuppressLint
import android.graphics.Rect
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import f.cking.software.glasskit.effect.glass.GlassShader
import f.cking.software.glasskit.effect.glass.RefractionMaterial
import f.cking.software.glasskit.effect.glass.Tilt
import f.cking.software.glasskit.effect.glass.glassPanel
import f.cking.software.glasskit.utils.dpToPx
import f.cking.software.glasskit.utils.letIf

internal data object GlassBottomSpaceDefaults {
    const val BLUR = 4f
}

@Composable
fun GlassBottomNavBar(
    modifier: Modifier = Modifier,
    blur: Float = GlassBottomSpaceDefaults.BLUR,
    fallbackColor: Color? = Color.Transparent,
    overlayColor: Color? = null,
    content: @Composable (bottomPadding: PaddingValues) -> Unit,
) {
    GlassBottomSpace(
        modifier = modifier,
        blur = blur,
        fallbackColor = fallbackColor,
        overlayColor = overlayColor,
        bottomContent = { },
        globalContent =  { padding ->
            content(padding)
        }
    )
}

@SuppressLint("NewApi")
@Composable
fun GlassBottomSpace(
    modifier: Modifier = Modifier,
    height: Dp? = null,
    blur: Float = GlassBottomSpaceDefaults.BLUR,
    zIndex: Float = 1f,
    fallbackColor: Color? = Color.Transparent,
    overlayColor: Color? = null,
    bottomContent: @Composable () -> Unit,
    globalContent: @Composable (bottomPadding: PaddingValues) -> Unit,
) {
    Box(modifier = modifier) {
        val context = LocalContext.current
        var navbarHeightPx by remember { mutableStateOf(height?.value?.let(context::dpToPx)?.toFloat()) }
        val isRenderEffectSupported = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .letIf(isRenderEffectSupported && navbarHeightPx != null) {
                    it.glassBottom(heightPx = navbarHeightPx!!, blurRadius = blur)
                }
        ) {
            globalContent(PaddingValues(bottom = navbarHeightPx?.dp ?: 0.dp))
        }
        Box(
            modifier = Modifier
                .zIndex(zIndex)
                .fillMaxWidth()
                .let {
                    if (height == null) {
                        it.onGloballyPositioned {
                            navbarHeightPx = it.size.height.toFloat()
                        }
                    } else {
                        it.height(height)
                    }
                }
                .let {
                    when {
                        !isRenderEffectSupported && fallbackColor != null -> it.background(fallbackColor)
                        overlayColor != null -> it.background(overlayColor)
                        else -> it
                    }
                }
                .align(Alignment.BottomCenter)
        ) {
            Column {
                bottomContent()
            }

            Box(
                Modifier
                    .fillMaxWidth()
                    .height(2.dp)
                    .align(Alignment.TopCenter)
                    .background(Brush.horizontalGradient(listOf(Color.White.copy(alpha = 0.1f), Color.White.copy(alpha = 0.2f), Color.Transparent)))
            )
        }
    }
}

@Composable
@SuppressLint("SuspiciousModifierThen")
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
fun Modifier.glassBottom(
    heightPx: Float,
    curveType: GlassShader.CurveType = GlassShader.CurveType.Mod,
    blurRadius: Float = GlassBottomSpaceDefaults.BLUR,
): Modifier = composed {

    val contentSize = remember { mutableStateOf(Size(0.0f, 0.0f)) }

    this
        .onSizeChanged {
            contentSize.value = Size(it.width.toFloat(), it.height.toFloat())
        }
        .then(
            glassPanel(
                rect = Rect(
                    0,
                    (contentSize.value.height - heightPx).toInt(),
                    contentSize.value.width.toInt(),
                    contentSize.value.height.toInt(),
                ),
                curveType = curveType,
                material = RefractionMaterial.GLASS,
                blurRadius = blurRadius,
                tilt = Tilt.Motion(0.04f, 0.015f),
            )
        )
}