package f.cking.software.glasskit.effect.blur

import android.graphics.Rect
import android.graphics.RenderEffect
import android.graphics.RuntimeShader
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.asComposeRenderEffect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onSizeChanged

@Composable
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
fun Modifier.progressiveBlurShader(
    rect: Rect, blurRadius: Float,
    topMultiplier: Float,
    bottomMultiplier: Float,
): Modifier = composed {

    val glassShader = remember { RuntimeShader(ProgressiveBlurShader.PROGRESSIVE_BLUR_SHADER) }
    var contentSize by remember { mutableStateOf(Size(0.0f, 0.0f)) }


    glassShader.setFloatUniform(ProgressiveBlurShader.ARG_BLUR, blurRadius)

    glassShader.setFloatUniform(ProgressiveBlurShader.ARG_RESOLUTION, contentSize.width.toFloat(), contentSize.height.toFloat())
    glassShader.setFloatUniform(ProgressiveBlurShader.ARG_PANEL_HEIGHT, rect.height().toFloat())
    glassShader.setFloatUniform(ProgressiveBlurShader.ARG_PANEL_WIDTH, rect.width().toFloat())
    glassShader.setFloatUniform(ProgressiveBlurShader.ARG_PANEL_X, rect.left.toFloat())
    glassShader.setFloatUniform(ProgressiveBlurShader.ARG_PANEL_Y, rect.top.toFloat())
    glassShader.setFloatUniform(ProgressiveBlurShader.TOP_MULTIPLIER, topMultiplier)
    glassShader.setFloatUniform(ProgressiveBlurShader.BOTTOM_MULTIPLIER, bottomMultiplier)

    val shaderRenderEffect = remember(rect, contentSize, topMultiplier, bottomMultiplier) {
        RenderEffect
            .createRuntimeShaderEffect(glassShader, ProgressiveBlurShader.ARG_CONTENT)
            .asComposeRenderEffect()
    }

    this
        .onSizeChanged {
            contentSize = Size(it.width.toFloat(), it.height.toFloat())
        }
        .then(
            graphicsLayer {
                renderEffect = shaderRenderEffect
            }
        )
}