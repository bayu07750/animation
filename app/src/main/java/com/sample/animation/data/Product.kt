package com.sample.animation.data

import androidx.compose.ui.graphics.Color
import java.time.LocalDate

data class Product(
    val id: Int,
    val name: String,
    val price: Int,
    val description: String,
    // TODO: Step 2.2 — Stability: Use Immutable Collections
    // Masalah: Menggunakan `List<String>` bawaan Kotlin standar dianggap `unstable` oleh Compose Compiler
    // karena interface List di JVM dapat di-cast ke mutable collection seperti ArrayList.
    // Akibatnya, Composable yang menerima Product akan berstatus `restartable but not skippable`.
    // Tindakan: Ubah tipe `List<String>` menjadi `kotlinx.collections.immutable.ImmutableList<String>`.
    val specs: List<String>,
    val color: Color,
    // TODO: Step 2.1 — Stability: Prefer val over var
    // Masalah: Properti `var` mutable menyebabkan Compose Compiler tidak dapat menjamin kestabilan objek,
    // sehingga menandai data class ini sebagai `unstable`.
    // Tindakan: Ubah `var stock: Int` menjadi `val stock: Int`.
    var stock: Int = 10,
    // TODO: Step 2.3 — Stability: Handle External & Java Types via compose_compiler_config.conf
    // Masalah: Tipe `java.time.LocalDate` adalah tipe standard library Java non-Compose yang tidak memiliki anotasi stabilitas,
    // sehingga membuat Composable yang mengonsumsinya menjadi tidak skippable.
    // Tindakan: Daftarkan `java.time.LocalDate` di `compose_compiler_config.conf` dan verifikasi kestabilannya di compiler reports.
    val estimatedDelivery: LocalDate = LocalDate.now().plusDays(3)
)
