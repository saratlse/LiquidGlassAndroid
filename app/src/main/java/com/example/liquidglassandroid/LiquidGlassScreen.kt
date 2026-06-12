package com.example.liquidglassandroid

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.liquidglassandroid.ui.theme.LiquidGlassAndroidTheme
import com.kyant.backdrop.Backdrop
import com.kyant.backdrop.backdrops.layerBackdrop
import com.kyant.backdrop.backdrops.rememberLayerBackdrop
import com.kyant.backdrop.drawBackdrop
import com.kyant.backdrop.effects.blur
import com.kyant.backdrop.effects.lens
import com.kyant.backdrop.effects.vibrancy
import kotlin.math.roundToInt

/**
 * Liquid Glass (Android) : réfraction + dispersion chromatique (lib Kyant0 `backdrop`).
 * - une BULLE de verre qu'on glisse sur le texte (réfraction + dispersion 🌈)
 * - un BOUTON de verre qui se CONTRACTE de façon élastique quand on APPUIE (presse & maintiens).
 */
@Composable
fun LiquidGlassScreen(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize()) {
        val backdrop = rememberLayerBackdrop()
        var offset by remember { mutableStateOf(Offset.Zero) }
        var clicks by remember { mutableIntStateOf(0) }

        // 1) FOND BLANC + TEXTE NOIR CENTRÉ = la source capturée
        Column(
            Modifier
                .fillMaxSize()
                .layerBackdrop(backdrop)
                .background(Color.White)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            repeat(20) {
                Text("LIQUID GLASS ✦", color = Color.Black, fontSize = 30.sp, fontWeight = FontWeight.Black)
            }
        }

        // Compteur (feedback visible du bouton)
        Text(
            "Clics : $clicks",
            color = Color.Black,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.TopCenter).padding(top = 64.dp),
        )

        // 2) LA BULLE DE VERRE déplaçable (réfraction + dispersion chromatique)
        Box(
            Modifier
                .align(Alignment.Center)
                .offset { IntOffset(offset.x.roundToInt(), offset.y.roundToInt()) }
                .size(190.dp)
                .drawBackdrop(
                    backdrop = backdrop,
                    shape = { CircleShape },
                    effects = {
                        vibrancy()
                        blur(1f.dp.toPx())
                        lens(28f.dp.toPx(), 56f.dp.toPx(), depthEffect = true, chromaticAberration = true)
                    },
                )
                .pointerInput(Unit) {
                    detectDragGestures { change, drag ->
                        change.consume()
                        offset += drag
                    }
                },
        )

        // 3) LE BOUTON VERRE — se contracte (élastique) quand on appuie, et compte les clics
        LiquidGlassButton(
            text = "Appuie & maintiens ✦",
            backdrop = backdrop,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .padding(bottom = 40.dp),
        ) { clicks++ }
    }
}

/** Bouton de verre qui se CONTRACTE fort (ressort rebondissant) à l'appui = effet liquide élastique. */
@Composable
fun LiquidGlassButton(
    text: String,
    backdrop: Backdrop,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    val interaction = remember { MutableInteractionSource() }
    val pressed by interaction.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.80f else 1f,          // contraction bien visible
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy, // le rebond élastique
            stiffness = Spring.StiffnessLow,                // assez lent pour bien le voir
        ),
        label = "scale",
    )

    Box(
        modifier
            .drawBackdrop(
                backdrop = backdrop,
                shape = { RoundedCornerShape(percent = 50) },
                effects = {
                    vibrancy()
                    blur(2f.dp.toPx())
                    lens(14f.dp.toPx(), 28f.dp.toPx(), chromaticAberration = true)
                },
                layerBlock = {                  // on déforme le calque verre selon l'appui
                    scaleX = scale
                    scaleY = scale
                },
            )
            .clickable(
                interactionSource = interaction,
                indication = null,
                onClick = onClick,
            )
            .padding(horizontal = 40.dp, vertical = 22.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(text, color = Color.Black, fontSize = 18.sp, fontWeight = FontWeight.Bold)
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 760)
@Composable
private fun LiquidGlassPreview() {
    LiquidGlassAndroidTheme {
        LiquidGlassScreen()
    }
}
