package com.sample.animation.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.sample.animation.data.Product
import com.sample.animation.data.demoProducts
import com.sample.animation.data.promoBanners
import com.sample.animation.ui.components.BannerSlider
import com.sample.animation.ui.components.CartBadge
import kotlinx.coroutines.delay

/*
 * TODO: Step 3.1 — Recomposition Debugging: Android Studio Layout Inspector
 * Instruksi:
 * 1. Jalankan aplikasi dalam mode Debug pada emulator atau physical device.
 * 2. Buka jendela `Layout Inspector` di Android Studio (View > Tool Windows > Layout Inspector).
 * 3. Centang opsi "Show Recomposition Counts" pada menu dropdown di pojok kiri atas.
 * 4. Amati kolom "Recomposition" dan "Skipped" pada `HomeScreen` saat banner beranimasi otomatis.
 * 5. Bandingkan jumlah recomposition sebelum dan sesudah menerapkan perbaikan Step 1 & Step 2.
 *
 * TODO: Step 3.2 — Recomposition Debugging: Compose Compiler Reports
 * Instruksi:
 * 1. Buka file `app/build.gradle.kts`, periksa blok `composeCompiler { ... }` dengan `reportsDestination`.
 * 2. Jalankan build di terminal: `./gradlew assembleDebug`.
 * 3. Buka folder `app/build/compose_compiler_report/` dan periksa:
 *    - `app-classes.txt`: Temukan `unstable class Product` dan identifikasi field penyebabnya.
 *    - `app-composables.txt`: Periksa Composable yang `restartable not skippable` vs `restartable skippable`.
 *
 * TODO: Step 3.3 — Recomposition Debugging: Plugin Compose Stability Analyzer
 * Instruksi:
 * 1. Pasang plugin "Compose Stability Analyzer" (oleh skydoves) dari Settings > Plugins > Marketplace.
 * 2. Buka file ini atau `Product.kt`, amati badge stabilitas (hijau/merah) langsung di samping parameter Composable.
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    cartCount: Int,
    onBannerClick: () -> Unit,
    onCartClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var currentIndex by rememberSaveable { mutableIntStateOf(0) }

    LaunchedEffect(currentIndex) {
        delay(2000)
        currentIndex = (currentIndex + 1) % promoBanners.size
    }

    Surface(modifier = modifier.fillMaxSize()) {
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            TopAppBar(
                title = {
                    Column {
                        Text(text = "Compose Performance Training", style = MaterialTheme.typography.titleMedium)
                        Text(
                            text = "Home",
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Light)
                        )
                    }
                },
                actions = {
                    CartBadge(count = cartCount, onClick = onCartClick)
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground
                )
            )

            // Step 1.5 Solved: Using key and contentType in LazyColumn to preserve identity and layout pooling
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item(contentType = "banner_carousel") {
                    // Step 1.3 Solved: Passing lambda provider { currentIndex } defers state read to leaf composable
                    BannerSlider(
                        banners = promoBanners,
                        currentIndexProvider = { currentIndex },
                        onBannerClick = { onBannerClick() },
                        onIndicatorSelect = { index -> currentIndex = index },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                item(contentType = "catalog_header") {
                    Text(
                        text = "Featured Products",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }

                items(
                    items = demoProducts,
                    key = { it.id },
                    contentType = { "product_catalog_item" }
                ) { product ->
                    ProductCatalogItem(
                        product = product,
                        onClick = onBannerClick
                    )
                }

                item(contentType = "bottom_spacer") {
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

@Composable
fun ProductCatalogItem(
    product: Product,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = product.color.copy(alpha = 0.2f))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = product.name, style = MaterialTheme.typography.titleMedium)
                Text(
                    text = "Rp ${product.price}",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                )
                Text(
                    text = "Stock: ${product.stock} • Est. Arrival: ${product.estimatedDelivery}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}
