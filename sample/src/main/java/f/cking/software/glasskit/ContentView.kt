package f.cking.software.glasskit

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import f.cking.software.glasskit.view.Effect
import f.cking.software.glasskit.view.GlassScaffold

object ContentView {

    @Composable
    fun Screen() {
        var currentEffect by remember { mutableStateOf(BottomBarItem.GLASS) }

        GlassScaffold(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            topBar = {
                Header()
            },
            content = {
                Content()
            },
            bottomBar = {
                BottomBar(
                    selected = currentEffect,
                    onEffectSelected = { selected -> currentEffect = selected }
                )
            },
            effect = currentEffect.effect,
        )
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun BottomBar(
        selected: BottomBarItem,
        onEffectSelected: (selected: BottomBarItem) -> Unit,
    ) {
        Row(Modifier.fillMaxWidth().height(56.dp).background(TopAppBarDefaults.topAppBarColors().containerColor.copy(0.3f))) {
            BottomBarItem.entries.forEach { effect ->
                Column(
                    modifier = Modifier.weight(1f).fillMaxHeight()
                        .clickable { onEffectSelected.invoke(effect) },
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    Icon(
                        painter = painterResource(effect.iconRes),
                        contentDescription = stringResource(effect.stringRes),
                        tint = if (selected == effect) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
                    )
                    Text(stringResource(effect.stringRes))
                }
            }
        }
    }

    @Composable
    private fun Content() {
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
        ) {
            repeat(3) {
                Image(
                    modifier = Modifier.fillMaxWidth(),
                    painter = painterResource(id = R.drawable.monstera),
                    contentDescription = "",
                    contentScale = ContentScale.FillWidth
                )
                Image(
                    modifier = Modifier.fillMaxWidth(),
                    painter = painterResource(id = R.drawable.appa),
                    contentDescription = "",
                    contentScale = ContentScale.FillWidth
                )
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun Header() {
        TopAppBar(
            colors = TopAppBarDefaults.topAppBarColors().copy(containerColor = TopAppBarDefaults.topAppBarColors().containerColor.copy(0.3f)),
            title = {
                Text(stringResource(R.string.app_name))
            }
        )
    }

    private enum class BottomBarItem(
        val effect: Effect,
        @StringRes val stringRes: Int,
        @DrawableRes val iconRes: Int,
    ) {
        GLASS(
            effect = Effect.Glass(blurRadius = 8f),
            stringRes = R.string.effect_name_glass,
            iconRes = R.drawable.ic_glass,
        ),
        PROGRESSIVE_BLUR(
            effect = Effect.ProgressiveBlur(),
            stringRes = R.string.effect_name_progressive_blur,
            iconRes = R.drawable.ic_progressive_blur,
        ),
        BLUR(
            effect = Effect.Blur(),
            stringRes = R.string.effect_name_blur,
            iconRes = R.drawable.ic_blur,
        ),
    }
}