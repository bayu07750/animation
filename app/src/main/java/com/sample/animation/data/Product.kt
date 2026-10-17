package com.sample.animation.data

import androidx.compose.ui.graphics.Color
import kotlinx.collections.immutable.ImmutableList
import java.time.LocalDate

data class Product(
    val id: Int,
    val name: String,
    val price: Int,
    val description: String,
    // Step 2.2 Solved: Using ImmutableList to ensure collection stability
    val specs: ImmutableList<String>,
    val color: Color,
    // Step 2.1 Solved: Prefer val over var for immutability and stable contract
    val stock: Int = 10,
    // Step 2.3 Solved: Stabilized external type java.time.LocalDate via compose_compiler_config.conf
    val estimatedDelivery: LocalDate = LocalDate.now().plusDays(3)
)
