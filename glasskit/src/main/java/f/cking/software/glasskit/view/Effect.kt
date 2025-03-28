package f.cking.software.glasskit.view

import androidx.compose.ui.graphics.Color
import f.cking.software.glasskit.effect.glass.GlassShader
import f.cking.software.glasskit.effect.glass.RefractionMaterial
import f.cking.software.glasskit.effect.glass.Tilt

sealed interface Effect {

    data class Blur(
        val radius: Float = 16f,
        override val tint: Color = Color.Transparent,
    ) : Effect, WithTint

    data class ProgressiveBlur(
        val maxBlurRadius: Float = 24f
    ) : Effect

    data class Glass(
        val material: RefractionMaterial = RefractionMaterial.GLASS,
        val aberrationIndex: Float = 0.1f,
        val curveType: GlassShader.CurveType = GlassShader.CurveType.Mod,
        val blurRadius: Float = 0f,
        val tilt: Tilt = Tilt.Fixed(),
        override val tint: Color = Color.Transparent,
    ) : Effect, WithTint

    interface WithTint {
        val tint: Color
    }
}