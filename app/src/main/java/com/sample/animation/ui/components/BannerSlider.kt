package com.sample.animation.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.sample.animation.data.PromoBanner
import kotlinx.coroutines.delay

@Composable
fun BannerSlider(
    banners: List<PromoBanner>,
    currentIndex: Int,
    onBannerClick: (PromoBanner) -> Unit,
    onIndicatorSelect: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val activeBanner = banners[currentIndex]
    var breathe by remember { mutableStateOf(false) }

    LaunchedEffect(activeBanner) {
        while (true) {
            breathe = !breathe
            delay(1200)
        }
    }

    val scale by animateFloatAsState(
        targetValue = if (breathe) 1.02f else 0.98f,
        animationSpec = tween(durationMillis = 1200),
        label = "banner_scale"
    )
    val offset by animateFloatAsState(
        targetValue = 0f,
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
        label = "banner_offset"
    )

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                // TODO: Step 1.1 — Composable Best Practice: Use Lambda Modifiers
                // Masalah: Menggunakan `Modifier.graphicsLayer(...)` dengan parameter nilai langsung menyebabkan
                // Composable Card mengalami recomposition di setiap frame perubahan nilai animasi (fase Composition).
                // Tindakan: Ubah menjadi lambda modifier `Modifier.graphicsLayer { translationX = ...; scaleX = ...; scaleY = ... }`
                // agar kalkulasi animasi langsung dieksekusi pada fase Drawing dan melewati fase Composition & Layout.
                .graphicsLayer(
                    translationX = offset,
                    scaleX = scale,
                    scaleY = scale
                )
                .clickable { onBannerClick(activeBanner) },
            colors = CardDefaults.cardColors(containerColor = activeBanner.color),
            shape = RoundedCornerShape(24.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = activeBanner.title,
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onPrimary
                )
                Text(
                    text = "Discover great offers today",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            banners.forEachIndexed { index, _ ->
                val indicatorScale by animateFloatAsState(
                    targetValue = if (index == currentIndex) 1.3f else 1f,
                    animationSpec = tween(durationMillis = 300),
                    label = "indicator_scale"
                )
                val color by animateColorAsState(
                    targetValue = if (index == currentIndex) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline,
                    animationSpec = tween(durationMillis = 300),
                    label = "indicator_color"
                )
                Spacer(
                    modifier = Modifier
                        .size(10.dp)
                        .graphicsLayer(
                            scaleX = indicatorScale,
                            scaleY = indicatorScale
                        )
                        .clickable { onIndicatorSelect(index) }
                        .background(color, shape = RoundedCornerShape(50))
                )
            }
        }
    }
}
