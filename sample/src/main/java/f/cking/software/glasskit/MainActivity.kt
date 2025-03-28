package f.cking.software.glasskit

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val colors = themeColorScheme()
            MaterialTheme(
                colorScheme = colors,
                typography = Typography(
                    bodyMedium = MaterialTheme.typography.bodyMedium.copy(color = colors.onSurface),
                    bodyLarge = MaterialTheme.typography.bodyLarge.copy(color = colors.onSurface),
                    bodySmall = MaterialTheme.typography.bodySmall.copy(color = colors.onSurface),
                ),
                content = {
                    ContentView.Screen()
                }
            )
        }
    }

    @Composable
    private fun themeColorScheme(): ColorScheme {
        val darkMode = isSystemInDarkTheme()
        return if (darkMode) {
            dynamicDarkColorScheme(this)
        } else {
            dynamicLightColorScheme(this)
        }
    }
}