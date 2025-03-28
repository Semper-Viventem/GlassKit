package f.cking.software.glasskit.utils

import android.content.Context
import android.util.TypedValue
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract


internal fun Context.dpToPx(value: Float): Int =
    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, resources.displayMetrics).toInt()

internal fun Context.pxToDp(value: Float): Float = value / resources.displayMetrics.density

@OptIn(ExperimentalContracts::class)
internal inline fun <T> T.letIf(condition: () -> Boolean, block: (T) -> T): T {
    contract {
        callsInPlace(block, InvocationKind.EXACTLY_ONCE)
    }
    return if (condition.invoke()) block(this) else this
}

@OptIn(ExperimentalContracts::class)
internal inline fun <T> T.letIf(condition: Boolean, block: (T) -> T): T {
    contract {
        callsInPlace(block, InvocationKind.EXACTLY_ONCE)
    }
    return if (condition) block(this) else this
}

@OptIn(ExperimentalContracts::class)
internal inline fun <T, R> T.letIfNotNull(arg: R?, block: (T, R) -> T): T {
    contract {
        callsInPlace(block, InvocationKind.EXACTLY_ONCE)
    }
    return if (arg != null) block(this, arg) else this
}

@Composable
internal fun Float.toPx(): Int {
    return LocalContext.current.dpToPx(this)
}

@Composable
internal fun Int.toDp(): Dp {
    return Dp(LocalContext.current.pxToDp(this.toFloat()))
}