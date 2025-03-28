package f.cking.software.glasskit

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp


@Composable
internal fun Int.toDp(): Dp {
    return Dp(LocalContext.current.pxToDp(this.toFloat()))
}

internal fun Context.pxToDp(value: Float): Float = value / resources.displayMetrics.density