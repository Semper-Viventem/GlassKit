package f.cking.software.glasskit.view

import f.cking.software.glasskit.effect.glass.GlassShader
import f.cking.software.glasskit.effect.glass.RefractionMaterial
import f.cking.software.glasskit.effect.glass.Tilt

sealed interface Effect {

    data class Blur(
        val radius: Float = 8f
    ) : Effect

    data class ProgressiveBlur(
        val maxBlurRadius: Float = 8f
    ) : Effect

    data class Glass(
        val material: RefractionMaterial = RefractionMaterial.GLASS,
        val aberrationIndex: Float = 0.1f,
        val curveType: GlassShader.CurveType = GlassShader.CurveType.Mod,
        val blurRadius: Float = 0f,
        val tilt: Tilt = Tilt.Fixed(),
    ) : Effect
}