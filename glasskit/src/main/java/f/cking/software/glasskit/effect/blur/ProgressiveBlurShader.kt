package f.cking.software.glasskit.effect.blur

import org.intellij.lang.annotations.Language

object ProgressiveBlurShader {

    const val ARG_CONTENT = "content"
    const val ARG_RESOLUTION = "iResolution"
    const val ARG_BLUR = "blurRadius"
    const val ARG_TOP_HEIGHT = "topHeight"
    const val ARG_BOTTOM_HEIGHT = "bottomHeight"

    @Language("AGSL")
    val PROGRESSIVE_BLUR_SHADER = """
        uniform shader $ARG_CONTENT;
        uniform float2 $ARG_RESOLUTION;
        uniform float $ARG_BLUR;
        uniform float $ARG_TOP_HEIGHT;
        uniform float $ARG_BOTTOM_HEIGHT;
        
        float4 blurred(float2 fragCoord, bool rotated, float blurY, float blutHeight) {
            const float Pi = 6.28318530718; // Pi*2
            const float Directions = 16.0;
            const float Quality = 3.0;
            float maxMultiplier = 1;
            float minMultiplier = 0;
            float progress = (fragCoord.y - blurY) / blutHeight;
            if (rotated) {
                progress = 1 - progress;
            }
            
            float multiplier = minMultiplier + (maxMultiplier - minMultiplier) * min(1, max(0, progress));
            float Size = $ARG_BLUR * multiplier; // BLUR SIZE (Radius)
            
            float2 Radius = Size / iResolution.xy;
            
            float2 uv = fragCoord / iResolution; // Normalize screen coordinates
            // Pixel color
            float4 Color = $ARG_CONTENT.eval(fragCoord);
            
            // Blur calculations
            float sampleCount = 0.0;
            for (float d = 0.0; d < Pi; d += Pi / Directions) {
                for (float i = 1.0 / Quality; i <= 1.0; i += 1.0 / Quality) {
                    Color += $ARG_CONTENT.eval((uv + float2(cos(d), sin(d)) * Radius * i) * iResolution);
                    sampleCount += 1.0;
                }
            }
            
            // Average the color by the total sample count
            Color /= sampleCount;
            
            // Output to screen
            return Color;
        }
        
        float4 main(float2 fragCoord) {
        
            if (fragCoord.y < iResolution.y / 2) {
                return blurred(fragCoord, true, 0, $ARG_TOP_HEIGHT);
            } else {
                return blurred(fragCoord, false, iResolution.y - $ARG_BOTTOM_HEIGHT, $ARG_BOTTOM_HEIGHT);
            }
        }
    """
}