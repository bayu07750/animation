package com.sample.animation.data

import androidx.compose.ui.graphics.Color
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
        description = "Premium wireless headphone with active noise cancellation and rich bass.",
        specs = listOf("Bluetooth 5.3", "12h battery", "Type-C charging"),
        color = Color(0xFF90CAF9),
        stock = 15,
        estimatedDelivery = LocalDate.now().plusDays(3)
    ),
    Product(
        id = 2,
        name = "Aura Smartwatch Pro",
        price = 1_499_000,
        description = "AMOLED display with all-day health, sleep tracking, and heart monitoring.",
        specs = listOf("AMOLED 1.43\"", "SpO2 & Heart Rate", "Water Resistant 5ATM"),
        color = Color(0xFFA5D6A7),
        stock = 8,
        estimatedDelivery = LocalDate.now().plusDays(2)
    ),
    Product(
        id = 3,
        name = "Nebula Mechanical Keyboard",
        price = 649_000,
        description = "Compact wireless mechanical keyboard with hot-swappable tactile switches.",
        specs = listOf("RGB Backlit", "Gateron Brown Switches", "Triple Connection Mode"),
        color = Color(0xFFFFCC80),
        stock = 20,
        estimatedDelivery = LocalDate.now().plusDays(4)
    ),
    Product(
        id = 4,
        name = "Pulse Mini Bluetooth Speaker",
        price = 389_000,
        description = "Pocket-sized wireless speaker with 360-degree surround sound.",
        specs = listOf("IPX7 Waterproof", "10h Playback", "Built-in Mic"),
        color = Color(0xFFFFAB91),
        stock = 25,
        estimatedDelivery = LocalDate.now().plusDays(2)
    ),
    Product(
        id = 5,
        name = "Vortex Ergonomic Gaming Mouse",
        price = 429_000,
        description = "Precision optical sensor with customizable DPI and lightweight honeycomb shell.",
        specs = listOf("16000 DPI", "6 Programmable Buttons", "Ultra-light 59g"),
        color = Color(0xFFCE93D8),
        stock = 14,
        estimatedDelivery = LocalDate.now().plusDays(3)
    ),
    Product(
        id = 6,
        name = "Lumina Powerbank 20000mAh",
        price = 299_000,
        description = "High-capacity power bank with fast charging support for multiple devices.",
        specs = listOf("20000mAh", "22.5W Fast Charging", "Dual USB + Type-C"),
        color = Color(0xFF80DEEA),
        stock = 30,
        estimatedDelivery = LocalDate.now().plusDays(1)
    ),
    Product(
        id = 7,
        name = "Nova 4K USB-C Webcam",
        price = 850_000,
        description = "Crystal clear 4K video calling with stereo dual noise-canceling microphones.",
        specs = listOf("4K @ 30FPS", "Auto-focus & Low Light Correction", "Privacy Shutter"),
        color = Color(0xFFB0BEC5),
        stock = 12,
        estimatedDelivery = LocalDate.now().plusDays(3)
    ),
    Product(
        id = 8,
        name = "Echo True Wireless Earbuds",
        price = 499_000,
        description = "Low-latency wireless earbuds with punchy bass and touch controls.",
        specs = listOf("Bluetooth 5.3", "IPX5 Sweatproof", "28h Total Playtime"),
        color = Color(0xFFFFF59D),
        stock = 18,
        estimatedDelivery = LocalDate.now().plusDays(2)
    ),
    Product(
        id = 9,
        name = "HyperGlide Speed Desk Mat",
        price = 149_000,
        description = "Extended water-resistant desk pad with anti-slip rubber base.",
        specs = listOf("900 x 400 mm", "Stitched Edges", "Micro-woven Cloth"),
        color = Color(0xFFB39DDB),
        stock = 40,
        estimatedDelivery = LocalDate.now().plusDays(1)
    ),
    Product(
        id = 10,
        name = "Apex 65W GaN Fast Charger",
        price = 279_000,
        description = "Compact gallium nitride wall charger capable of powering laptops and phones.",
        specs = listOf("65W Output", "GaN III Technology", "2x Type-C + 1x USB-A"),
        color = Color(0xFF80CBC4),
        stock = 22,
        estimatedDelivery = LocalDate.now().plusDays(2)
    ),
    Product(
        id = 11,
        name = "Solstice Smart Desk Lamp",
        price = 359_000,
        description = "Adjustable LED desk light with wireless charging pad and ambient sensor.",
        specs = listOf("Stepless Dimming", "3 Color Temperatures", "10W Wireless Pad"),
        color = Color(0xFFFFE082),
        stock = 10,
        estimatedDelivery = LocalDate.now().plusDays(4)
    ),
    Product(
        id = 12,
        name = "Titan Laptop Stand Aluminum",
        price = 229_000,
        description = "Foldable heavy-duty aluminum riser for ergonomic viewing angles.",
        specs = listOf("Fits 10\"-17\" Laptops", "6 Height Levels", "Heat Dissipation Slots"),
        color = Color(0xFFCFD8DC),
        stock = 16,
        estimatedDelivery = LocalDate.now().plusDays(2)
    ),
    Product(
        id = 13,
        name = "Zenith Condenser Microphone",
        price = 920_000,
        description = "Studio-grade cardioid microphone for podcasting, streaming, and voiceover.",
        specs = listOf("192kHz/24bit", "Zero-latency Monitoring", "Tap-to-Mute Sensor"),
        color = Color(0xFFE1BEE7),
        stock = 7,
        estimatedDelivery = LocalDate.now().plusDays(3)
    ),
    Product(
        id = 14,
        name = "Cascade Magnetic Phone Mount",
        price = 119_000,
        description = "Strong magnetic dashboard and vent mount with 360-degree rotation ball.",
        specs = listOf("N52 Strong Magnets", "Universal Vent Clip", "One-Hand Operation"),
        color = Color(0xFFFFCDD2),
        stock = 35,
        estimatedDelivery = LocalDate.now().plusDays(1)
    ),
    Product(
        id = 15,
        name = "Quantum 1TB Portable SSD NVMe",
        price = 1_250_000,
        description = "Pocket-sized solid state drive with lightning-fast transfer speeds.",
        specs = listOf("1050 MB/s Read Speed", "USB 3.2 Gen 2", "Drop Resistant up to 2m"),
        color = Color(0xFFC5CAE9),
        stock = 9,
        estimatedDelivery = LocalDate.now().plusDays(2)
    )
)

val demoProduct = demoProducts.first()
