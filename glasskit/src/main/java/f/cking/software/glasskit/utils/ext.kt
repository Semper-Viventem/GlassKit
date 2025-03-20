package f.cking.software.glasskit.utils

import android.content.Context
import android.util.TypedValue
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract


fun Context.dpToPx(value: Float): Int =
    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, resources.displayMetrics).toInt()

fun Context.pxToDp(value: Float): Float = value / resources.displayMetrics.density

@OptIn(ExperimentalContracts::class)
inline fun <T> T.letIf(condition: () -> Boolean, block: (T) -> T): T {
    contract {
        callsInPlace(block, InvocationKind.EXACTLY_ONCE)
    }
    return if (condition.invoke()) block(this) else this
}

@OptIn(ExperimentalContracts::class)
inline fun <T> T.letIf(condition: Boolean, block: (T) -> T): T {
    contract {
        callsInPlace(block, InvocationKind.EXACTLY_ONCE)
    }
    return if (condition) block(this) else this
}