package f.cking.software.glasskit.effect.blur

import org.intellij.lang.annotations.Language

object ProgressiveBlurShader {

    const val ARG_CONTENT = "content"
    const val ARG_RESOLUTION = "iResolution"
    const val ARG_PANEL_HEIGHT = "panelHeigh"
    const val ARG_PANEL_WIDTH = "panelWidth"
    const val ARG_PANEL_X = "panelX"
    const val ARG_PANEL_Y = "panelY"
    const val ARG_BLUR = "blurRadius"
    const val TOP_MULTIPLIER = "topMultiplier"
    const val BOTTOM_MULTIPLIER = "bottomMultiplier"

    @Language("AGSL")
    val PROGRESSIVE_BLUR_SHADER = """
        uniform shader $ARG_CONTENT;
        uniform float2 $ARG_RESOLUTION;
        uniform float $ARG_PANEL_HEIGHT;
        uniform float $ARG_PANEL_WIDTH;
        uniform float $ARG_PANEL_X;
        uniform float $ARG_PANEL_Y;
        uniform float $ARG_BLUR;
        uniform float $TOP_MULTIPLIER;
        uniform float $BOTTOM_MULTIPLIER;
        
        bool isInsidePanel(float2 fCoord) {
            return fCoord.x >= $ARG_PANEL_X 
                && fCoord.x < $ARG_PANEL_X + $ARG_PANEL_WIDTH 
                && fCoord.y > $ARG_PANEL_Y 
                && fCoord.y < $ARG_PANEL_Y + $ARG_PANEL_HEIGHT;
        }
        
        float4 blurred(float2 fragCoord) {
            const float Pi = 6.28318530718; // Pi*2
            const float Directions = 16.0;
            const float Quality = 3.0;
            float maxMultiplier = max($BOTTOM_MULTIPLIER, $TOP_MULTIPLIER);
            float minMultiplier = min($BOTTOM_MULTIPLIER, $TOP_MULTIPLIER);
            float progress = (fragCoord.y - $ARG_PANEL_Y) / $ARG_PANEL_HEIGHT;
            if ($BOTTOM_MULTIPLIER < $TOP_MULTIPLIER) {
                progress = 1 - progress;
            } else if ($BOTTOM_MULTIPLIER == $TOP_MULTIPLIER) {
                progress = 1;
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
        
            if ($TOP_MULTIPLIER == $BOTTOM_MULTIPLIER && !isInsidePanel(fragCoord)) {
                return content.eval(fragCoord);
            }
            
            return blurred(fragCoord);
        }
    """
}