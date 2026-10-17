package com.sample.animation.data

import androidx.compose.ui.graphics.Color
import kotlinx.collections.immutable.persistentListOf
import java.time.LocalDate

val promoBanners = listOf(
    PromoBanner(id = 1, title = "Midnight Sale", color = Color(0xFFE57373)),
    PromoBanner(id = 2, title = "Buy 1 Get 1", color = Color(0xFF64B5F6)),
    PromoBanner(id = 3, title = "Flash Deal", color = Color(0xFF81C784))
)

val demoProducts = listOf(
    Product(
        id = 1,
        name = "Stellar Wireless Headphones",
        price = 799_000,
        description = "Premium wireless headphone with noise cancellation.",
        specs = persistentListOf("Bluetooth 5.3", "12h battery", "Type-C charging"),
        color = Color(0xFF90CAF9),
        stock = 15,
        estimatedDelivery = LocalDate.now().plusDays(3)
    ),
    Product(
        id = 2,
        name = "Aura Smartwatch Pro",
        price = 1_499_000,
        description = "AMOLED display with all-day health & sleep tracking.",
        specs = persistentListOf("AMOLED 1.43\"", "SpO2 & Heart Rate", "Water Resistant 5ATM"),
        color = Color(0xFFA5D6A7),
        stock = 8,
        estimatedDelivery = LocalDate.now().plusDays(2)
    ),
    Product(
        id = 3,
        name = "Nebula Mechanical Keyboard",
        price = 649_000,
        description = "Compact wireless mechanical keyboard with hot-swappable switches.",
        specs = persistentListOf("RGB Backlit", "Gateron Brown Switches", "Triple Connection Mode"),
        color = Color(0xFFFFCC80),
        stock = 20,
        estimatedDelivery = LocalDate.now().plusDays(4)
    )
)

val demoProduct = demoProducts.first()
